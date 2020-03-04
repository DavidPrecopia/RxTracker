package com.precopia.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.precopia.data.database.DatabaseConstants.PRESCRIPTION_TITLE_COLUMN
import com.precopia.data.datamodel.DbPrescription
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface PrescriptionDao {
    @Query("SELECT * FROM prescriptions ORDER BY $PRESCRIPTION_TITLE_COLUMN ASC")
    fun getAll(): Flowable<List<DbPrescription>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(prescription: DbPrescription): Completable


    /**
     * TESTING PURPOSES ONLY
     */
    @Query("DELETE FROM prescriptions")
    fun deleteAll()
}