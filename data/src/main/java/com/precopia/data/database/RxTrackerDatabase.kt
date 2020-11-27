package com.precopia.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.precopia.data.dao.PrescriptionDao
import com.precopia.data.dao.TimeStampDao
import com.precopia.data.database.DatabaseConstants.DATABASE_NAME
import com.precopia.data.database.DatabaseConstants.PRESCRIPTION_POSITION_COLUMN
import com.precopia.data.database.DatabaseConstants.PRESCRIPTION_TABLE_NAME
import com.precopia.data.database.DatabaseConstants.TIME_STAMP_TABLE_NAME
import com.precopia.data.database.DatabaseConstants.TIME_STAMP_YEAR_COLUMN
import com.precopia.data.datamodel.DbPrescription
import com.precopia.data.datamodel.DbTimeStamp

@Database(
        entities = [DbTimeStamp::class, DbPrescription::class],
        version = 3,
        exportSchema = true
)
internal abstract class RxTrackerDatabase: RoomDatabase() {
    companion object {
        private var database: RxTrackerDatabase? = null

        val MIGRATION_1_2 = object: Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE $PRESCRIPTION_TABLE_NAME " +
                        "ADD COLUMN $PRESCRIPTION_POSITION_COLUMN REAL NOT NULL DEFAULT 0.0")
            }
        }

        val MIGRATION_2_3 = object: Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE $TIME_STAMP_TABLE_NAME " +
                        "ADD COLUMN $TIME_STAMP_YEAR_COLUMN INTEGER NOT NULL DEFAULT 2020")
            }
        }

        fun getInstance(application: Application): RxTrackerDatabase {
            if (database === null) {
                database = Room.databaseBuilder(
                        application,
                        RxTrackerDatabase::class.java,
                        DATABASE_NAME
                )
                        .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                        .build()
            }
            return database!!
        }
    }

    abstract fun timeStampDao(): TimeStampDao

    abstract fun prescriptionDao(): PrescriptionDao
}