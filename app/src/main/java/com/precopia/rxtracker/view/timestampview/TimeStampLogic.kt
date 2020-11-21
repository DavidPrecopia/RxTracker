package com.precopia.rxtracker.view.timestampview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.precopia.domain.datamodel.TimeStamp
import com.precopia.domain.repository.ITimeStampRepoContract
import com.precopia.rxtracker.util.IUtilSchedulerProviderContract
import com.precopia.rxtracker.util.UtilExceptions
import com.precopia.rxtracker.util.subscribeCompletable
import com.precopia.rxtracker.util.subscribeFlowableTimeStamp
import com.precopia.rxtracker.view.common.ERROR_EMPTY_LIST
import com.precopia.rxtracker.view.common.ERROR_GENERIC
import com.precopia.rxtracker.view.timestampview.ITimeStampViewContract.LogicEvents
import com.precopia.rxtracker.view.timestampview.ITimeStampViewContract.ViewEvents
import io.reactivex.rxjava3.disposables.CompositeDisposable

class TimeStampLogic(
        private val repo: ITimeStampRepoContract,
        private val utilSchedulerProvider: IUtilSchedulerProviderContract,
        private val disposable: CompositeDisposable,
): ViewModel(),
        ITimeStampViewContract.Logic {


    private val viewEventLiveData = MutableLiveData<ViewEvents>()

    private val selected = mutableListOf<Int>()


    override fun onEvent(event: LogicEvents) {
        when (event) {
            LogicEvents.OnStart -> onStart()
            is LogicEvents.EditTime -> viewEvent(ViewEvents.OpenEditTimeView(event.id, event.dateTime))
            is LogicEvents.EditDate -> viewEvent(ViewEvents.OpenEditDateView(event.id, event.dateTime))
            is LogicEvents.DeleteItem -> validateDeletePosition(event.id, event.position)
            LogicEvents.OpenAddPrescriptionView -> viewEvent(ViewEvents.OpenPrescriptionView)
            LogicEvents.OpenAddTimeStampView -> viewEvent(ViewEvents.OpenAddTimeStampView)
            is LogicEvents.SelectedAdd -> addSelected(event.id)
            is LogicEvents.SelectedRemove -> removeSelected(event.id)
            LogicEvents.DeleteAll -> deleteAll()
        }
    }

    private fun addSelected(id: Int) {
        viewEvent(ViewEvents.DisplayDeleteButton)
        if (selected.contains(id).not()) {
            selected.add(id)
        }
    }

    private fun removeSelected(id: Int) {
        selected.remove(id)
        if (selected.isEmpty()) {
            viewEvent(ViewEvents.HideDeleteButton)
        }
    }

    private fun deleteAll() {
        deleteAllFromRepo(selected.toList())
        selected.clear()
        viewEvent(ViewEvents.HideDeleteButton)
    }

    private fun deleteAllFromRepo(selectedCopy: List<Int>) {
        disposable.add(subscribeCompletable(
                repo.deleteAll(selectedCopy),
                { /*intentionally empty*/ },
                { repoError(it) },
                utilSchedulerProvider
        ))
    }


    private fun onStart() {
        viewEvent(ViewEvents.DisplayLoading)
        observeRepo()
    }

    private fun observeRepo() {
        disposable.add(subscribeFlowableTimeStamp(
                repo.getAll(),
                { evalTimeStampList(it) },
                { repoError(it) },
                utilSchedulerProvider
        ))
    }

    private fun evalTimeStampList(list: List<TimeStamp>) {
        if (list.isEmpty()) {
            viewEvent(ViewEvents.DisplayError(ERROR_EMPTY_LIST))
            return
        }
        // This ensures it is not holding old data.
        selected.clear()
        viewEvent(ViewEvents.DisplayList(list))
    }

    private fun repoError(throwable: Throwable) {
        viewEvent(ViewEvents.DisplayError(ERROR_GENERIC))
        UtilExceptions.throwException(throwable)
    }


    private fun validateDeletePosition(id: Int, position: Int) {
        if (position < 0) {
            UtilExceptions.throwException(IllegalArgumentException("Is less then 0."))
        }
        viewEvent(ViewEvents.DeleteItem(position))
        deleteFromRepo(id)
    }

    private fun deleteFromRepo(id: Int) {
        disposable.add(subscribeCompletable(
                repo.delete(id),
                { /*intentionally empty*/ },
                { UtilExceptions.throwException(it) },
                utilSchedulerProvider
        ))
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