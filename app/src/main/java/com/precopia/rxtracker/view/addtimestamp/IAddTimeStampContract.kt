package com.precopia.rxtracker.view.addtimestamp

import androidx.lifecycle.LiveData
import com.precopia.domain.datamodel.Prescription

interface IAddTimeStampContract {
    interface View

    interface Logic {
        fun onEvent(event: LogicEvents)

        fun observe(): LiveData<ViewEvents>
    }

    sealed class ViewEvents {
        object DisplayLoading: ViewEvents()
        data class DisplayList(val list: List<String>): ViewEvents()
        data class DisplayError(val message: String): ViewEvents()
        data class DisplayMessage(val message: String): ViewEvents()
        object Close: ViewEvents()
    }

    sealed class LogicEvents {
        object OnStart: LogicEvents()
        object Cancel: LogicEvents()
        data class Save(val prescription: Prescription): LogicEvents()
    }
}