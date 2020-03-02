package com.precopia.data.repository

import com.precopia.data.dao.PrescriptionDao
import com.precopia.data.datamodel.DbPrescription
import com.precopia.domain.datamodel.Prescription
import com.precopia.domain.repository.IPrescriptionRepoContract
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

class PrescriptionRepo(private val dao: PrescriptionDao): IPrescriptionRepoContract {

    override fun getAll(): Flowable<List<Prescription>> = dao.getAll().map { mapDbPrescription(it) }

    override fun add(rxTitle: String): Completable = dao.add(rxTitle)


    private fun mapDbPrescription(list: List<DbPrescription>) = list.map {
        Prescription(it.id, it.title)
    }
}