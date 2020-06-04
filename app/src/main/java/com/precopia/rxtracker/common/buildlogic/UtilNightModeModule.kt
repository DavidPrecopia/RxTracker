package com.precopia.rxtracker.common.buildlogic

import android.app.Application
import android.content.SharedPreferences
import com.precopia.rxtracker.R
import com.precopia.rxtracker.util.IUtilNightModeContract
import com.precopia.rxtracker.util.UtilNightMode
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UtilNightModeModule {
    @Provides
    @Singleton
    fun utilNightMode(sharedPrefs: SharedPreferences, application: Application): IUtilNightModeContract {
        return UtilNightMode(
                sharedPrefs,
                application.getString(R.string.night_mode_shared_pref_key)
        )
    }
}
