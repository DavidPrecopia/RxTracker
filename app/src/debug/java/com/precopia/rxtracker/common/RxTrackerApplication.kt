package com.precopia.rxtracker.common

import timber.log.Timber

internal class RxTrackerApplication: RxTrackerApplicationBase() {
    override fun onCreate() {
        super.onCreate()
        initTimber()
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }
}