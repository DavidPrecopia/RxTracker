package com.precopia.domain.repository

import com.precopia.domain.datamodel.TimeStamp
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import java.util.*

interface ITimeStampRepoContract {
    fun getAll(): Flowable<List<TimeStamp>>

    fun add(rxTitle: String): Completable

    fun delete(id: Int): Completable

    fun modifyDateTime(id: Int, calendar: Calendar): Completable
}
