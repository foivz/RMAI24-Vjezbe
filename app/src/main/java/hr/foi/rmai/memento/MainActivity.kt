package hr.foi.rmai.memento

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import hr.foi.rmai.memento.adapters.MainPagerAdapter
import hr.foi.rmai.memento.fragments.CompletedFragment
import hr.foi.rmai.memento.fragments.NewsFragment
import hr.foi.rmai.memento.fragments.PendingFragment

class MainActivity : AppCompatActivity() {
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupQualityOfLifeImprovements()

        setupTabNavigation()
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
}