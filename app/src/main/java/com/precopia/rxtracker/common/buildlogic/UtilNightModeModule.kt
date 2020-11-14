package com.precopia.rxtracker.common.buildlogic

import android.app.Application
import com.precopia.rxtracker.util.IUtilNightModeContract
import com.precopia.rxtracker.util.UtilNightMode
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UtilNightModeModule {
    @Provides
    @Singleton
    fun utilNightMode(application: Application): IUtilNightModeContract {
        return UtilNightMode(application)
    }
}
