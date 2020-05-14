package com.precopia.domain.repository

import com.precopia.domain.datamodel.Prescription
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface IPrescriptionRepoContract {
    fun getAll(): Flowable<List<Prescription>>

    fun add(rxTitle: String): Completable

    fun updatePosition(id: Int, oldPosition: Int, newPosition: Int): Completable
}