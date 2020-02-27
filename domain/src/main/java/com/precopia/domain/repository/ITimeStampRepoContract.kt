package com.precopia.domain.repository

import com.precopia.domain.datamodel.TimeStamp
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

interface ITimeStampRepoContract {
    fun getAll(): Flowable<List<TimeStamp>>

    fun add(rxTitle: String): Completable

    fun delete(id: Int): Completable
}
