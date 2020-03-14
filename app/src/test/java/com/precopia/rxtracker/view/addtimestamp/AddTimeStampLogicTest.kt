package com.precopia.rxtracker.view.addtimestamp

import androidx.lifecycle.Observer
import com.precopia.domain.datamodel.Prescription
import com.precopia.domain.repository.IPrescriptionRepoContract
import com.precopia.domain.repository.ITimeStampRepoContract
import com.precopia.rxtracker.InstantExecutorExtension
import com.precopia.rxtracker.UtilSchedulerProviderMockInit
import com.precopia.rxtracker.observeForTesting
import com.precopia.rxtracker.util.IUtilSchedulerProviderContract
import com.precopia.rxtracker.view.addtimestamp.IAddTimeStampContract.LogicEvents
import com.precopia.rxtracker.view.addtimestamp.IAddTimeStampContract.ViewEvents
import com.precopia.rxtracker.view.common.ERROR_EMPTY_LIST
import com.precopia.rxtracker.view.common.ERROR_GENERIC
import com.precopia.rxtracker.view.common.ERROR_OPERATION_FAILED
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
internal class AddTimeStampLogicTest {

    private val prescriptionRepo = mockk<IPrescriptionRepoContract>()

    private val timeStampRepo = mockk<ITimeStampRepoContract>()

    private val schedulerProvider = mockk<IUtilSchedulerProviderContract>()

    private val disposable = spyk<CompositeDisposable>()


    private lateinit var logic: AddTimeStampLogic


    /**
     * I am re-instantiating the class under test before each test
     * to ensure that the observable returned by [IAddTimeStampContract.Logic.observe]
     * is cleared before the following test.
     */
    @BeforeEach
    fun setUp() {
        logic = AddTimeStampLogic(prescriptionRepo, timeStampRepo, schedulerProvider, disposable)
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
            val listPrescription = listOf(Prescription(0, "title"))
            val listLiveDataOutput = mutableListOf<ViewEvents>()
            val liveDataObserver = Observer<ViewEvents> { listLiveDataOutput.add(it) }

            every { prescriptionRepo.getAll() } returns Flowable.just(listPrescription)

            logic.observe().observeForever(liveDataObserver)

            logic.onEvent(LogicEvents.OnStart)

            verify(exactly = 1) { prescriptionRepo.getAll() }
            assertThat(listLiveDataOutput.size).isEqualTo(2)
            assertThat(listLiveDataOutput[0]).isEqualTo(ViewEvents.DisplayLoading)
            assertThat(listLiveDataOutput[1]).isEqualTo(ViewEvents.DisplayList(listPrescription))

            logic.observe().removeObserver(liveDataObserver)
        }

        /**
         * - Send event [ViewEvents.DisplayLoading].
         * - Get all from the Repo - it will be empty in this test.
         * - Send [ViewEvents.DisplayError] to the View with the message [ERROR_EMPTY_LIST].
         * - Verify that [ViewEvents.DisplayList] was not sent.
         */
        @Test
        fun `onStart - empty list`() {
            val listPrescription = emptyList<Prescription>()
            val listLiveDataOutput = mutableListOf<ViewEvents>()
            val liveDataObserver = Observer<ViewEvents> { listLiveDataOutput.add(it) }

            every { prescriptionRepo.getAll() } returns Flowable.just(listPrescription)

            logic.observe().observeForever(liveDataObserver)

            logic.onEvent(LogicEvents.OnStart)

            verify(exactly = 1) { prescriptionRepo.getAll() }
            assertThat(listLiveDataOutput.size).isEqualTo(2)
            assertThat(listLiveDataOutput[0]).isEqualTo(ViewEvents.DisplayLoading)
            assertThat(listLiveDataOutput[1]).isEqualTo(ViewEvents.DisplayError(ERROR_EMPTY_LIST))

            logic.observe().removeObserver(liveDataObserver)
        }

        /**
         * - Send event [ViewEvents.DisplayLoading].
         * - Get all from the Repo - a Throwable will be returned in this test.
         * - Send [ViewEvents.DisplayError] to the View with the message [ERROR_GENERIC].
         * - Verify that the Throwable was thrown.
         */
        @Test
        fun `onStart - error`() {
            val throwable = mockk<Throwable>(relaxed = true)
            val listLiveDataOutput = mutableListOf<ViewEvents>()
            val liveDataObserver = Observer<ViewEvents> { listLiveDataOutput.add(it) }

            every { prescriptionRepo.getAll() } returns Flowable.error(throwable)

            logic.observe().observeForever(liveDataObserver)

            logic.onEvent(LogicEvents.OnStart)

            verify(exactly = 1) { prescriptionRepo.getAll() }
            verify(atLeast = 1) { throwable.printStackTrace() }
            assertThat(listLiveDataOutput.size).isEqualTo(2)
            assertThat(listLiveDataOutput[0]).isEqualTo(ViewEvents.DisplayLoading)
            assertThat(listLiveDataOutput[1]).isEqualTo(ViewEvents.DisplayError(ERROR_GENERIC))

            logic.observe().removeObserver(liveDataObserver)
        }
    }

    @Nested
    inner class Cancel {
        /**
         * - Send [ViewEvents.Close] to the View.
         */
        @Test
        fun cancel() {
            logic.onEvent(LogicEvents.Cancel)

            logic.observe().observeForTesting {
                assertThat(logic.observe().value).isEqualTo(ViewEvents.Close)
            }
        }
    }

    @Nested
    inner class Save {
        /**
         * - Pass [LogicEvents.Save]'s argument to [ITimeStampRepoContract].
         *   - It will be successfully added in this test.
         * - Send [ViewEvents.Close] to the View.
         */
        @Test
        fun `save - successful`() {
            val title = "title"

            every { timeStampRepo.add(title) } returns Completable.complete()

            logic.onEvent(LogicEvents.Save(title))

            verify(exactly = 1) { timeStampRepo.add(title) }
            logic.observe().observeForTesting {
                assertThat(logic.observe().value).isEqualTo(ViewEvents.Close)
            }
        }

        /**
         * - Pass [LogicEvents.Save]'s argument to [ITimeStampRepoContract].
         *   - It will fail in this test.
         * - Throw the returned Throwable.
         * - Send [ViewEvents.DisplayMessage] to the View with [ERROR_OPERATION_FAILED].
         * - Send [ViewEvents.Close] to the View.
         */
        @Test
        fun `save - failed`() {
            val title = "title"
            val throwable = mockk<Throwable>(relaxed = true)
            val listLiveDataOutput = mutableListOf<Any>()
            val liveDataObserver = Observer<ViewEvents> { listLiveDataOutput.add(it) }

            every { timeStampRepo.add(title) } returns Completable.error(throwable)

            logic.observe().observeForever(liveDataObserver)

            logic.onEvent(LogicEvents.Save(title))

            verify(exactly = 1) { timeStampRepo.add(title) }
            verify(atLeast = 1) { throwable.printStackTrace() }
            assertThat(listLiveDataOutput.size).isEqualTo(2)
            assertThat(listLiveDataOutput[0]).isEqualTo(
                    ViewEvents.DisplayMessage(ERROR_OPERATION_FAILED)
            )
            assertThat(listLiveDataOutput[1]).isEqualTo(ViewEvents.Close)

            logic.observe().removeObserver(liveDataObserver)
        }
    }
}