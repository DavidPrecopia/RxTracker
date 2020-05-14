package com.precopia.data.repository

import com.precopia.data.dao.PrescriptionDao
import com.precopia.data.datamodel.DbPrescription
import io.mockk.Called
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class PrescriptionPositionsTest {

    private val dao = mockk<PrescriptionDao>(relaxUnitFun = true)

    private val prescriptionPositions = PrescriptionPositions(dao)


    @BeforeEach
    fun init() {
        clearAllMocks()
    }


    @Nested
    inner class Error {
        /**
         * - The old and the new positions are the same.
         * - Returns [Completable.complete].
         */
        @Test
        fun `update - both positions are the same`() {
            val id = 0
            val oldPosition = 1
            val newPosition = 1

            prescriptionPositions.update(id, oldPosition, newPosition)
                    .test()
                    .assertComplete()

            verify { dao wasNot Called }
        }

        /**
         * - The old and the new positions are the same.
         * - Returns [Completable.complete].
         */
        @ParameterizedTest
        @CsvSource("-1, 0", "0, -1")
        fun `update - invalid positions`(oldPosition: Int, newPosition: Int) {
            val id = 0

            prescriptionPositions.update(id, oldPosition, newPosition)
                    .test()
                    .assertError(Exception::class.java)

            verify { dao wasNot Called }
        }
    }

    /**
     * - The old position is less than new position.
     * - Pass the (newPosition + 0.5) to the DAO.
     * - Update the positions of all items so they are consecutive.
     * - Pass the update list of items back to the DAO.
     * - Verify all positions are consecutive.
     */
    @Test
    fun `update - old position is less than new position`() {
        val id = 100
        val oldPosition = 0
        val newPosition = 1
        val updatedTempPosition = (newPosition + 0.5)
        val prescriptionListTempPosition = mutableListOf(
                DbPrescription(200, "title1", 1.0),
                DbPrescription(id, "title0", updatedTempPosition),
                DbPrescription(300, "title2", 2.0)
        )
        val prescriptionListPostPositionUpdates = mutableListOf(
                DbPrescription(200, "title1", 0.0),
                DbPrescription(id, "title0", 1.0),
                DbPrescription(300, "title2", 2.0)
        )

        every { dao.updatePositionAndGetAll(id, updatedTempPosition) } returns prescriptionListTempPosition
        every { dao.updateAllPositions(prescriptionListPostPositionUpdates) } returns Completable.complete()

        prescriptionPositions.update(id, oldPosition, newPosition)
                .test()
                .assertComplete()

        verify(exactly = 1) { dao.updatePositionAndGetAll(id, updatedTempPosition) }
        verify(exactly = 1) { dao.updateAllPositions(prescriptionListPostPositionUpdates) }
    }

    /**
     * - The old position is less than new position.
     * - Start a transaction via the database.
     * - Pass the (newPosition - 0.5) to the DAO.
     * - Retrieve all items via the DAO.
     * - Update the positions of all items so they are consecutive.
     * - Pass the update list of items back to the DAO.
     * - Verify all positions are consecutive.
     */
    @Test
    fun `update - old position is greater than new position`() {
        val id = 200
        val oldPosition = 1
        val newPosition = 0
        val updatedTempPosition = (newPosition - 0.5)
        val prescriptionListTempPosition = mutableListOf(
                DbPrescription(id, "title1", updatedTempPosition),
                DbPrescription(100, "title0", 0.0),
                DbPrescription(300, "title2", 2.0)
        )
        val prescriptionListPostModification = mutableListOf(
                DbPrescription(id, "title1", 0.0),
                DbPrescription(100, "title0", 1.0),
                DbPrescription(300, "title2", 2.0)
        )

        every { dao.updatePositionAndGetAll(id, updatedTempPosition) } returns prescriptionListTempPosition
        every { dao.updateAllPositions(prescriptionListPostModification) } returns Completable.complete()

        prescriptionPositions.update(id, oldPosition, newPosition)
                .test()
                .assertComplete()

        verify(exactly = 1) { dao.updatePositionAndGetAll(id, updatedTempPosition) }
        verify(exactly = 1) { dao.updateAllPositions(prescriptionListPostModification) }
    }
}