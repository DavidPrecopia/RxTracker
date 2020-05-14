package com.precopia.data.repository

import com.precopia.data.dao.PrescriptionDao
import com.precopia.data.datamodel.DbPrescription
import com.precopia.domain.datamodel.Prescription
import com.precopia.domain.repository.IPrescriptionRepoContract
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

internal class PrescriptionRepo(private val dao: PrescriptionDao,
                                private val prescriptionPositions: IPrescriptionPositions): IPrescriptionRepoContract {

    override fun getAll(): Flowable<List<Prescription>> =
            RxJavaBridge.toV3Flowable(dao.getAll())
                    .map { mapDbPrescription(it) }

    override fun add(rxTitle: String): Completable =
            RxJavaBridge.toV3Completable(dao.add(DbPrescription(title = rxTitle)))

    override fun updatePosition(id: Int, oldPosition: Int, newPosition: Int): Completable =
            RxJavaBridge.toV3Completable(prescriptionPositions.update(id, oldPosition, newPosition))


    private fun mapDbPrescription(list: List<DbPrescription>) = list.map {
        Prescription(it.id, it.title, it.position.toInt())
    }
}