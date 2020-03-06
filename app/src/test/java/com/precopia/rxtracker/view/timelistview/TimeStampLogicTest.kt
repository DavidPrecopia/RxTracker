package com.precopia.rxtracker.view.timelistview

import com.precopia.domain.repository.ITimeStampRepoContract
import com.precopia.rxtracker.InstantExecutorExtension
import com.precopia.rxtracker.SchedulerProviderMockInit
import com.precopia.rxtracker.util.ISchedulerProviderContract
import com.precopia.rxtracker.view.timelistview.ITimeStampListViewContract.LogicEvents
import com.precopia.rxtracker.view.timelistview.ITimeStampListViewContract.ViewEvents
import io.mockk.*
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(value = [InstantExecutorExtension::class])
internal class TimeStampLogicTest {

    private val repo = mockk<ITimeStampRepoContract>(relaxed = true)

    private val schedulerProvider = mockk<ISchedulerProviderContract>()

    private val disposable = spyk<CompositeDisposable>()


    private val logic = TimeStampLogic(repo, schedulerProvider, disposable)


    @BeforeEach
    fun init() {
        clearAllMocks()
        SchedulerProviderMockInit.init(schedulerProvider)
    }


    @Nested
    inner class OpenPrescriptionView {
        /**
         * - Send the [ViewEvents.OpenPrescriptionView] event to the View.
         */
        @Test
        fun openPrescriptionViewEvent() {
            logic.onEvent(LogicEvents.OpenAddPrescriptionView)

            logic.observe().observeForever {
                assertThat(it).isEqualTo(ViewEvents.OpenPrescriptionView)
            }
        }
    }

    @Nested
    inner class OpenAddTimeStampView {
        /**
         * - Send the [ViewEvents.OpenAddTimeStampView] event to the View.
         */
        @Test
        fun openAddTimeStampView() {
            logic.onEvent(LogicEvents.OpenAddTimeStampView)

            logic.observe().observeForever {
                assertThat(it).isEqualTo(ViewEvents.OpenAddTimeStampView)
            }
        }
    }

    @Nested
    inner class DeleteItem {
        /**
         * - Pass the function the position of the list item.
         *   - In this test it will be 0 or greater, thus valid.
         * -  Pass the position to the View via the [ViewEvents.DeleteItem] event.
         * - Pass the ID to the repo.
         */
        @Test
        fun `deleteItem - valid number`() {
            val id = 1
            val validPosition = 0

            logic.onEvent(LogicEvents.DeleteItem(id, validPosition))

            logic.observe().observeForever {
                assertThat(it).isEqualTo(ViewEvents.DeleteItem(validPosition))
            }
            verify(exactly = 1) { repo.delete(id) }
        }

        /**
         * - Pass the function the position of the list item.
         *   - In this test it will be less than 0, thus invalid.
         * - An Exception will be thrown.
         * - Verify no events were sent to the View and the Repo was not called.
         */
        @Test
        fun `deleteItem - invalid number`() {
            val id = 1
            val invalidPosition = -1

            assertThrows<Exception> {
                logic.onEvent(LogicEvents.DeleteItem(id, invalidPosition))
            }

            logic.observe().observeForever {
                assertThat(it).isEqualTo(null)
            }
            verify { repo wasNot Called }
        }
    }
}