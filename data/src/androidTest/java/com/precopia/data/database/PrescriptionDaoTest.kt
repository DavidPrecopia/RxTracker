package com.precopia.data.database

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.precopia.data.datamodel.DbPrescription
import org.assertj.core.api.Assertions.assertThat
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


    @After
    fun tearDown() {
        database.close()
    }


    /**
     * - Clear the database.
     * - Add a [DbPrescription].
     * - Retrieve it.
     * - Verify that it is unmodified.
     */
    @Test
    fun addAndGet() {
        val titleString = "title"
        val prescription = DbPrescription(title = titleString)

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
     * - Add a [DbPrescription].
     * - Retrieve it to get its ID.
     * - Delete it with its ID.
     * - Get all and verify the returned List is empty.
     */
    @Test
    fun delete() {
        val title = "title"
        val position = 0.0

        dao.deleteAll()

        dao.add(DbPrescription(title = title, position = position))
                .test()
                .assertComplete()

        val insertedId = dao.getAllSynchronously()[0].id

        dao.delete(insertedId)
                .test()
                .assertComplete()

        assertThat(dao.getAllSynchronously().isEmpty()).isTrue()
    }

    /**
     * - Clear the database.
     * - Add a [DbPrescription].
     * - Retrieve it to gets its ID.
     * - Update is position and verify the change was made.
     */
    @Test
    fun updatePositionAndGetAll() {
        val positionOriginal = 0.0
        val positionModified = 1.0
        val prescription = DbPrescription(title = "title", position = positionOriginal)

        dao.deleteAll()

        dao.add(prescription)
                .test()
                .assertComplete()

        val insertedId = dao.getAll()
                .test()
                .values()[0][0].id

        assertThat(
                dao.updatePositionAndGetAll(insertedId, positionModified)[0].position
        ).isEqualTo(
                positionModified
        )
    }

    /**
     * - Clear the database.
     * - Add a [DbPrescription].
     * - Retrieve it to get its ID.
     * - Updated its position with its ID.
     * - Retrieve the inserted [DbPrescription] and verify the change was made.
     */
    @Test
    fun updatePosition() {
        val positionOriginal = 0.0
        val positionModified = 1.0
        val prescription = DbPrescription(title = "title", position = positionOriginal)

        dao.deleteAll()

        dao.add(prescription)
                .test()
                .assertComplete()

        val insertedId = dao.getAll()
                .test()
                .values()[0][0].id

        dao.updatePosition(insertedId, positionModified)

        dao.getAll()
                .test()
                .assertValue { it[0].position == positionModified }
    }

    /**
     * - Clear the database.
     * - Add a [DbPrescription].
     * - Retrieve it and verify that it is unmodified.
     */
    @Test
    fun getAllSynchronously() {
        val title = "title"
        val position = 0.0
        val prescription = DbPrescription(title = title, position = position)

        dao.deleteAll()

        dao.add(prescription)
                .test()
                .assertComplete()

        dao.getAll()
                .test()
                .assertValue { it[0].title == title && it[0].position == position }
    }

    /**
     * - Clear the database.
     * - Add two [DbPrescription].
     * - Retrieve both and update both of their positions.
     * - Retrieve both are verify that the changes were made.
     */
    @Test
    fun updateAllPositions() {
        val prescriptionOne = DbPrescription(title = "titleOne", position = 0.0)
        val positionOneModified = prescriptionOne.position + 1
        val prescriptionTwo = DbPrescription(title = "titleTwo", position = 1.0)
        val positionTwoModified = prescriptionTwo.position + 1

        dao.deleteAll()

        dao.add(prescriptionOne)
                .test()
                .assertComplete()

        dao.add(prescriptionTwo)
                .test()
                .assertComplete()

        val insertedPrescriptionsList = dao.getAllSynchronously()

        insertedPrescriptionsList[0].position = positionOneModified
        insertedPrescriptionsList[1].position = positionTwoModified

        dao.updateAllPositions(insertedPrescriptionsList)
                .test()
                .assertComplete()

        val postModificationList = dao.getAllSynchronously()
        assertThat(postModificationList[0].position).isEqualTo(positionOneModified)
        assertThat(postModificationList[1].position).isEqualTo(positionTwoModified)
    }


    /**
     * - Clear the database.
     * - Verify that an empty List is returned from the database.
     */
    @Test
    fun emptyDatabaseReturnsNothing() {
        dao.deleteAll()

        dao.getAll()
                .test()
                .assertValue { it.isEmpty() }
    }
}