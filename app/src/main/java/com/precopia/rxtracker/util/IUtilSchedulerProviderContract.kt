package com.precopia.rxtracker.util

import io.reactivex.rxjava3.core.Scheduler

interface IUtilSchedulerProviderContract {
    fun io(): Scheduler

    fun ui(): Scheduler
}
