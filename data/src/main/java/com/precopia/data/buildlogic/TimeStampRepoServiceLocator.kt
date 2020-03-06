package com.precopia.data.buildlogic

import android.app.Application
import com.precopia.data.database.RxTrackerDatabase
import com.precopia.data.repository.TimeStampRepo
import com.precopia.data.util.CurrentTimeUtil

class TimeStampRepoServiceLocator(application: Application) {

    private val timeStampRepo = TimeStampRepo(
        dao(application),
        timeUtil()
    )


    fun timeStampRepo() = timeStampRepo


    private fun dao(application: Application) =
        RxTrackerDatabase.getInstance(application).timeStampDao()

    private fun timeUtil() = CurrentTimeUtil()
}