package com.precopia.rxtracker.view.timelistview

import com.precopia.domain.repository.ITimeStampRepoContract
import com.precopia.rxtracker.SchedulerProviderMockInit
import com.precopia.rxtracker.util.ISchedulerProviderContract
import com.precopia.rxtracker.view.timelistview.ITimeStampListViewContract.LogicEvents
import com.precopia.rxtracker.view.timelistview.ITimeStampListViewContract.ViewEvents
import io.mockk.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class TimeStampLogicTest {

    private val repo = mockk<ITimeStampRepoContract>()

    private val view = mockk<ITimeStampListViewContract.View>(relaxUnitFun = true)

    private val schedulerProvider = mockk<ISchedulerProviderContract>()

    private val disposable = spyk<CompositeDisposable>()


    private val logic = TimeStampLogic(view, repo, schedulerProvider, disposable)


    @BeforeEach
    fun init() {
        clearAllMocks()
        SchedulerProviderMockInit.init(schedulerProvider)
    }


    @Nested
    inner class OpenPrescriptionView {
        /**
         * - Pass [ViewEvents.OpenPrescriptionView] to the View.
         */
        @Test
        fun openPrescriptionViewEvent() {
            logic.onEvent(LogicEvents.OpenAddPrescriptionView)

            verify(exactly = 1) { view.onEvent(ViewEvents.OpenPrescriptionView) }
        }
    }

    @Nested
    inner class OpenAddTimeStampView {
        /**
         * - Pass [ViewEvents.OpenAddTimeStampView] to the View.
         */
        @Test
        fun openAddTimeStampView() {
            logic.onEvent(LogicEvents.OpenAddTimeStampView)

            verify(exactly = 1) { view.onEvent(ViewEvents.OpenAddTimeStampView) }
        }

    }

    @Nested
    inner class DeleteItem {
        /**
         * - Pass the function the position of the list item.
         *   - In this test it will be 0 or greater, thus valid.
         * - The position is passed to the View via [ViewEvents.DeleteItem].
         */
        @Test
        fun `deleteItem - valid number`() {
            val validPosition = 0

            logic.onEvent(LogicEvents.DeleteItem(validPosition))

            verify(exactly = 1) { view.onEvent(ViewEvents.DeleteItem(validPosition)) }
        }

        /**
         * - Pass the function the position of the list item.
         *   - In this test it will be less than 0, thus invalid.
         * - An Exception will be thrown.
         * - Verify that the View was not called.
         */
        @Test
        fun `deleteItem - invalid number`() {
            val invalidPosition = -1

            assertThrows<Exception> {
                logic.onEvent(LogicEvents.DeleteItem(invalidPosition))
            }

            verify { view wasNot Called }
        }
    }
}