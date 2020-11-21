package com.precopia.rxtracker.common.buildlogic

import android.app.Application
import android.content.SharedPreferences
import com.precopia.domain.repository.IPrescriptionRepoContract
import com.precopia.domain.repository.ITimeStampRepoContract
import com.precopia.rxtracker.util.IUtilThemeContract
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    RepositoryModule::class,
    UtilThemeModule::class,
    SharedPrefsModule::class
])
interface AppComponent {
    fun timeStampRepo(): ITimeStampRepoContract

    fun prescriptionRepo(): IPrescriptionRepoContract

    fun utilTheme(): IUtilThemeContract

    fun sharedPrefs(): SharedPreferences

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun application(application: Application): Builder
    }
}