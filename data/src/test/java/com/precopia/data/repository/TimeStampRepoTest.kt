package com.precopia.data.repository

import com.precopia.data.dao.TimeStampDao
import com.precopia.data.datamodel.DbTimeStamp
import com.precopia.data.util.ICurrentTimeUtil
import com.precopia.domain.datamodel.TimeStamp
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

    private val timeUtil = mockk<ICurrentTimeUtil>(relaxUnitFun = true)


    private val repo = TimeStampRepo(dao, timeUtil)


    @BeforeEach
    fun init() {
        clearAllMocks()
    }


    @Nested
    inner class GetAll {
        /**
         * - Verify that an RxJava 3 Flowable is returned of type [TimeStamp].
         * - It will be successful in this test.
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
         * - Verify that an RxJava 3 Flowable is returned of type [TimeStamp].
         * - It will return an Throwable in this test.
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
            every { timeUtil.currentTime() } returns dbTimeStamp.time

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
            every { timeUtil.currentTime() } returns dbTimeStamp.time

            repo.add(dbTimeStamp.title)
                    .test()
                    .assertError(throwable)
        }
    }


    @Nested
    inner class Delete {
        /**
         * - Accept the prescription's title as a parameter.
         * - Acquire the current date and time.
         * - Create a new instance of [DbTimeStamp].
         * - Pass the instance to the Dao.
         */
        @Test
        fun delete() {
            val capturedArg = CapturingSlot<DbTimeStamp>()
            val rxTitle = "title"
            val currentTimeString = "time"

            every { timeUtil.currentTime() } returns currentTimeString
            every { dao.add(timeStamp = capture(capturedArg)) } answers {
                io.reactivex.Completable.complete()
            }

            repo.add(rxTitle)

            verify { dao.add(capturedArg.captured) }
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
            every { dao.modifyTime(id, timeString) } returns io.reactivex.Completable.complete()

            repo.modifyTime(id, calendar)
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
            every { dao.modifyTime(id, timeString) } returns io.reactivex.Completable.error(throwable)

            repo.modifyTime(id, calendar)
                    .test()
                    .assertError(throwable)
        }
    }
}