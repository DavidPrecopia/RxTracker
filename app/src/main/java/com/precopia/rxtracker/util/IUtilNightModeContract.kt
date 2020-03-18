package com.precopia.rxtracker.util

interface IUtilNightModeContract {
    val nightModeEnabled: Boolean

    fun setDay()

    fun setNight()
}
