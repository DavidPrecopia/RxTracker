package com.precopia.data.database

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.precopia.data.datamodel.DbTimeStamp
import com.precopia.data.datamodel.DbTimeStampDelete
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
        val yearInt = 2020

        dao.clearDatabase()

        dao.add(DbTimeStamp(title = titleString, time = timeString, year = yearInt))
                .test()
                .assertComplete()

        dao.getAll()
                .test()
                .assertValue {
                    it.size == 1
                            && it[0].title == titleString
                            && it[0].time == timeString
                            && it[0].year == yearInt
                }
    }

    /**
     * - Clear the database.
     * - Add two [DbTimeStamp] with different dates and years.
     * - Retrieve it and verify that they are unmodified and in the correct order.
     */
    @Test
    fun addAndGetVerifyOrder() {
        val titleString = "title"
        val timeOneString = "1/25/2021"
        val yearOneInt = 2021
        val timeTwoString = "12/25/2020"
        val yearTwoInt = 2020

        dao.clearDatabase()

        dao.add(DbTimeStamp(title = titleString, time = timeOneString, year = yearOneInt))
                .test()
                .assertComplete()

        dao.add(DbTimeStamp(title = titleString, time = timeTwoString, year = yearTwoInt))
                .test()
                .assertComplete()

        dao.getAll()
                .test()
                .assertValue {
                    it.size == 2
                            && it[0].time == timeOneString
                            && it[0].year == yearOneInt
                            && it[1].time == timeTwoString
                            && it[1].year == yearTwoInt
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
        val yearInt = 2020

        dao.clearDatabase()

        dao.add(DbTimeStamp(title = titleString, time = timeString, year = yearInt))
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
     * - Add two [DbTimeStamp].
     * - Retrieve both of them to get their IDs.
     * - Delete both of them.
     * - Get all and verify the returned list is empty.
     */
    @Test
    fun deleteAll() {
        dao.clearDatabase()

        dao.add(DbTimeStamp(title = "titleOne", time = "timeOne", year = 2021))
                .test()
                .assertComplete()

        dao.add(DbTimeStamp(title = "titleTwo", time = "timeTwo", year = 2020))
                .test()
                .assertComplete()

        val values = dao.getAll()
                .test()
                .values()

        val insertedIdOne = values[0][0].id
        val insertedIdTwo = values[0][1].id

        dao.deleteAll(listOf(
                DbTimeStampDelete(insertedIdOne),
                DbTimeStampDelete(insertedIdTwo)
        ))
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
        val year = 2020

        dao.clearDatabase()

        dao.add(DbTimeStamp(title = title, time = timeOriginal, year = year))
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