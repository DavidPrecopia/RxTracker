package com.precopia.rxtracker.view.timelistview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.precopia.domain.datamodel.TimeStamp
import com.precopia.domain.repository.ITimeStampRepoContract
import com.precopia.rxtracker.util.ISchedulerProviderContract
import com.precopia.rxtracker.util.UtilExceptions
import com.precopia.rxtracker.util.subscribeCompletable
import com.precopia.rxtracker.util.subscribeFlowableTimeStamp
import com.precopia.rxtracker.view.common.ERROR_EMPTY_LIST
import com.precopia.rxtracker.view.common.ERROR_GENERIC
import com.precopia.rxtracker.view.timelistview.ITimeStampListViewContract.LogicEvents
import com.precopia.rxtracker.view.timelistview.ITimeStampListViewContract.ViewEvents
import io.reactivex.rxjava3.disposables.CompositeDisposable

class TimeStampLogic(
    private val repo: ITimeStampRepoContract,
    private val schedulerProvider: ISchedulerProviderContract,
    private val disposable: CompositeDisposable
) : ViewModel(),
    ITimeStampListViewContract.Logic {


    private val viewEventLiveData = MutableLiveData<ViewEvents>()


    override fun onEvent(event: LogicEvents) {
        when (event) {
            LogicEvents.OnStart -> onStart()
            is LogicEvents.DeleteItem -> validateDeletePosition(event.id, event.position)
            LogicEvents.OpenAddPrescriptionView -> viewEventLiveData.setValue(ViewEvents.OpenPrescriptionView)
            LogicEvents.OpenAddTimeStampView -> viewEventLiveData.setValue(ViewEvents.OpenAddTimeStampView)
        }
    }

    override fun observe(): LiveData<ViewEvents> = viewEventLiveData


    private fun onStart() {
        viewEventLiveData.value = ViewEvents.DisplayLoading
        observeRepo()
    }

    private fun observeRepo() {
        disposable.add(
            subscribeFlowableTimeStamp(
                repo.getAll(),
                { evalTimeStampList(it) },
                { repoError(it) },
                schedulerProvider
            )
        )
    }

    private fun evalTimeStampList(list: List<TimeStamp>) {
        if (list.isEmpty()) {
            viewEventLiveData.value = ViewEvents.DisplayError(ERROR_EMPTY_LIST)
        } else {
            viewEventLiveData.value = ViewEvents.DisplayList(list)
        }
    }

    private fun repoError(throwable: Throwable) {
        viewEventLiveData.value = ViewEvents.DisplayError(ERROR_GENERIC)
        UtilExceptions.throwException(throwable)
    }


    private fun validateDeletePosition(id: Int, position: Int) {
        if (position < 0) {
            UtilExceptions.throwException(IllegalArgumentException("Is less then 0."))
        }
        viewEventLiveData.value = ViewEvents.DeleteItem(position)
        deleteFromRepo(id)
    }

    private fun deleteFromRepo(id: Int) {
        disposable.add(
            subscribeCompletable(
                repo.delete(id),
                { /*intentionally empty*/ },
                { UtilExceptions.throwException(IllegalStateException("Encountered an error deleting")) },
                schedulerProvider
            )
        )
    }


    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}