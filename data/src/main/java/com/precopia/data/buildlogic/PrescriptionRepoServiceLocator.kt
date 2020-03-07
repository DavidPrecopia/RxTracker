package com.precopia.data.buildlogic

import android.app.Application
import com.precopia.data.database.RxTrackerDatabase
import com.precopia.data.repository.PrescriptionRepo
import com.precopia.domain.repository.IPrescriptionRepoContract

class PrescriptionRepoServiceLocator(application: Application) {

    private val prescriptionRepo: IPrescriptionRepoContract = PrescriptionRepo(
        RxTrackerDatabase.getInstance(application).prescriptionDao()
    )

    fun prescriptionRepo() = prescriptionRepo
}