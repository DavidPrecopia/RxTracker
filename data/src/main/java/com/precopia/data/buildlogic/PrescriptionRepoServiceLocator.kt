package com.precopia.data.buildlogic

import android.app.Application
import com.precopia.data.dao.PrescriptionDao
import com.precopia.data.database.RxTrackerDatabase
import com.precopia.data.repository.IPrescriptionPositions
import com.precopia.data.repository.PrescriptionPositions
import com.precopia.data.repository.PrescriptionRepo
import com.precopia.domain.repository.IPrescriptionRepoContract

class PrescriptionRepoServiceLocator(application: Application) {

    private val prescriptionRepo: IPrescriptionRepoContract = PrescriptionRepo(
            dao(application),
            prescriptionPositions(application)
    )

    private fun dao(application: Application): PrescriptionDao =
            RxTrackerDatabase.getInstance(application).prescriptionDao()

    private fun prescriptionPositions(application: Application): IPrescriptionPositions =
            PrescriptionPositions(dao(application))


    fun prescriptionRepo() = prescriptionRepo
}