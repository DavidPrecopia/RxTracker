package com.precopia.rxtracker.view.manageprescriptionsview

import androidx.lifecycle.Observer
import com.precopia.domain.datamodel.Prescription
import com.precopia.domain.repository.IPrescriptionRepoContract
import com.precopia.rxtracker.InstantExecutorExtension
import com.precopia.rxtracker.UtilSchedulerProviderMockInit
import com.precopia.rxtracker.observeForTesting
import com.precopia.rxtracker.util.IUtilSchedulerProviderContract
import com.precopia.rxtracker.view.common.ERROR_EMPTY_LIST
import com.precopia.rxtracker.view.common.ERROR_GENERIC
import com.precopia.rxtracker.view.common.ERROR_OPERATION_FAILED
import com.precopia.rxtracker.view.common.ERROR_TITLE
import com.precopia.rxtracker.view.common.MSG_SUCCESSFULLY_SAVE
import com.precopia.rxtracker.view.manageprescriptionsview.IManagePrescriptionsContact.LogicEvents
import com.precopia.rxtracker.view.manageprescriptionsview.IManagePrescriptionsContact.ViewEvents
import io.mockk.Called
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(value = [InstantExecutorExtension::class])
internal class ManagePrescriptionsLogicTest {

    private val repo = mockk<IPrescriptionRepoContract>()

    private val schedulerProvider = mockk<IUtilSchedulerProviderContract>()

    private val disposable = spyk<CompositeDisposable>()


    private lateinit var logic: ManagePrescriptionsLogic


    /**
     * Re-instantiating the class under test before each test to ensure the LiveData
     * being observed is cleared.
     */
    @BeforeEach
    fun setUp() {
        logic = ManagePrescriptionsLogic(repo, schedulerProvider, disposable)
        clearAllMocks()
        UtilSchedulerProviderMockInit.init(schedulerProvider)
    }


    @Nested
    inner class OnStart {
        /**
         * - Send [ViewEvents.DisplayLoading].
         * - Get all from the Repo - in this test a List of data will be returned.
         * - Sent [ViewEvents.DisplayList] to the View.
         */
        @Test
        fun `onStart - normal`() {
            val listPrescription = listOf(Prescription(0, "title", 0), Prescription(1, "titleOne", 0))
            val listLiveDataOutput = mutableListOf<ViewEvents>()
            val liveDataObserver = Observer<ViewEvents> { listLiveDataOutput.add(it) }

            every { repo.getAll() } returns Flowable.just(listPrescription)

            logic.observe().observeForever(liveDataObserver)

            logic.onEvent(LogicEvents.OnStart)

            verify(exactly = 1) { repo.getAll() }
            assertThat(listLiveDataOutput.size).isEqualTo(2)
            assertThat(listLiveDataOutput[0]).isEqualTo(ViewEvents.DisplayLoading)
            assertThat(listLiveDataOutput[1]).isEqualTo(ViewEvents.DisplayList(listPrescription))

            logic.observe().removeObserver(liveDataObserver)
        }

        /**
         * - Send [ViewEvents.DisplayLoading].
         * - Get all from the Repo - in this test an empty List will be returned.
         * - Sent [ViewEvents.DisplayError] with [ERROR_EMPTY_LIST] to the View.
         */
        @Test
        fun `onStart - empty list`() {
            val listPrescription = emptyList<Prescription>()
            val listLiveDataOutput = mutableListOf<ViewEvents>()
            val liveDataObserver = Observer<ViewEvents> { listLiveDataOutput.add(it) }

            every { repo.getAll() } returns Flowable.just(listPrescription)

            logic.observe().observeForever(liveDataObserver)

            logic.onEvent(LogicEvents.OnStart)

            verify(exactly = 1) { repo.getAll() }
            assertThat(listLiveDataOutput.size).isEqualTo(2)
            assertThat(listLiveDataOutput[0]).isEqualTo(ViewEvents.DisplayLoading)
            assertThat(listLiveDataOutput[1]).isEqualTo(ViewEvents.DisplayError(ERROR_EMPTY_LIST))

            logic.observe().removeObserver(liveDataObserver)
        }

        /**
         * - Send [ViewEvents.DisplayLoading].
         * - Get all from the Repo - in this test the repo will return an error.
         * - Throw the Exception returned by the repo.
         * - Sent [ViewEvents.DisplayError] with [ERROR_GENERIC] to the View.
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
    inner class Save {
        /**
         * - Save to the repo - verify the title is unchanged.
         * - It will complete in this test.
         * - Send [ViewEvents.DisplayMessage] to the View with [MSG_SUCCESSFULLY_SAVE].
         *
         * - BE MINDFUL OF: the position sent to [IPrescriptionRepoContract.add]
         * will always be 0 because the class get the position from its internal list,
         * which is only initialized, not populated with data.
         */
        @Test
        fun `save - successful`() {
            val title = "title"
            val position = 0

            every { repo.add(title, position) } returns Completable.complete()

            logic.onEvent(LogicEvents.Save(title))

            verify(exactly = 1) { repo.add(title, position) }
            logic.observe().observeForTesting {
                assertThat(logic.observe().value).isEqualTo(ViewEvents.DisplayMessage(MSG_SUCCESSFULLY_SAVE))
            }
        }

        /**
         * - Save to the repo - verify the title is unchanged.
         * - It will error in this test.
         * - Throw the Exception returned by the repo.
         * - Send [ViewEvents.DisplayMessage] to the View with [ERROR_OPERATION_FAILED].
         *
         * - BE MINDFUL OF: the position sent to [IPrescriptionRepoContract.add]
         * will always be 0 because the class get the position from its internal list,
         * which is only initialized, not populated with data.
         */
        @Test
        fun `save - failure`() {
            val throwable = mockk<Throwable>(relaxed = true)
            val title = "title"
            val position = 0

            every { repo.add(title, position) } returns Completable.error(throwable)

            logic.onEvent(LogicEvents.Save(title))

            verify(exactly = 1) { repo.add(title, position) }
            verify(atLeast = 1) { throwable.printStackTrace() }
            logic.observe().observeForTesting {
                assertThat(logic.observe().value).isEqualTo(ViewEvents.DisplayMessage(ERROR_OPERATION_FAILED))
            }
        }

        /**
         * - Pass an empty title
         * - Send [ViewEvents.DisplayMessage] to the View with [ERROR_TITLE].
         * - Verify the Repo was not called.
         */
        @Test
        fun `save - empty title`() {
            val emptyTitle = ""

            logic.onEvent(LogicEvents.Save(emptyTitle))

            verify { repo wasNot Called }
            logic.observe().observeForTesting {
                assertThat(logic.observe().value).isEqualTo(ViewEvents.DisplayMessage(ERROR_TITLE))
            }
        }
    }


