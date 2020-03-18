package com.precopia.rxtracker.view.addprescriptionview

import androidx.lifecycle.Observer
import com.precopia.domain.datamodel.Prescription
import com.precopia.domain.repository.IPrescriptionRepoContract
import com.precopia.rxtracker.InstantExecutorExtension
import com.precopia.rxtracker.UtilSchedulerProviderMockInit
import com.precopia.rxtracker.observeForTesting
import com.precopia.rxtracker.util.IUtilSchedulerProviderContract
import com.precopia.rxtracker.view.addprescriptionview.IAddPrescriptionContact.Logic
import com.precopia.rxtracker.view.addprescriptionview.IAddPrescriptionContact.ViewEvents
import com.precopia.rxtracker.view.common.ERROR_EMPTY_LIST
import com.precopia.rxtracker.view.common.ERROR_GENERIC
import com.precopia.rxtracker.view.common.ERROR_OPERATION_FAILED
import com.precopia.rxtracker.view.common.MSG_SUCCESSFULLY_SAVE
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
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(value = [InstantExecutorExtension::class])
internal class AddPrescriptionLogicTest {

    private val repo = mockk<IPrescriptionRepoContract>()

    private val schedulerProvider = mockk<IUtilSchedulerProviderContract>()

    private val disposable = spyk<CompositeDisposable>()


    private lateinit var logic: AddPrescriptionLogic


    /**
     * I am re-instantiating the class under test before each test.
     * to ensure that the observable returned by [Logic.observe].
     * is cleared before the following test.
     */
    @BeforeEach
    fun setUp() {
        logic = AddPrescriptionLogic(repo, schedulerProvider, disposable)
        clearAllMocks()
        UtilSchedulerProviderMockInit.init(schedulerProvider)
    }


    @Nested
    inner class OnStart {
        /**
         * - Send event [ViewEvents.DisplayLoading].
         * - Get all from the Repo - in this test a List of data will be returned.
         * - Sent event [ViewEvents.DisplayList].
         */
        @Test
        fun `onStart - normal`() {
            val listPrescription = listOf(Prescription(0, "title"), Prescription(1, "titleOne"))
            val listLiveDataOutput = mutableListOf<ViewEvents>()
            val liveDataObserver = Observer<ViewEvents> { listLiveDataOutput.add(it) }

            every { repo.getAll() } returns Flowable.just(listPrescription)

            logic.observe().observeForever(liveDataObserver)

            logic.onEvent(IAddPrescriptionContact.LogicEvents.OnStart)

            verify(exactly = 1) { repo.getAll() }
            assertThat(listLiveDataOutput.size).isEqualTo(2)
            assertThat(listLiveDataOutput[0]).isEqualTo(ViewEvents.DisplayLoading)
            assertThat(listLiveDataOutput[1]).isEqualTo(ViewEvents.DisplayList(listPrescription))

            logic.observe().removeObserver(liveDataObserver)
        }

        /**
         * - Send event [ViewEvents.DisplayLoading].
         * - Get all from the Repo - in this test an empty List will be returned.
         * - Sent event [ViewEvents.DisplayError] with [ERROR_EMPTY_LIST].
         */
        @Test
        fun `onStart - empty list`() {
            val listPrescription = emptyList<Prescription>()
            val listLiveDataOutput = mutableListOf<ViewEvents>()
            val liveDataObserver = Observer<ViewEvents> { listLiveDataOutput.add(it) }

            every { repo.getAll() } returns Flowable.just(listPrescription)

            logic.observe().observeForever(liveDataObserver)

            logic.onEvent(IAddPrescriptionContact.LogicEvents.OnStart)

            verify(exactly = 1) { repo.getAll() }
            assertThat(listLiveDataOutput.size).isEqualTo(2)
            assertThat(listLiveDataOutput[0]).isEqualTo(ViewEvents.DisplayLoading)
            assertThat(listLiveDataOutput[1]).isEqualTo(ViewEvents.DisplayError(ERROR_EMPTY_LIST))

            logic.observe().removeObserver(liveDataObserver)
        }

        /**
         * - Send event [ViewEvents.DisplayLoading].
         * - Get all from the Repo - in this test the repo will return an error.
         * - Throw the Exception returned by the repo.
         * - Sent event [ViewEvents.DisplayError] with [ERROR_GENERIC].
         */
        @Test
        fun `onStart - error`() {
            val throwable = mockk<Throwable>(relaxed = true)
            val listLiveDataOutput = mutableListOf<ViewEvents>()
            val liveDataObserver = Observer<ViewEvents> { listLiveDataOutput.add(it) }

            every { repo.getAll() } returns Flowable.error(throwable)

            logic.observe().observeForever(liveDataObserver)

            logic.onEvent(IAddPrescriptionContact.LogicEvents.OnStart)

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
         * - In this test the repo will indicate that the save was successful.
         * - Send [ViewEvents.DisplayMessage] to the View with [MSG_SUCCESSFULLY_SAVE].
         */
        @Test
        fun `save - successful`() {
            val title = "title"

            every { repo.add(title) } returns Completable.complete()

            logic.onEvent(IAddPrescriptionContact.LogicEvents.Save(title))

            verify(exactly = 1) { repo.add(title) }
            logic.observe().observeForTesting {
                assertThat(logic.observe().value).isEqualTo(ViewEvents.DisplayMessage(MSG_SUCCESSFULLY_SAVE))
            }
        }

        /**
         * - Save to the repo - verify the title is unchanged.
         * - In this test the repo will indicate that the save failed.
         * - Throw the Exception returned by the repo.
         * - Send [ViewEvents.DisplayMessage] to the View with [ERROR_OPERATION_FAILED].
         */
        @Test
        fun `save - failure`() {
            val throwable = mockk<Throwable>(relaxed = true)
            val title = "title"

            every { repo.add(title) } returns Completable.error(throwable)

            logic.onEvent(IAddPrescriptionContact.LogicEvents.Save(title))

            verify(exactly = 1) { repo.add(title) }
            verify(atLeast = 1) { throwable.printStackTrace() }
            logic.observe().observeForTesting {
                assertThat(logic.observe().value).isEqualTo(ViewEvents.DisplayMessage(ERROR_OPERATION_FAILED))
            }
        }
    }
}