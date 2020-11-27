package com.precopia.rxtracker.view.addtimestamp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.precopia.domain.datamodel.Prescription
import com.precopia.domain.repository.IPrescriptionRepoContract
import com.precopia.domain.repository.ITimeStampRepoContract
import com.precopia.rxtracker.util.IUtilSchedulerProviderContract
import com.precopia.rxtracker.util.UtilExceptions
import com.precopia.rxtracker.util.subscribeCompletable
import com.precopia.rxtracker.util.subscribeFlowablePrescription
import com.precopia.rxtracker.view.addtimestamp.IAddTimeStampContract.LogicEvents
import com.precopia.rxtracker.view.addtimestamp.IAddTimeStampContract.ViewEvents
import com.precopia.rxtracker.view.common.ERROR_EMPTY_LIST
import com.precopia.rxtracker.view.common.ERROR_GENERIC
import com.precopia.rxtracker.view.common.ERROR_OPERATION_FAILED
import io.reactivex.rxjava3.disposables.CompositeDisposable

class AddTimeStampLogic(
        private val prescriptionRepo: IPrescriptionRepoContract,
        private val timeStampRepo: ITimeStampRepoContract,
        private val utilSchedulerProvider: IUtilSchedulerProviderContract,
        private val disposable: CompositeDisposable
): ViewModel(), IAddTimeStampContract.Logic {

    private val viewEventLiveData = MutableLiveData<ViewEvents>()

    override fun onEvent(event: LogicEvents) {
        when (event) {
            LogicEvents.OnStart -> onStart()
            is LogicEvents.Save -> save(event.title)
        }
    }

    private fun onStart() {
        viewEvent(ViewEvents.DisplayLoading)
        observePrescriptionRepo()
    }

    private fun observePrescriptionRepo() {
        disposable.add(
                subscribeFlowablePrescription(
                        prescriptionRepo.getAll(),
                        { evalPrescriptionList(it) },
                        { evalRepoError(it) },
                        utilSchedulerProvider
                )
        )
    }

    private fun evalPrescriptionList(list: List<Prescription>) {
        if (list.isEmpty()) {
            viewEvent(ViewEvents.DisplayError(ERROR_EMPTY_LIST))
        } else {
            viewEvent(ViewEvents.DisplayList(list.map { it.title }))
        }
    }

    private fun evalRepoError(throwable: Throwable) {
        viewEvent(ViewEvents.DisplayError(ERROR_GENERIC))
        UtilExceptions.throwException(throwable)
    }


    private fun save(rxTitle: String) {
        disposable.add(
                subscribeCompletable(
                        timeStampRepo.add(rxTitle),
                        { viewEvent(ViewEvents.Close(true)) },
                        {
                            UtilExceptions.throwException(it)
                            viewEvent(ViewEvents.DisplayMessage(ERROR_OPERATION_FAILED))
                            viewEvent(ViewEvents.Close(false))
                        },
                        utilSchedulerProvider
                )
        )
    }


    private fun viewEvent(event: ViewEvents) {
        viewEventLiveData.value = event
    }

    override fun observe(): LiveData<ViewEvents> = viewEventLiveData


    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}