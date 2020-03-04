package com.precopia.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.precopia.data.database.DatabaseConstants.TIME_STAMP_DATE_COLUMN
import com.precopia.data.database.DatabaseConstants.TIME_STAMP_ID_COLUMN
import com.precopia.data.datamodel.DbTimeStamp
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface TimeStampDao {
    @Query("SELECT * FROM time_stamps ORDER BY $TIME_STAMP_DATE_COLUMN ASC")
    fun getAll(): Flowable<List<DbTimeStamp>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(timeStamp: DbTimeStamp): Completable

    @Query("DELETE FROM time_stamps WHERE $TIME_STAMP_ID_COLUMN = :id")
    fun delete(id: Int): Completable


    /**
     * TESTING PURPOSES ONLY
     */
    @Query("DELETE FROM time_stamps")
    fun deleteAll()
}