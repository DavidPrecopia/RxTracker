package com.precopia.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.precopia.data.database.DatabaseConstants.PRESCRIPTION_ID_COLUMN
import com.precopia.data.database.DatabaseConstants.PRESCRIPTION_POSITION_COLUMN
import com.precopia.data.datamodel.DbPrescription
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
internal interface PrescriptionDao {
    @Query("SELECT * FROM prescriptions ORDER BY $PRESCRIPTION_POSITION_COLUMN ASC")
    fun getAll(): Flowable<List<DbPrescription>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(prescription: DbPrescription): Completable

    @Query("DELETE FROM prescriptions WHERE $PRESCRIPTION_ID_COLUMN = :id")
    fun delete(id: Int): Completable


    @Transaction
    fun updatePositionAndGetAll(id: Int, newPosition: Double): MutableList<DbPrescription> {
        updatePosition(id, newPosition)
        return getAllSynchronously()
    }

    @Query("UPDATE prescriptions SET $PRESCRIPTION_POSITION_COLUMN = :newPos WHERE $PRESCRIPTION_ID_COLUMN = :id")
    fun updatePosition(id: Int, newPos: Double)

    @Query("SELECT * FROM prescriptions ORDER BY $PRESCRIPTION_POSITION_COLUMN ASC")
    fun getAllSynchronously(): MutableList<DbPrescription>


    @Update
    fun updateAllPositions(list: List<DbPrescription>): Completable


    /**
     * EXCLUSIVELY FOR TESTING
     */
    @Query("DELETE FROM prescriptions")
    fun deleteAll()
}