package com.precopia.rxtracker.common

import android.app.Application
import android.os.Looper
import com.precopia.rxtracker.common.buildlogic.AppComponent
import com.precopia.rxtracker.common.buildlogic.DaggerAppComponent
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

internal abstract class RxTrackerApplicationBase: Application() {


    lateinit var appComponent: AppComponent
        private set


    override fun onCreate() {
        super.onCreate()
        initAppComponent()
        initRxAndroidScheduler()
        setTheme()
    }

    private fun initAppComponent() {
        appComponent = DaggerAppComponent
                .builder()
                .application(this)
                .build()
    }

    /**
     * This means events flowing to the main thread do not
     * have to wait for vsync, decreasing the likelihood of frame drops.
     * https://twitter.com/jakewharton/status/1170437658776133636?s=12
     */
    private fun initRxAndroidScheduler() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler {
            AndroidSchedulers.from(Looper.getMainLooper(), true)
        }
    }


    private fun setTheme() {
        appComponent.utilTheme().setFollowSystem()
    }
}