    @Nested
    inner class Dragging {
        /**
         * - Prepare the class via [LogicEvents.OnStart] - load [ManagePrescriptionsLogic]'s
         * internal List with data.
         * - Send [ViewEvents.Dragging] to the View with both of the passed-in positions.
         */
        @Test
        fun dragging() {
            val oldPosition = 0
            val list = mutableListOf(
                    Prescription(1, "Title1", 1),
                    Prescription(0, "Title0", oldPosition)
            )
            val newPosition = list.size - 1

            every { repo.getAll() } returns Flowable.just(list)
            logic.onEvent(LogicEvents.OnStart)

            logic.onEvent(LogicEvents.Dragging(oldPosition, newPosition))

            logic.observe().observeForTesting {
                assertThat(logic.observe().value)
                        .isEqualTo(ViewEvents.Dragging(oldPosition, newPosition))
            }
        }
    }


    @Nested
    inner class PermanentlyMoved {
        /**
         * - Prepare the class via [LogicEvents.OnStart] - load [ManagePrescriptionsLogic]'s
         * internal List with data.
         * - Send the ID, old position, and new position to the Repo, verify they were
         * sent unmodified.
         * - In this test, the Repo will return [Completable.complete].
         */
        @Test
        fun `permanentlyMoved - success`() {
            val id = 100
            val oldPosition = 0
            val list = mutableListOf(
                    Prescription(1, "Title1", 1),
                    Prescription(id, "Title0", oldPosition)
            )
            val newPosition = list.size - 1
            val throwable = mockk<Throwable>(relaxed = true)

            every {
                repo.updatePosition(id, oldPosition, newPosition)
            } returns Completable.error(throwable)
            every { repo.getAll() } returns Flowable.just(list)

            logic.onEvent(LogicEvents.OnStart)

            logic.onEvent(LogicEvents.PermanentlyMoved(newPosition))

            verify(exactly = 1) { repo.updatePosition(id, oldPosition, newPosition) }
        }


        /**
         * - Prepare the class via [LogicEvents.OnStart] - load [ManagePrescriptionsLogic]'s
         * internal List with data.
         * - Send the ID, old position, and new position to the Repo.
         * - In this test, the Repo will return [Completable.error], verify it was thrown.
         */
        @Test
        fun `permanentlyMoved - failure`() {
            val id = 100
            val oldPosition = 0
            val list = mutableListOf(
                    Prescription(1, "Title1", 1),
                    Prescription(id, "Title0", oldPosition)
            )
            val newPosition = list.size - 1
            val throwable = mockk<Throwable>(relaxed = true)

            every {
                repo.updatePosition(id, oldPosition, newPosition)
            } returns Completable.error(throwable)

            every { repo.getAll() } returns Flowable.just(list)
            logic.onEvent(LogicEvents.OnStart)

            logic.onEvent(LogicEvents.PermanentlyMoved(newPosition))

            verify(atLeast = 1) { throwable.printStackTrace() }
        }
    }


    @Nested
    inner class DeleteItem {
        /**
         * - Send [ViewEvents.DeleteItem] to the View with the passed-in position.
         * - Send the passed-in ID to the Repo.
         * - It will complete in this test.
         */
        @Test
        fun `delete - success`() {
            val id = 0
            val position = 1

            every { repo.delete(id) } returns Completable.complete()

            logic.onEvent(LogicEvents.DeleteItem(id, position))

            logic.observe().observeForTesting {
                assertThat(logic.observe().value).isEqualTo(ViewEvents.DeleteItem(position))
            }
            verify(exactly = 1) { repo.delete(id) }
        }

        /**
         * - Pass an invalid position.
         * - Exception is thrown.
         * - Verify that the Repo was not called.
         */
        @Test
        fun `delete - invalid position`() {
            val id = 0
            val invalidPosition = -1

            assertThrows<Exception> {
                logic.onEvent(LogicEvents.DeleteItem(id, invalidPosition))
            }

            verify { repo wasNot Called }
        }

        /**
         * - Send [ViewEvents.DeleteItem] to the View with the passed-in position.
         * - Send the passed-in ID to the Repo.
         * - In this test the Repo will return a Throwable.
         * - Verify that the Throwable was thrown.
         */
        @Test
        fun `delete - error`() {
            val id = 0
            val position = 1
            val throwable = mockk<Throwable>()

            every { repo.delete(id) } returns Completable.error(throwable)

            logic.onEvent(LogicEvents.DeleteItem(id, position))

            logic.observe().observeForTesting {
                assertThat(logic.observe().value).isEqualTo(ViewEvents.DeleteItem(position))
            }
            verify(exactly = 1) { repo.delete(id) }
            verify(atLeast = 1) { throwable.printStackTrace() }
        }
    }
}