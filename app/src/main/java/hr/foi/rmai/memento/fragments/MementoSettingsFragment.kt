package hr.foi.rmai.memento.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import hr.foi.rmai.memento.R

class MementoSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_preferences, rootKey)
    }
}
