package com.bangkit.financeguard

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.bangkit.financeguard.view.settings.NightMode
import java.util.Locale

class FinanceGuard : Application() {

    override fun onCreate() {
        super.onCreate()

        val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        preferences.getString(
            getString(R.string.pref_key_dark),
            getString(R.string.pref_dark_auto)
        )?.apply {
            val mode = NightMode.valueOf(this.uppercase(Locale.US))
            AppCompatDelegate.setDefaultNightMode(mode.value)
        }
    }
}