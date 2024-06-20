package com.bangkit.financeguard.view.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.bangkit.financeguard.R
import java.util.Locale


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        findPreference<ListPreference>(getString(R.string.pref_key_dark))?.setOnPreferenceChangeListener { _, newValue ->
            val selectedMode = NightMode.valueOf(newValue.toString().uppercase(Locale.US))
            updateTheme(selectedMode.value)
            true
        }

    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }
}