package com.precopia.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.precopia.data.dao.PrescriptionDao
import com.precopia.data.dao.TimeStampDao
import com.precopia.data.database.DatabaseConstants.DATABASE_NAME
import com.precopia.data.datamodel.DbPrescription
import com.precopia.data.datamodel.DbTimeStamp

@Database(entities = [DbTimeStamp::class, DbPrescription::class], version = 1, exportSchema = false)
internal abstract class RxTrackerDatabase: RoomDatabase() {
    companion object {
        private var database: RxTrackerDatabase? = null

        fun getInstance(application: Application): RxTrackerDatabase {
            if (database === null) {
                database = Room.databaseBuilder(
                        application,
                        RxTrackerDatabase::class.java,
                        DATABASE_NAME
                ).build()
            }
            return database !!
        }
    }

    abstract fun timeStampDao(): TimeStampDao

    abstract fun prescriptionDao(): PrescriptionDao
}