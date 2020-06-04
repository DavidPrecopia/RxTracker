package com.precopia.rxtracker.view.editdateview

import com.precopia.domain.repository.ITimeStampRepoContract
import com.precopia.rxtracker.UtilSchedulerProviderMockInit
import com.precopia.rxtracker.util.IUtilParseDateTime
import com.precopia.rxtracker.util.IUtilSchedulerProviderContract
import com.precopia.rxtracker.view.editdateview.IEditDateContract.LogicEvents
import io.mockk.CapturingSlot
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

internal class EditDateLogicTest {


    private val repo = mockk<ITimeStampRepoContract>(relaxed = true)

    private val schedulerProvider = mockk<IUtilSchedulerProviderContract>()

    private val disposable = spyk<CompositeDisposable>()

    private val utilParseDateTime = mockk<IUtilParseDateTime>()


    private val logic = EditDateLogic(
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
         * - Pass the ID, current dateTime, month, day, and year via [LogicEvents.UpdateDate].
         * - Verify that the arguments passed to [ITimeStampRepoContract.modifyDateTime]
         * have not been modified - the ID is unmodified and the [Calendar] instance
         * has the same time and date.
         * - The repo will return [Completable.complete].
         */
        @Test
        fun `onEvent - update date - success`() {
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
                utilParseDateTime.parsedTime(dateTime)
            } returns listOf(hour.toInt(), minute.toInt())
            every {
                repo.modifyDateTime(
                        id = capture(captureId),
                        calendar = capture(captureCalendar)
                )
            } returns Completable.complete()

            logic.onEvent(LogicEvents.UpdateDate(id, dateTime, month.toInt(), day.toInt(), year.toInt()))

            Assertions.assertThat(captureId.captured).isEqualTo(id)
            Assertions.assertThat(captureCalendar.captured[Calendar.MONTH]).isEqualTo(month.toInt())
            Assertions.assertThat(captureCalendar.captured[Calendar.DATE]).isEqualTo(day.toInt())
            Assertions.assertThat(captureCalendar.captured[Calendar.YEAR]).isEqualTo(year.toInt())
            Assertions.assertThat(captureCalendar.captured[Calendar.MINUTE]).isEqualTo(minute.toInt())
            Assertions.assertThat(captureCalendar.captured[Calendar.HOUR_OF_DAY]).isEqualTo(hour.toInt())
        }

        /**
         * - Pass the ID, current dateTime, month, day, and year via [LogicEvents.UpdateDate].
         * - The repo will return [Completable.error].
         * - Verify that the returned Exception is thrown.
         */
        @Test
        fun `onEvent - update date - error`() {
            val throwable = mockk<Throwable>(relaxed = true)
            val id = 1
            val month = "01"
            val day = "01"
            val year = "2020"
            val hour = "01"
            val minute = "01"
            val dateTime = "$month/$day/$year $hour:$minute"

            every {
                utilParseDateTime.parsedTime(dateTime)
            } returns listOf(hour.toInt(), minute.toInt())
            every { repo.modifyDateTime(any(), any()) } returns Completable.error(throwable)

            logic.onEvent(LogicEvents.UpdateDate(id, dateTime, month.toInt(), day.toInt(), year.toInt()))

            verify(atLeast = 1) { throwable.printStackTrace() }
        }
    }

}