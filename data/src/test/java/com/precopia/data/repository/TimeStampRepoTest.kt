package com.precopia.data.repository

import com.precopia.data.dao.TimeStampDao
import com.precopia.data.datamodel.DbTimeStamp
import com.precopia.data.datamodel.DbTimeStampDelete
import com.precopia.data.util.ITimeUtil
import io.mockk.CapturingSlot
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

internal class TimeStampRepoTest {


    private val dao = mockk<TimeStampDao>(relaxUnitFun = true)

    private val timeUtil = mockk<ITimeUtil>(relaxUnitFun = true)


    private val repo = TimeStampRepo(dao, timeUtil)


    @BeforeEach
    fun init() {
        clearAllMocks()
    }


    @Nested
    inner class GetAll {
        /**
         * - Verify that an RxJava 3 Flowable is returned.
         * - It will complete in this test.
         */
        @Test
        fun `getAll - success`() {
            val dbTimeStamp = DbTimeStamp(0, "title", "time")

            every { dao.getAll() } returns io.reactivex.Flowable.just(listOf(dbTimeStamp))

            repo.getAll()
                    .test()
                    .assertValue {
                        it[0].id == dbTimeStamp.id && it[0].title == dbTimeStamp.title
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
            val dbTimeStamp = DbTimeStamp(0, "title", "time")

            every { dao.add(dbTimeStamp) } returns io.reactivex.Completable.complete()
            every { timeUtil.getCurrentTime() } returns dbTimeStamp.time

            repo.add(dbTimeStamp.title)
                    .test()
                    .assertComplete()
        }

        /**
         * - Verify that an RxJava 3 Completable is returned.
         * - It will error in this test.
         */
        @Test
        fun `add - error`() {
            val dbTimeStamp = DbTimeStamp(0, "title", "time")
            val throwable = mockk<Throwable>(relaxed = true)

            every { dao.add(dbTimeStamp) } returns io.reactivex.Completable.error(throwable)
            every { timeUtil.getCurrentTime() } returns dbTimeStamp.time

            repo.add(dbTimeStamp.title)
                    .test()
                    .assertError(throwable)
        }
    }


    @Nested
    inner class Delete {
        /**
         * - Acquire the current date and time.
         * - Create a new instance of [DbTimeStamp] with passed-in title
         * and said date and time.
         * - Pass the instance to the Dao.
         */
        @Test
        fun delete() {
            val capturedArg = CapturingSlot<DbTimeStamp>()
            val rxTitle = "title"
            val currentTimeString = "time"

            every { timeUtil.getCurrentTime() } returns currentTimeString
            every { dao.add(timeStamp = capture(capturedArg)) } answers {
                io.reactivex.Completable.complete()
            }

            repo.add(rxTitle)

            verify { dao.add(capturedArg.captured) }
        }
    }


    @Nested
    inner class DeleteAll {
        /**
         * - Verify that an RxJava 3 Completable is returned.
         * - It will complete in this test.
         */
        @Test
        fun `deleteAll - complete`() {
            val idOne = 1
            val idTwo = 2
            val listOfIds = listOf(idOne, idTwo)
            val listOfTimeStampDelete = listOfIds.map { DbTimeStampDelete(it) }

            every { dao.deleteAll(listOfTimeStampDelete) } answers {
                io.reactivex.Completable.complete()
            }

            repo.deleteAll(listOfIds)
                    .test()
                    .assertComplete()
        }

        /**
         * - Verify that an RxJava 3 Error is returned.
         * - It will error in this test.
         */
        @Test
        fun `deleteAll - error`() {
            val idOne = 1
            val idTwo = 2
            val listOfIds = listOf(idOne, idTwo)
            val listOfTimeStampDelete = listOfIds.map { DbTimeStampDelete(it) }
            val throwable = mockk<Throwable>(relaxed = true)

            every { dao.deleteAll(listOfTimeStampDelete) } answers {
                io.reactivex.Completable.error(throwable)
            }

            repo.deleteAll(listOfIds)
                    .test()
                    .assertError(throwable)
        }
    }


    @Nested
    inner class ModifyTime {
        /**
         * - Verify that an RxJava 3 Completable is returned.
         * - It will complete in this test.
         */
        @Test
        fun `modifyTime - success`() {
            val id = 1
            val calendar = mockk<Calendar>()
            val timeString = "current time"

            every { timeUtil.calendarToString(calendar) } returns timeString
            every { dao.modifyDateTime(id, timeString) } returns io.reactivex.Completable.complete()

            repo.modifyDateTime(id, calendar)
                    .test()
                    .assertComplete()
        }

        /**
         * - Verify that an RxJava 3 Completable is returned.
         * - It will error in this test.
         */
        @Test
        fun `modifyTime - failure`() {
            val id = 1
            val calendar = mockk<Calendar>()
            val timeString = "current time"
            val throwable = mockk<Throwable>(relaxed = true)

            every { timeUtil.calendarToString(calendar) } returns timeString
            every { dao.modifyDateTime(id, timeString) } returns io.reactivex.Completable.error(throwable)

            repo.modifyDateTime(id, calendar)
                    .test()
                    .assertError(throwable)
        }
    }
}