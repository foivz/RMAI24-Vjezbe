package hr.foi.rmai.memento

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import hr.foi.rmai.memento.fragments.MementoSettingsFragment

class PreferencesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_settings, MementoSettingsFragment())
            .commit()
    }
}