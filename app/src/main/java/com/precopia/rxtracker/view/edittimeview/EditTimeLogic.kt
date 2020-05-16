package com.precopia.rxtracker.view.edittimeview

import androidx.lifecycle.ViewModel
import com.precopia.domain.repository.ITimeStampRepoContract
import com.precopia.rxtracker.util.IUtilSchedulerProviderContract
import com.precopia.rxtracker.util.UtilExceptions
import com.precopia.rxtracker.util.subscribeCompletable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.*

class EditTimeLogic(private val repo: ITimeStampRepoContract,
                    private val utilSchedulerProvider: IUtilSchedulerProviderContract,
                    private val disposable: CompositeDisposable
): ViewModel(),
        IEditTimeContract.Logic {

    override fun onEvent(event: IEditTimeContract.LogicEvents) {
        when (event) {
            is IEditTimeContract.LogicEvents.UpdateTime ->
                updateTime(event.id, event.hourOfDay, event.minute)
        }
    }


    private fun updateTime(id: Int, hourOfDay: Int, minute: Int) {
        disposable.add(subscribeCompletable(
                repo.modifyTime(id, getCalendar(hourOfDay, minute)),
                { /*intentionally blank*/ },
                { UtilExceptions.throwException(it) },
                utilSchedulerProvider
        ))
    }

    private fun getCalendar(hourOfDay: Int, minute: Int) =
            Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hourOfDay)
                set(Calendar.MINUTE, minute)
            }


    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}