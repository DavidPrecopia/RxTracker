package com.precopia.rxtracker.view.editdateview

interface IEditDateContract {
    interface View

    interface Logic {
        fun onEvent(event: LogicEvents)
    }

    sealed class LogicEvents {
        data class UpdateDate(
                val id: Int,
                val dateTime: String,
                val monthOfYear: Int,
                val dayOfMonth: Int,
                val year: Int
        ): LogicEvents()
    }
}