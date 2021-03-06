package com.precopia.rxtracker.view.timestampview

import androidx.lifecycle.LiveData
import com.precopia.domain.datamodel.TimeStamp

interface ITimeStampViewContract {
    interface View

    interface Adapter {
        fun showList(list: List<TimeStamp>)

        fun delete(position: Int)
    }

    interface Logic {
        fun onEvent(event: LogicEvents)

        fun observe(): LiveData<ViewEvents>
    }


    sealed class ViewEvents {
        object DisplayLoading: ViewEvents()
        data class DisplayList(val list: List<TimeStamp>): ViewEvents()
        data class DisplayError(val message: String): ViewEvents()
        data class DeleteItem(val position: Int): ViewEvents()
        object OpenPrescriptionView: ViewEvents()
        object OpenAddTimeStampView: ViewEvents()
        data class OpenEditTimeView(val id: Int, val dateTime: String): ViewEvents()
        data class OpenEditDateView(val id: Int, val dateTime: String): ViewEvents()
        object DisplayDeleteButton: ViewEvents()
        object HideDeleteButton: ViewEvents()
    }

    sealed class LogicEvents {
        object OnStart: LogicEvents()
        data class EditTime(val id: Int, val dateTime: String): LogicEvents()
        data class EditDate(val id: Int, val dateTime: String): LogicEvents()
        data class DeleteItem(val id: Int, val position: Int): LogicEvents()
        object OpenAddPrescriptionView: LogicEvents()
        object OpenAddTimeStampView: LogicEvents()
        data class SelectedAdd(val id: Int): LogicEvents()
        data class SelectedRemove(val id: Int): LogicEvents()
        object DeleteAll: LogicEvents()
    }
}