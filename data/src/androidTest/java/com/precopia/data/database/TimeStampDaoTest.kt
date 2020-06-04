package com.precopia.data.database

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.precopia.data.datamodel.DbTimeStamp
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class TimeStampDaoTest {

    /**
     * This ensures that Room executes operations immediately.
     */
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val database = Room.inMemoryDatabaseBuilder(
                    ApplicationProvider.getApplicationContext<Application>(),
                    RxTrackerDatabase::class.java
            )
            .allowMainThreadQueries()
            .build()

    private val dao = database.timeStampDao()


    @After
    fun tearDown() {
        database.close()
    }


    /**
     * - Clear the database.
     * - Add a [DbTimeStamp].
     * - Retrieve it and verify that it is unmodified.
     */
    @Test
    fun addAndGet() {
        val titleString = "title"
        val timeString = "time"

        dao.deleteAll()

        dao.add(DbTimeStamp(title = titleString, time = timeString))
                .test()
                .assertComplete()

        dao.getAll()
                .test()
                .assertValue {
                    it.size == 1
                            && it[0].title == titleString
                            && it[0].time == timeString
                }
    }

    /**
     * - Clear the database.
     * - Add a [DbTimeStamp].
     * - Retrieve it to get its ID.
     * - Delete it.
     * - Get all and verify that it is not in the returned list.
     */
    @Test
    fun delete() {
        val titleString = "title"
        val timeString = "time"

        dao.deleteAll()

        dao.add(DbTimeStamp(title = titleString, time = timeString))
                .test()
                .assertComplete()

        val insertedId = dao.getAll()
                .test()
                .values()[0][0].id

        dao.delete(insertedId)
                .test()
                .assertComplete()

        dao.getAll()
                .test()
                .assertValue { it.isEmpty() }
    }

    /**
     * - Clear the database.
     * - Add a [DbTimeStamp].
     * - Retrieve it to get its ID.
     * - Modify its time with its ID.
     * - Retrieve it and verify the change was made.
     */
    @Test
    fun modifyTime() {
        val title = "title"
        val timeOriginal = "time"
        val timeModified = "timeModified"

        dao.deleteAll()

        dao.add(DbTimeStamp(title = title, time = timeOriginal))
                .test()
                .assertComplete()

        val insertedId = dao.getAll()
                .test()
                .values()[0][0].id

        dao.modifyDateTime(insertedId, timeModified)
                .test()
                .assertComplete()

        dao.getAll()
                .test()
                .assertValue { it[0].time == timeModified }
    }
}