package com.precopia.data.repository

import com.precopia.data.dao.PrescriptionDao
import com.precopia.data.datamodel.DbPrescription
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class PrescriptionRepoTest {


    private val dao = mockk<PrescriptionDao>()

    private val prescriptionPositions = mockk<PrescriptionPositions>(relaxed = true)


    private val repo = PrescriptionRepo(dao, prescriptionPositions)


    @BeforeEach
    fun setUp() {
        clearAllMocks()
    }


    @Nested
    inner class GetAll {
        /**
         * - Verify that an RxJava 3 Flowable is returned of.
         * - It will complete in this test.
         */
        @Test
        fun `getAll - success`() {
            val dbPrescription = DbPrescription(0, "title")

            every { dao.getAll() } returns io.reactivex.Flowable.just(listOf(dbPrescription))

            repo.getAll()
                    .test()
                    .assertValue {
                        it[0].id == dbPrescription.id && it[0].title == dbPrescription.title
                    }
        }

        /**
         * - Verify that an RxJava 3 Flowable is returned.
         * - It will error in this test.
         */
        @Test
        fun `getAll - failure`() {
            val throwable = mockk<Throwable>(relaxed = true)

            every { dao.getAll() } returns io.reactivex.Flowable.error(throwable)

            repo.getAll()
                    .test()
                    .assertError(throwable)
        }
    }


    @Nested
    inner class Add {
        /**
         * - Verify that an RxJava 3 Completable is returned.
         * - It will complete in this test.
         */
        @Test
        fun `add - complete`() {
            val dbPrescription = DbPrescription(0, "title", 1.0)

            every { dao.add(dbPrescription) } returns io.reactivex.Completable.complete()

            repo.add(dbPrescription.title, dbPrescription.position.toInt())
                    .test()
                    .assertComplete()
        }

        /**
         * - Verify that an RxJava 3 Completable is returned.
         * - It will error in this test.
         */
        @Test
        fun `add - error`() {
            val dbPrescription = DbPrescription(0, "title", 1.0)
            val throwable = mockk<Throwable>(relaxed = true)

            every { dao.add(dbPrescription) } returns io.reactivex.Completable.error(throwable)

            repo.add(dbPrescription.title, dbPrescription.position.toInt())
                    .test()
                    .assertError(throwable)
        }
    }


    @Nested
    inner class Delete {
        /**
         * - Verify that an RxJava 3 Completable is returned.
         * - It will complete in this test.
         */
        @Test
        fun `delete - complete`() {
            val id = 1

            every { dao.delete(id) } returns io.reactivex.Completable.complete()

            repo.delete(id)
                    .test()
                    .assertComplete()
        }

        /**
         * - Verify that an RxJava 3 Completable is returned.
         * - It will error in this test.
         */
        @Test
        fun `delete - error`() {
            val id = 1
            val throwable = mockk<Throwable>(relaxed = true)

            every { dao.delete(id) } returns io.reactivex.Completable.error(throwable)

            repo.delete(id)
                    .test()
                    .assertError(throwable)
        }

    }


    @Nested
    inner class UpdatePosition {
        /**
         * - Verify that an RxJava 3 Completable is returned.
         * - It will complete in this test.
         */
        @Test
        fun `updatePosition - complete`() {
            val id = 100
            val oldPosition = 1
            val newPosition = 2

            every {
                prescriptionPositions.update(id, oldPosition, newPosition)
            } returns io.reactivex.Completable.complete()

            repo.updatePosition(id, oldPosition, newPosition)
                    .test()
                    .assertComplete()
        }

        /**
         * - Verify that an RxJava 3 Completable is returned.
         * - It will error in this test.
         */
        @Test
        fun `updatePosition - error`() {
            val id = 100
            val oldPosition = 1
            val newPosition = 2
            val throwable = mockk<Throwable>(relaxed = true)

            every {
                prescriptionPositions.update(id, oldPosition, newPosition)
            } returns io.reactivex.Completable.error(throwable)

            repo.updatePosition(id, oldPosition, newPosition)
                    .test()
                    .assertError(throwable)
        }
    }
}