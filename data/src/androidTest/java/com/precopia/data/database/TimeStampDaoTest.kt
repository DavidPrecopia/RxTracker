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


    private val titleString = "title"
    private val timeString = "time"
    private val timeStamp = DbTimeStamp(title = titleString, time = timeString)


    @After
    fun tearDown() {
        database.close()
    }


    /**
     * - Clear the database.
     * - Add a [DbTimeStamp].
     * - Verify that is was successfully added.
     * - Retrieve it.
     * - Verify that it is unmodified.
     */
    @Test
    fun addAndGet() {
        dao.deleteAll()

        dao.add(timeStamp)
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
     * - Verify that is was successfully added.
     * - Retrieve it to get its ID.
     * - Delete it.
     * - Verify that it was successfully deleted.
     * - Get all and verify that it is not in the returned list.
     */
    @Test
    fun delete() {
        dao.deleteAll()

        dao.add(timeStamp)
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
}