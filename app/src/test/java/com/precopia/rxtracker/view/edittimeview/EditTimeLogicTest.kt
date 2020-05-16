package com.precopia.rxtracker.view.edittimeview

import com.precopia.domain.repository.ITimeStampRepoContract
import com.precopia.rxtracker.UtilSchedulerProviderMockInit
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


    private val logic = EditTimeLogic(repo, schedulerProvider, disposable)


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
         * have not been modified.
         *   - The ID is unmodified and the [Calendar] instance has the same hour and minute.
         * - The repo will return [Completable.complete].
         */
        @Test
        fun `onEvent - update time - success`() {
            val captureId = CapturingSlot<Int>()
            val captureCalendar = CapturingSlot<Calendar>()
            val id = 1
            val hour = 1
            val minute = 1

            every {
                repo.modifyTime(
                        id = capture(captureId),
                        calendar = capture(captureCalendar)
                )
            } returns Completable.complete()

            logic.onEvent(LogicEvents.UpdateTime(id, hour, minute))

            assertThat(captureId.captured).isEqualTo(id)
            assertThat(captureCalendar.captured[Calendar.MINUTE]).isEqualTo(minute)
            assertThat(captureCalendar.captured[Calendar.HOUR_OF_DAY]).isEqualTo(hour)
        }

        /**
         * - Pass the ID, hour, and minute via [LogicEvents.UpdateTime].
         * - The repo will return [Completable.error].
         * - Verify that the returned Exception is thrown.
         */
        @Test
        fun `onEvent - update time - error`() {
            val id = 1
            val hour = 1
            val minute = 1
            val throwable = mockk<Throwable>(relaxed = true)

            every { repo.modifyTime(any(), any()) } returns Completable.error(throwable)

            logic.onEvent(LogicEvents.UpdateTime(id, hour, minute))

            verify(atLeast = 1) { throwable.printStackTrace() }
        }
    }
}