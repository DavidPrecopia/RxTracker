package com.precopia.rxtracker.util

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

class UtilNightMode(private val application: Application) : IUtilNightModeContract {

    override fun isNightModeEnabled(): Boolean = when (getMode()) {
        Configuration.UI_MODE_NIGHT_YES -> true
        else -> false
    }

    private fun getMode() = application.resources?.configuration?.uiMode
            ?.and(Configuration.UI_MODE_NIGHT_MASK)


    override fun setFollowSystem() {
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        )
    }
}
