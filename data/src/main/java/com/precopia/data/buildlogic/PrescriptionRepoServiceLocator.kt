package com.precopia.data.buildlogic

import android.app.Application
import com.precopia.data.database.RxTrackerDatabase
import com.precopia.data.repository.PrescriptionRepo

class PrescriptionRepoServiceLocator(application: Application) {

    private val prescriptionRepo = PrescriptionRepo(
        RxTrackerDatabase.getInstance(application).prescriptionDao()
    )

    fun prescriptionRepo() = prescriptionRepo
}