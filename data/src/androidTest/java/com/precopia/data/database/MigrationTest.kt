package com.precopia.data.database

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.precopia.data.database.DatabaseConstants.PRESCRIPTION_ID_COLUMN
import com.precopia.data.database.DatabaseConstants.PRESCRIPTION_POSITION_COLUMN
import com.precopia.data.database.DatabaseConstants.PRESCRIPTION_TABLE_NAME
import com.precopia.data.database.DatabaseConstants.PRESCRIPTION_TITLE_COLUMN
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
internal class MigrationTest {
    private val testDatabase = "migration-test"

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
            InstrumentationRegistry.getInstrumentation(),
            RxTrackerDatabase::class.java.canonicalName,
            FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun migrate1To2() {
        helper.createDatabase(testDatabase, 1).apply {
            // The db has schema version 1. Insert some data using raw SQL queries.
            execSQL("INSERT INTO $PRESCRIPTION_TABLE_NAME " +
                    "($PRESCRIPTION_ID_COLUMN, $PRESCRIPTION_TITLE_COLUMN) " +
                    "VALUES (1, \"Title\")")

            close()
        }

        // Re-open the database with version 2 and provide the migration process.
        val db = helper.runMigrationsAndValidate(testDatabase, 2, true, RxTrackerDatabase.MIGRATION_1_2)

        // Validating that the data was properly migrated.
        db.query("SELECT * FROM prescriptions ORDER BY $PRESCRIPTION_POSITION_COLUMN ASC").also {
            it.moveToFirst()
            assertThat(it.count).isEqualTo(1)
            assertThat(it.getInt(0)).isEqualTo(1)
            assertThat(it.getString(1)).isEqualTo("Title")
            assertThat(it.getDouble(2)).isEqualTo(0.0)
            it.close()
        }
        db.close()
    }
}