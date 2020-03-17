package com.precopia.rxtracker.view.addprescriptionview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.precopia.domain.datamodel.Prescription
import com.precopia.domain.repository.IPrescriptionRepoContract
import com.precopia.rxtracker.util.IUtilSchedulerProviderContract
import com.precopia.rxtracker.util.UtilExceptions
import com.precopia.rxtracker.util.subscribeCompletable
import com.precopia.rxtracker.util.subscribeFlowablePrescription
import com.precopia.rxtracker.view.addprescriptionview.IAddPrescriptionContact.LogicEvents
import com.precopia.rxtracker.view.addprescriptionview.IAddPrescriptionContact.ViewEvents
import com.precopia.rxtracker.view.common.ERROR_EMPTY_LIST
import com.precopia.rxtracker.view.common.ERROR_GENERIC
import com.precopia.rxtracker.view.common.ERROR_OPERATION_FAILED
import com.precopia.rxtracker.view.common.MSG_SUCCESSFULLY_SAVE
import io.reactivex.rxjava3.disposables.CompositeDisposable

class AddPrescriptionLogic(
        private val repo: IPrescriptionRepoContract,
        private val utilSchedulerProvider: IUtilSchedulerProviderContract,
        private val disposable: CompositeDisposable
): IAddPrescriptionContact.Logic {

    private val viewEventLiveData = MutableLiveData<ViewEvents>()


    override fun onEvent(event: LogicEvents) {
        when (event) {
            LogicEvents.OnStart -> onStart()
            is LogicEvents.Save -> save(event.prescription.title)
        }
    }


    private fun onStart() {
        viewEventLiveData.value = ViewEvents.DisplayLoading
        observeRepo()
    }

    private fun observeRepo() {
        disposable.add(subscribeFlowablePrescription(
                repo.getAll(),
                { evalRepoData(it) },
                { evalRepoError(it) },
                utilSchedulerProvider
        ))
    }

    private fun evalRepoData(list: List<Prescription>) {
        if (list.isEmpty()) {
            viewEventLiveData.value = ViewEvents.DisplayError(ERROR_EMPTY_LIST)
        } else {
            viewEventLiveData.value = ViewEvents.DisplayList(list)
        }
    }

    private fun evalRepoError(throwable: Throwable) {
        UtilExceptions.throwException(throwable)
        viewEventLiveData.value = ViewEvents.DisplayError(ERROR_GENERIC)
    }


    private fun save(title: String) {
        disposable.add(subscribeCompletable(
                repo.add(title),
                { viewEventLiveData.value = ViewEvents.DisplayMessage(MSG_SUCCESSFULLY_SAVE) },
                {
                    UtilExceptions.throwException(it)
                    viewEventLiveData.value = ViewEvents.DisplayMessage(ERROR_OPERATION_FAILED)
                },
                utilSchedulerProvider
        ))
    }


    override fun observe(): LiveData<ViewEvents> = viewEventLiveData
}