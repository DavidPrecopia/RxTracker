package com.precopia.rxtracker.view.timelistview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.precopia.domain.datamodel.TimeStamp
import com.precopia.domain.repository.ITimeStampRepoContract
import com.precopia.rxtracker.util.ISchedulerProviderContract
import com.precopia.rxtracker.util.UtilExceptions
import com.precopia.rxtracker.view.timelistview.ITimeStampListViewContract.LogicEvents
import com.precopia.rxtracker.view.timelistview.ITimeStampListViewContract.ViewEvents
import io.reactivex.rxjava3.disposables.CompositeDisposable

class TimeStampLogic(
    private val view: ITimeStampListViewContract.View,
    private val repo: ITimeStampRepoContract,
    private val schedulerProvider: ISchedulerProviderContract,
    private val disposable: CompositeDisposable
) : ViewModel(),
    ITimeStampListViewContract.Logic {


    private val timeStampLiveData = MutableLiveData<List<TimeStamp>>()


    // TODO Invoke the repo
    init {

    }


    override fun onEvent(event: LogicEvents) {
        when (event) {
            is LogicEvents.DeleteItem -> validateDeletePosition(event.position)
            LogicEvents.OpenAddPrescriptionView -> view.onEvent(ViewEvents.OpenPrescriptionView)
            LogicEvents.OpenAddTimeStampView -> view.onEvent(ViewEvents.OpenAddTimeStampView)
        }
    }

    private fun validateDeletePosition(position: Int) {
        if (position >= 0) {
            view.onEvent(ViewEvents.DeleteItem(position))
        } else {
            UtilExceptions.throwException(IllegalArgumentException("Is less then 0."))
        }
    }


    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}