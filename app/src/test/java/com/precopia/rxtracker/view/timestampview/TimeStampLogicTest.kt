package com.precopia.rxtracker.view.timestampview

import androidx.lifecycle.Observer
import com.precopia.domain.datamodel.TimeStamp
import com.precopia.domain.repository.ITimeStampRepoContract
import com.precopia.rxtracker.InstantExecutorExtension
import com.precopia.rxtracker.UtilSchedulerProviderMockInit
import com.precopia.rxtracker.observeForTesting
import com.precopia.rxtracker.util.IUtilSchedulerProviderContract
import com.precopia.rxtracker.view.common.ERROR_EMPTY_LIST
import com.precopia.rxtracker.view.common.ERROR_GENERIC
import com.precopia.rxtracker.view.timestampview.ITimeStampViewContract.LogicEvents
import com.precopia.rxtracker.view.timestampview.ITimeStampViewContract.ViewEvents
import io.mockk.Called
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.rxjava3.core.Flowable
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

    private val schedulerProvider = mockk<IUtilSchedulerProviderContract>()

    private val disposable = spyk<CompositeDisposable>()


    private lateinit var logic: TimeStampLogic


    /**
     * I am re-instantiating the class under test before each test
     * to ensure that the observable returned by [ITimeStampViewContract.Logic.observe]
     * is cleared before the following test.
     */
    @BeforeEach
    fun init() {
        logic = TimeStampLogic(repo, schedulerProvider, disposable)
        clearAllMocks()
        UtilSchedulerProviderMockInit.init(schedulerProvider)
    }


    @Nested
    inner class OnStart {
        /**
         * - Send event [ViewEvents.DisplayLoading].
         * - Get all from the Repo - it will contain data in this test.
         * - Send the data returned by the Repo to the View via [ViewEvents.DisplayList].
         */
        @Test
        fun `onStart - normal`() {
            val listTimeStamp = listOf(TimeStamp(0, "title", "time"))
            val listLiveDataOutput = mutableListOf<ViewEvents>()
            val liveDataObserver = Observer<ViewEvents> { listLiveDataOutput.add(it) }

            every { repo.getAll() } returns Flowable.just(listTimeStamp)

            logic.observe().observeForever(liveDataObserver)

            logic.onEvent(LogicEvents.OnStart)

            verify(exactly = 1) { repo.getAll() }
            assertThat(listLiveDataOutput.size).isEqualTo(2)
            assertThat(listLiveDataOutput[0]).isEqualTo(ViewEvents.DisplayLoading)
            assertThat(listLiveDataOutput[1]).isEqualTo(ViewEvents.DisplayList(listTimeStamp))

            logic.observe().removeObserver(liveDataObserver)
        }

        /**
         * - Send event [ViewEvents.DisplayLoading].
         * - Get all from the Repo - it will be empty in this test.
         * - Send [ERROR_EMPTY_LIST] to the View via [ViewEvents.DisplayError].
         * - Verify that [ViewEvents.DisplayList] was not sent.
         */
        @Test
        fun `onStart - empty list`() {
            val emptyList = emptyList<TimeStamp>()
            val listLiveDataOutput = mutableListOf<ViewEvents>()
            val liveDataObserver = Observer<ViewEvents> { listLiveDataOutput.add(it) }

            every { repo.getAll() } returns Flowable.just(emptyList)

            logic.observe().observeForever(liveDataObserver)

            logic.onEvent(LogicEvents.OnStart)

            verify(exactly = 1) { repo.getAll() }
            assertThat(listLiveDataOutput.size).isEqualTo(2)
            assertThat(listLiveDataOutput[0]).isEqualTo(ViewEvents.DisplayLoading)
            assertThat(listLiveDataOutput[1]).isEqualTo(ViewEvents.DisplayError(ERROR_EMPTY_LIST))

            logic.observe().removeObserver(liveDataObserver)
        }

        /**
         * - Send event [ViewEvents.DisplayLoading].
         * - Get all from the Repo - an exception will be returned.
         * - Send [ERROR_GENERIC] to the View via [ViewEvents.DisplayError].
         * - Throw the exception.
         * - Verify that [ViewEvents.DisplayList] was not sent.
         */
        @Test
        fun `onStart - error`() {
            val throwable = mockk<Throwable>(relaxed = true)
            val listLiveDataOutput = mutableListOf<ViewEvents>()
            val liveDataObserver = Observer<ViewEvents> { listLiveDataOutput.add(it) }

            every { repo.getAll() } returns Flowable.error(throwable)

            logic.observe().observeForever(liveDataObserver)

            logic.onEvent(LogicEvents.OnStart)

            verify(exactly = 1) { repo.getAll() }
            verify(atLeast = 1) { throwable.printStackTrace() }
            assertThat(listLiveDataOutput.size).isEqualTo(2)
            assertThat(listLiveDataOutput[0]).isEqualTo(ViewEvents.DisplayLoading)
            assertThat(listLiveDataOutput[1]).isEqualTo(ViewEvents.DisplayError(ERROR_GENERIC))

            logic.observe().removeObserver(liveDataObserver)
        }
    }

    @Nested
    inner class OpenPrescriptionView {
        /**
         * - Send the [ViewEvents.OpenPrescriptionView] event to the View.
         */
        @Test
        fun openPrescriptionViewEvent() {
            logic.onEvent(LogicEvents.OpenAddPrescriptionView)

            logic.observe().observeForTesting {
                assertThat(logic.observe().value).isEqualTo(ViewEvents.OpenPrescriptionView)
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

            logic.observe().observeForTesting {
                assertThat(logic.observe().value).isEqualTo(ViewEvents.OpenAddTimeStampView)
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

            logic.observe().observeForTesting {
                assertThat(logic.observe().value).isEqualTo(ViewEvents.DeleteItem(validPosition))
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

            logic.observe().observeForTesting {
                assertThat(logic.observe().value).isEqualTo(null)
            }
            verify { repo wasNot Called }
        }
    }
}