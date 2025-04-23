package hr.foi.rmai.memento

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.os.SystemClock
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import hr.foi.rmai.memento.adapters.MainPagerAdapter
import hr.foi.rmai.memento.fragments.CompletedFragment
import hr.foi.rmai.memento.fragments.NewsFragment
import hr.foi.rmai.memento.fragments.PendingFragment
import androidx.core.view.get
import androidx.preference.PreferenceManager
import hr.foi.rmai.memento.database.TasksDatabase
import hr.foi.rmai.memento.helpers.MockDataLoader
import hr.foi.rmai.memento.helpers.TaskDeletionServiceHelper
import hr.foi.rmai.memento.services.TaskDeletionService
import java.util.Locale

class MainActivity : AppCompatActivity() {
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager2

    lateinit var navDrawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    private var taskDeletionServiceHelper = TaskDeletionServiceHelper(this)

    lateinit var onSharedPreferencesListener : OnSharedPreferenceChangeListener

    private val settingsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_LANG_CHANGED) {
                recreate()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupQualityOfLifeImprovements()

        setupNotificationChannel()

        navDrawerLayout = findViewById(R.id.nav_drawer_layout)
        navView = findViewById(R.id.nav_view)

        setupTabNavigation()
        TasksDatabase.buildInstance(this)
        MockDataLoader.loadMockData()

        activateTaskDeletionService()
    }

    private fun activateTaskDeletionService() {
        taskDeletionServiceHelper.activateTaskDeletionService { deletedTaskId ->
            supportFragmentManager.setFragmentResult(
                "task_deleted",
                bundleOf("task_id" to deletedTaskId)
            )
        }
    }

    private fun setupNotificationChannel() {
        val channel = NotificationChannel ("task-timer", "Task Timer Channel",
            NotificationManager.IMPORTANCE_HIGH)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun applyUserSettings(newContext: Context?) : Context? {
        if (newContext == null) return newContext

        PreferenceManager.getDefaultSharedPreferences(newContext).let { pref ->
            PreferencesActivity.switchDarkMode(
                pref.getBoolean("preference_dark_mode", false)
            )

            val lang = pref.getString("preference_language", "EN")
            if (lang != null) {
                val locale = Locale(lang)
                if (newContext.resources.configuration.locales[0].language != locale.language) {
                    newContext.resources.configuration.setLocale(locale)
                    Locale.setDefault(locale)
                    return newContext.createConfigurationContext(newContext.resources.configuration)
                }
            }
        }

        return newContext
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(applyUserSettings(newBase))
    }

    private fun setupTabNavigation() {
        tabLayout = findViewById(R.id.tabs)
        viewPager = findViewById(R.id.viewPager)

        val viewPagerAdapter = MainPagerAdapter(supportFragmentManager, lifecycle)
        fillAdapterWithFragments(viewPagerAdapter)

        viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setIcon(viewPagerAdapter.fragmentItems[position].iconRes)
            tab.setText(viewPagerAdapter.fragmentItems[position].titleRes)
        }.attach()

        viewPagerAdapter.fragmentItems.withIndex().forEach { (index, fragmentItem) ->
            navView.menu
                .add(fragmentItem.titleRes)
                .setIcon(fragmentItem.iconRes)
                .setCheckable(true)
                .setChecked((index == 0))
                .setOnMenuItemClickListener {
                    viewPager.setCurrentItem(index, true)
                    navDrawerLayout.closeDrawers()
                    return@setOnMenuItemClickListener true
                }
        }

        val tasksCounterItem = navView.menu
            .add(2, 0, 0,
                "Tasks created: ${getTasksCreatedCount()}")
            .setEnabled(false)

        attachMenuItemToTasksCreatedCount(tasksCounterItem)

        navView.menu
            .add(3, 0, 0, getString(R.string.settings_menu_item))
            .setIcon(R.drawable.baseline_settings_applications_24)
            .setOnMenuItemClickListener {
                settingsLauncher.launch(Intent(this, PreferencesActivity::class.java))

                navDrawerLayout.closeDrawers()

                return@setOnMenuItemClickListener true
            }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                navView.menu[position].isChecked = true
            }
        })
    }

    private fun attachMenuItemToTasksCreatedCount(tasksCounterItem: MenuItem) {
        val sharedPreferences = getSharedPreferences("tasks_preferences", MODE_PRIVATE)

        onSharedPreferencesListener =
            OnSharedPreferenceChangeListener { _, key ->
                if (key == "tasks_created_counter") {
                    updateTasksCreatedCounter(tasksCounterItem, sharedPreferences)
                }
            }

        sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferencesListener)
        updateTasksCreatedCounter(tasksCounterItem, sharedPreferences)
    }

    private fun updateTasksCreatedCounter(tasksCounterItem: MenuItem,
                                          sharedPreferences: SharedPreferences) {
        val tasksCreated = sharedPreferences.getInt("tasks_created_counter", 0)
        tasksCounterItem.title = "Tasks created: $tasksCreated"
    }

    private fun getTasksCreatedCount(): Int {
        val sharedPreferences = getSharedPreferences(
            "tasks_preferences", MODE_PRIVATE
        )
        return sharedPreferences.getInt("tasks_created_counter", 0)
    }

    private fun fillAdapterWithFragments(viewPagerAdapter: MainPagerAdapter) {
        viewPagerAdapter.addFragment(
            MainPagerAdapter.FragmentItem(
                R.string.tasks_pending,
                R.drawable.baseline_assignment_late_24,
                PendingFragment::class
            )
        )
        viewPagerAdapter.addFragment(
            MainPagerAdapter.FragmentItem(
                R.string.tasks_completed,
                R.drawable.baseline_assignment_turned_in_24,
                CompletedFragment::class
            )
        )
        viewPagerAdapter.addFragment(
            MainPagerAdapter.FragmentItem(
                R.string.news,
                R.drawable.baseline_assignment_24,
                NewsFragment::class
            )
        )
    }

    private fun setupQualityOfLifeImprovements() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onDestroy() {
        taskDeletionServiceHelper.deactivateTaskDeletionService()
        super.onDestroy()
    }
}