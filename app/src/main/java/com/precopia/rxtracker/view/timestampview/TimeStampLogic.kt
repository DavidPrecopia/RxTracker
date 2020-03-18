package com.precopia.rxtracker.view.timestampview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.precopia.domain.datamodel.TimeStamp
import com.precopia.domain.repository.ITimeStampRepoContract
import com.precopia.rxtracker.util.IUtilNightModeContract
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
        private val utilNightMode: IUtilNightModeContract
): ViewModel(),
        ITimeStampViewContract.Logic {


    private val viewEventLiveData = MutableLiveData<ViewEvents>()


    private val localTimeStampList = mutableListOf<TimeStamp>()


    override fun onEvent(event: LogicEvents) {
        when (event) {
            LogicEvents.OnStart -> onStart()
            is LogicEvents.DeleteItem -> validateDeletePosition(event.id, event.position)
            LogicEvents.OpenAddPrescriptionView -> viewEventLiveData.setValue(ViewEvents.OpenPrescriptionView)
            LogicEvents.OpenAddTimeStampView -> viewEventLiveData.setValue(ViewEvents.OpenAddTimeStampView)
            is LogicEvents.SetNightMode -> nightMode(event.nightModeEnabled)
        }
    }

    override fun observe(): LiveData<ViewEvents> = viewEventLiveData


    private fun onStart() {
        viewEventLiveData.value = ViewEvents.DisplayLoading
        observeRepo()
    }

    /**
     * When the View restart per a reconfiguration change,
     * it will send another [LogicEvents.OnStart] event, to avoid
     * unnecessarily invoking the Repo, I am storing the list here as well.
     *
     * Google recommends having a separate LiveData instance for data from the Repo.
     * I am only exposing a single LiveData instance because I want to experiment
     * with different ways to structure an app.
     */
    private fun observeRepo() {
        if (localTimeStampList.isNotEmpty()) {
            viewEventLiveData.value = ViewEvents.DisplayList(localTimeStampList)
            return
        }

        disposable.add(subscribeFlowableTimeStamp(
                repo.getAll(),
                { evalTimeStampList(it) },
                { repoError(it) },
                utilSchedulerProvider
        ))
    }

    private fun evalTimeStampList(list: List<TimeStamp>) {
        if (list.isEmpty()) {
            viewEventLiveData.value = ViewEvents.DisplayError(ERROR_EMPTY_LIST)
            return
        }

        with(localTimeStampList) {
            clear()
            addAll(list)
        }
        viewEventLiveData.value = ViewEvents.DisplayList(list)
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
        disposable.add(subscribeCompletable(
                repo.delete(id),
                { /*intentionally empty*/ },
                {
                    UtilExceptions.throwException(
                            IllegalStateException("Encountered an error deleting")
                    )
                },
                utilSchedulerProvider
        ))
    }


    private fun nightMode(nightModeEnabled: Boolean) {
        if (nightModeEnabled) utilNightMode.setDay()
        else utilNightMode.setNight()
    }


    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}