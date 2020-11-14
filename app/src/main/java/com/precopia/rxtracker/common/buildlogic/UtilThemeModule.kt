package com.precopia.rxtracker.common.buildlogic

import android.app.Application
import com.precopia.rxtracker.util.IUtilThemeContract
import com.precopia.rxtracker.util.UtilTheme
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UtilThemeModule {
    @Provides
    @Singleton
    fun utilTheme(application: Application): IUtilThemeContract {
        return UtilTheme(application)
    }
}
