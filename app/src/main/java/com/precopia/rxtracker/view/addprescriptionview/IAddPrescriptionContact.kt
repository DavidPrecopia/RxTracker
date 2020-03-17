package com.precopia.rxtracker.view.addprescriptionview

import androidx.lifecycle.LiveData
import com.precopia.domain.datamodel.Prescription

interface IAddPrescriptionContact {
    interface View

    interface Adapter {
        fun displayList(list: List<Prescription>)
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
    }

    sealed class LogicEvents {
        object OnStart: LogicEvents()
        data class Save(val prescription: Prescription): LogicEvents()
    }
}