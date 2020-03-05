package com.precopia.rxtracker.common

import android.app.Application
import android.os.Looper
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

internal abstract class RxTrackerApplicationBase : Application() {

    override fun onCreate() {
        super.onCreate()
        initRxAndroidScheduler()
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
}