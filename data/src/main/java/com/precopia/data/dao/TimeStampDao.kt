package com.precopia.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.precopia.data.database.DatabaseConstants.TIME_STAMP_DATE_COLUMN
import com.precopia.data.database.DatabaseConstants.TIME_STAMP_ID_COLUMN
import com.precopia.data.database.DatabaseConstants.TIME_STAMP_TABLE_NAME
import com.precopia.data.database.DatabaseConstants.TIME_STAMP_YEAR_COLUMN
import com.precopia.data.datamodel.DbTimeStamp
import com.precopia.data.datamodel.DbTimeStampDelete
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
internal interface TimeStampDao {
    @Query("SELECT * FROM $TIME_STAMP_TABLE_NAME ORDER BY $TIME_STAMP_YEAR_COLUMN DESC, $TIME_STAMP_DATE_COLUMN DESC")
    fun getAll(): Flowable<List<DbTimeStamp>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(timeStamp: DbTimeStamp): Completable

    @Query("DELETE FROM $TIME_STAMP_TABLE_NAME WHERE $TIME_STAMP_ID_COLUMN = :id")
    fun delete(id: Int): Completable

    @Delete(entity = DbTimeStamp::class)
    fun deleteAll(ids: List<DbTimeStampDelete>): Completable

    @Query("UPDATE $TIME_STAMP_TABLE_NAME SET $TIME_STAMP_DATE_COLUMN = :time WHERE $TIME_STAMP_ID_COLUMN = :id")
    fun modifyDateTime(id: Int, time: String): Completable


    /**
     * EXCLUSIVELY FOR TESTING
     */
    @Query("DELETE FROM $TIME_STAMP_TABLE_NAME")
    fun clearDatabase()
}