package com.precopia.data.dao

import androidx.room.Dao
import com.precopia.data.datamodel.DbPrescription
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface PrescriptionDao {
    fun getAll(): Flowable<List<DbPrescription>>

    fun add(rxTitle: String): Completable
}