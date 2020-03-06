package com.precopia.rxtracker.view.timelistview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.precopia.domain.datamodel.TimeStamp
import com.precopia.domain.repository.ITimeStampRepoContract
import com.precopia.rxtracker.util.ISchedulerProviderContract
import com.precopia.rxtracker.util.UtilExceptions
import com.precopia.rxtracker.util.subscribeCompletable
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


//    init {
//        viewEventLiveData.value = ViewEvents.DisplayLoading
//        disposable.add(
//            subscribeFlowableTimeStamp(
//                repo.getAll(),
//                { evalTimeStampList(it) },
//                { repoError(it) },
//                schedulerProvider
//            )
//        )
//    }

    private fun evalTimeStampList(list: List<TimeStamp>) {
        if (list.isEmpty()) {
            viewEventLiveData.value = ViewEvents.DisplayError("LIST IS EMPTY - PLACEHOLDER")
        } else {
            viewEventLiveData.value = ViewEvents.DisplayList(list)
        }
    }

    private fun repoError(throwable: Throwable) {
    }


    override fun onEvent(event: LogicEvents) {
        when (event) {
            is LogicEvents.DeleteItem -> validateDeletePosition(event.id, event.position)
            LogicEvents.OpenAddPrescriptionView -> viewEventLiveData.setValue(ViewEvents.OpenPrescriptionView)
            LogicEvents.OpenAddTimeStampView -> viewEventLiveData.setValue(ViewEvents.OpenAddTimeStampView)
        }
    }

    override fun observe(): LiveData<ViewEvents> = viewEventLiveData


    private fun validateDeletePosition(id: Int, position: Int) {
        if (position < 0) {
            UtilExceptions.throwException(IllegalArgumentException("Is less then 0."))
        }

        viewEventLiveData.value = ViewEvents.DeleteItem(position)
        disposable.add(subscribeCompletable(
            repo.delete(id),
            { /*intentionally empty*/ },
            { UtilExceptions.throwException(IllegalStateException("Encountered an error deleting")) },
            schedulerProvider
        ))
    }


    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}