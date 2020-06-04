package com.precopia.rxtracker.common.buildlogic

import android.app.Application
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import org.jetbrains.anko.defaultSharedPreferences
import javax.inject.Singleton

@Module
class SharedPrefsModule {
    @Provides
    @Singleton
    fun sharedPrefs(application: Application): SharedPreferences {
        return application.defaultSharedPreferences
    }
}