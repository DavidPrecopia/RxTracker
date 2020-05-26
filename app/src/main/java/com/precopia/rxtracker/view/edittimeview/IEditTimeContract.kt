package com.precopia.rxtracker.view.edittimeview

interface IEditTimeContract {
    interface View

    interface Logic {
        fun onEvent(event: LogicEvents)
    }

    sealed class LogicEvents {
        data class UpdateTime(val id: Int, val dateTime: String, val hourOfDay: Int, val minute: Int): LogicEvents()
    }
}