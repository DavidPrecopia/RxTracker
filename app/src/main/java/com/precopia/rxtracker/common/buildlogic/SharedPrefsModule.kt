package com.precopia.rxtracker.common.buildlogic

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SharedPrefsModule {
    @Provides
    @Singleton
    fun sharedPrefs(application: Application): SharedPreferences {
        return application.getSharedPreferences(
                application.packageName + "_preferences",
                MODE_PRIVATE
        )
    }
}