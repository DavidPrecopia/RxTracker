package com.precopia.rxtracker.view.manageprescriptionsview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.precopia.domain.datamodel.Prescription
import com.precopia.domain.repository.IPrescriptionRepoContract
import com.precopia.rxtracker.util.IUtilSchedulerProviderContract
import com.precopia.rxtracker.util.UtilExceptions
import com.precopia.rxtracker.util.subscribeCompletable
import com.precopia.rxtracker.util.subscribeFlowablePrescription
import com.precopia.rxtracker.view.common.ERROR_EMPTY_LIST
import com.precopia.rxtracker.view.common.ERROR_GENERIC
import com.precopia.rxtracker.view.common.ERROR_OPERATION_FAILED
import com.precopia.rxtracker.view.common.ERROR_TITLE
import com.precopia.rxtracker.view.common.MSG_SUCCESSFULLY_SAVE
import com.precopia.rxtracker.view.manageprescriptionsview.IManagePrescriptionsContact.LogicEvents
import com.precopia.rxtracker.view.manageprescriptionsview.IManagePrescriptionsContact.ViewEvents
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.*

class ManagePrescriptionsLogic(
        private val repo: IPrescriptionRepoContract,
        private val utilSchedulerProvider: IUtilSchedulerProviderContract,
        private val disposable: CompositeDisposable
): ViewModel(), IManagePrescriptionsContact.Logic {

    private var prescriptionList: MutableList<Prescription> = ArrayList()

    private val viewEventLiveData = MutableLiveData<ViewEvents>()


    override fun onEvent(event: LogicEvents) {
        when (event) {
            LogicEvents.OnStart -> onStart()
            is LogicEvents.Save -> save(event.rxTitle)
            is LogicEvents.Dragging -> dragging(event.fromPosition, event.toPosition)
            is LogicEvents.PermanentlyMoved -> permanentlyMoved(event.newPosition)
            is LogicEvents.DeleteItem -> deleteItem(event.id, event.position)
        }
    }


    private fun onStart() {
        viewEventLiveData.value = ViewEvents.DisplayLoading
        observeRepo()
    }

    private fun observeRepo() {
        disposable.add(subscribeFlowablePrescription(
                repo.getAll(),
                {
                    prescriptionList = it.toMutableList()
                    evalRepoData()
                },
                { evalRepoError(it) },
                utilSchedulerProvider
        ))
    }

    private fun evalRepoData() {
        if (prescriptionList.isEmpty()) {
            viewEventLiveData.value = ViewEvents.DisplayError(ERROR_EMPTY_LIST)
        } else {
            viewEventLiveData.value = ViewEvents.DisplayList(prescriptionList)
        }
    }

    private fun evalRepoError(throwable: Throwable) {
        UtilExceptions.throwException(throwable)
        viewEventLiveData.value = ViewEvents.DisplayError(ERROR_GENERIC)
    }


    private fun save(title: String) {
        if (title.isEmpty()) viewEventLiveData.value = ViewEvents.DisplayMessage(ERROR_TITLE)
        else saveToRepo(title)
    }

    private fun saveToRepo(title: String) {
        disposable.add(subscribeCompletable(
                repo.add(title, prescriptionList.size),
                { viewEventLiveData.value = ViewEvents.DisplayMessage(MSG_SUCCESSFULLY_SAVE) },
                {
                    UtilExceptions.throwException(it)
                    viewEventLiveData.value = ViewEvents.DisplayMessage(ERROR_OPERATION_FAILED)
                },
                utilSchedulerProvider
        ))
    }


    private fun dragging(fromPosition: Int, toPosition: Int) {
        viewEventLiveData.value = ViewEvents.Dragging(fromPosition, toPosition)
        Collections.swap(prescriptionList, fromPosition, toPosition)
    }

    private fun permanentlyMoved(newPosition: Int) {
        val prescription = prescriptionList[newPosition]
        disposable.add(subscribeCompletable(
                repo.updatePosition(prescription.id, prescription.position, newPosition),
                { /*intentionally blank*/ },
                { UtilExceptions.throwException(it) },
                utilSchedulerProvider
        ))
    }


    private fun deleteItem(id: Int, position: Int) {
        if (position < 0) {
            UtilExceptions.throwException(IllegalArgumentException("Is less then 0."))
        }
        viewEventLiveData.value = ViewEvents.DeleteItem(position)
        deleteFromRepo(id)
    }

    private fun deleteFromRepo(id: Int) {
        disposable.add(subscribeCompletable(
                repo.delete(id),
                { /*intentionally blank*/ },
                { UtilExceptions.throwException(it) },
                utilSchedulerProvider
        ))
    }


    override fun observe(): LiveData<ViewEvents> = viewEventLiveData


    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}