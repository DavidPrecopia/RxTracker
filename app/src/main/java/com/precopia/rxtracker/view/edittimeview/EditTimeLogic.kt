package com.precopia.rxtracker.view.edittimeview

import androidx.lifecycle.ViewModel
import com.precopia.domain.repository.ITimeStampRepoContract
import com.precopia.rxtracker.util.IUtilParseDateTime
import com.precopia.rxtracker.util.IUtilSchedulerProviderContract
import com.precopia.rxtracker.util.UtilExceptions
import com.precopia.rxtracker.util.subscribeCompletable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.*

class EditTimeLogic(private val repo: ITimeStampRepoContract,
                    private val utilSchedulerProvider: IUtilSchedulerProviderContract,
                    private val disposable: CompositeDisposable,
                    private val utilParseDateTime: IUtilParseDateTime
): ViewModel(),
        IEditTimeContract.Logic {

    override fun onEvent(event: IEditTimeContract.LogicEvents) {
        when (event) {
            is IEditTimeContract.LogicEvents.UpdateTime ->
                updateTime(event.id, event.dateTime, event.hourOfDay, event.minute)
        }
    }


    private fun updateTime(id: Int, dateTime: String, hourOfDay: Int, minute: Int) {
        disposable.add(subscribeCompletable(
                repo.modifyTime(id, getCalendar(dateTime, hourOfDay, minute)),
                { /*intentionally blank*/ },
                { UtilExceptions.throwException(it) },
                utilSchedulerProvider
        ))
    }

    private fun getCalendar(dateTime: String, hourOfDay: Int, minute: Int): Calendar {
        val dateList = utilParseDateTime.parsedDate(dateTime)
        return Calendar.getInstance().apply {
            set(Calendar.MONTH, (dateList[0]))
            set(Calendar.DATE, dateList[1])
            set(Calendar.YEAR, dateList[2])
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
        }
    }


    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}