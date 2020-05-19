package com.precopia.rxtracker.view.manageprescriptionsview

import androidx.lifecycle.LiveData
import com.precopia.domain.datamodel.Prescription

interface IManagePrescriptionsContact {
    interface View

    interface Adapter {
        fun displayList(list: List<Prescription>)

        fun move(fromPosition: Int, toPosition: Int)
    }

    interface Logic {
        fun onEvent(event: LogicEvents)

        fun observe(): LiveData<ViewEvents>
    }


    sealed class ViewEvents {
        object DisplayLoading: ViewEvents()
        data class DisplayList(val list: List<Prescription>): ViewEvents()
        data class DisplayError(val message: String): ViewEvents()
        data class DisplayMessage(val message: String): ViewEvents()
        data class Dragging(val fromPosition: Int, val toPosition: Int): ViewEvents()
    }

    sealed class LogicEvents {
        object OnStart: LogicEvents()
        data class Save(val rxTitle: String): LogicEvents()
        data class Dragging(val fromPosition: Int, val toPosition: Int): LogicEvents()
        data class PermanentlyMoved(val newPosition: Int): LogicEvents()
    }
}