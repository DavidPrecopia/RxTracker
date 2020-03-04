package com.precopia.data.database

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.precopia.data.datamodel.DbPrescription
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class PrescriptionDaoTest {

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

    private val dao = database.prescriptionDao()

    private val titleString = "title"
    private val prescription = DbPrescription(title = titleString)


    @After
    fun tearDown() {
        database.close()
    }


    /**
     * - Clear the database.
     * - Add a [DbPrescription].
     * - Verify that it was successful added.
     * - Retrieve it.
     * - Verify that it is unmodified.
     */
    @Test
    fun addAndGet() {
        dao.deleteAll()

        dao.add(prescription)
            .test()
            .assertComplete()

        dao.getAll()
            .test()
            .assertValue { it.size == 1 && it[0].title == titleString }
    }

    /**
     * - Clear the database.
     * - An empty database should return an empty List.
     */
    @Test
    fun emptyDatabaseReturnsNothing() {
        dao.deleteAll()

        dao.getAll()
            .test()
            .assertValue { it.isEmpty() }
    }
}