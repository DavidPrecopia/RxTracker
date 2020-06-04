package com.precopia.rxtracker.view.editdateview

import androidx.lifecycle.ViewModel
import com.precopia.domain.repository.ITimeStampRepoContract
import com.precopia.rxtracker.util.IUtilParseDateTime
import com.precopia.rxtracker.util.IUtilSchedulerProviderContract
import com.precopia.rxtracker.util.UtilExceptions
import com.precopia.rxtracker.util.subscribeCompletable
import com.precopia.rxtracker.view.editdateview.IEditDateContract.LogicEvents
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.*

class EditDateLogic(
        private val repo: ITimeStampRepoContract,
        private val utilSchedulerProvider: IUtilSchedulerProviderContract,
        private val disposable: CompositeDisposable,
        private val utilParseDateTime: IUtilParseDateTime
): ViewModel(), IEditDateContract.Logic {

    override fun onEvent(event: LogicEvents) {
        when (event) {
            is LogicEvents.UpdateDate -> updateDate(
                    event.id, event.dateTime, event.monthOfYear, event.dayOfMonth, event.year
            )
        }
    }

    private fun updateDate(id: Int, dateTime: String, month: Int, day: Int, year: Int) {
        disposable.add(subscribeCompletable(
                repo.modifyDateTime(id, getCalendar(dateTime, month, day, year)),
                { /*intentionally blank*/ },
                { UtilExceptions.throwException(it) },
                utilSchedulerProvider
        ))
    }

    private fun getCalendar(dateTime: String, month: Int, day: Int, year: Int): Calendar {
        val timeList = utilParseDateTime.parsedTime(dateTime)
        return Calendar.getInstance().apply {
            set(Calendar.MONTH, month)
            set(Calendar.DATE, day)
            set(Calendar.YEAR, year)
            set(Calendar.HOUR_OF_DAY, timeList[0])
            set(Calendar.MINUTE, timeList[1])
        }
    }


    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}