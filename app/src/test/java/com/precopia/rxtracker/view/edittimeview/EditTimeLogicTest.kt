package com.precopia.rxtracker.view.edittimeview

import com.precopia.domain.repository.ITimeStampRepoContract
import com.precopia.rxtracker.UtilSchedulerProviderMockInit
import com.precopia.rxtracker.util.IUtilParseDateTime
import com.precopia.rxtracker.util.IUtilSchedulerProviderContract
import com.precopia.rxtracker.view.edittimeview.IEditTimeContract.LogicEvents
import io.mockk.CapturingSlot
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

internal class EditTimeLogicTest {


    private val repo = mockk<ITimeStampRepoContract>(relaxed = true)

    private val schedulerProvider = mockk<IUtilSchedulerProviderContract>()

    private val disposable = spyk<CompositeDisposable>()

    private val utilParseDateTime = mockk<IUtilParseDateTime>()


    private val logic = EditTimeLogic(
            repo, schedulerProvider, disposable, utilParseDateTime
    )


    @BeforeEach
    fun init() {
        clearAllMocks()
        UtilSchedulerProviderMockInit.init(schedulerProvider)
    }


    @Nested
    inner class UpdateTime {
        /**
         * - Pass the ID, hour, and minute via [LogicEvents.UpdateTime].
         * - Verify that the arguments passed to [ITimeStampRepoContract.modifyTime]
         * have not been modified - the ID is unmodified and the [Calendar] instance
         * has the same hour and minute.
         * - The repo will return [Completable.complete].
         */
        @Test
        fun `onEvent - update time - success`() {
            val captureId = CapturingSlot<Int>()
            val captureCalendar = CapturingSlot<Calendar>()
            val id = 1
            val month = "01"
            val day = "01"
            val year = "2020"
            val hour = "12"
            val minute = "00"
            val dateTime = "$month/$day/$year $hour:$minute"

            every {
                utilParseDateTime.parsedDate(dateTime)
            } returns listOf(month.toInt(), day.toInt(), year.toInt())
            every {
                repo.modifyTime(
                        id = capture(captureId),
                        calendar = capture(captureCalendar)
                )
            } returns Completable.complete()

            logic.onEvent(LogicEvents.UpdateTime(id, dateTime, hour.toInt(), minute.toInt()))

            assertThat(captureId.captured).isEqualTo(id)
            // Decrementing month by 1 due to how Calendar store months.
            assertThat(captureCalendar.captured[Calendar.MONTH]).isEqualTo(month.toInt() - 1)
            assertThat(captureCalendar.captured[Calendar.DATE]).isEqualTo(day.toInt())
            assertThat(captureCalendar.captured[Calendar.YEAR]).isEqualTo(year.toInt())
            assertThat(captureCalendar.captured[Calendar.MINUTE]).isEqualTo(minute.toInt())
            assertThat(captureCalendar.captured[Calendar.HOUR_OF_DAY]).isEqualTo(hour.toInt())
        }

        /**
         * - Pass the ID, hour, and minute via [LogicEvents.UpdateTime].
         * - The repo will return [Completable.error].
         * - Verify that the returned Exception is thrown.
         */
        @Test
        fun `onEvent - update time - error`() {
            val throwable = mockk<Throwable>(relaxed = true)
            val id = 1
            val month = "01"
            val day = "01"
            val year = "2020"
            val hour = "01"
            val minute = "01"
            val dateTime = "$month/$day/$year $hour:$minute"

            every {
                utilParseDateTime.parsedDate(dateTime)
            } returns listOf(month.toInt(), day.toInt(), year.toInt())
            every { repo.modifyTime(any(), any()) } returns Completable.error(throwable)

            logic.onEvent(LogicEvents.UpdateTime(id, dateTime, hour.toInt(), minute.toInt()))

            verify(atLeast = 1) { throwable.printStackTrace() }
        }
    }
}