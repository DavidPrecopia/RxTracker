package com.precopia.data.dao

import androidx.room.Dao
import com.precopia.data.datamodel.DbTimeStamp
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface TimeStampDao {
    fun getAll(): Flowable<List<DbTimeStamp>>

    fun add(timeStamp: DbTimeStamp): Completable

    fun delete(id: Int): Completable
}