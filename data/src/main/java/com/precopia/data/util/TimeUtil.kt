package com.precopia.data.util

import java.text.SimpleDateFormat
import java.util.*

internal class TimeUtil: ITimeUtil {
    override fun getCurrentTime(): String = getFormattedDate()

    override fun calendarToString(calendar: Calendar): String =
            getFormattedDate(calendar)


    private fun getFormattedDate(calendar: Calendar = Calendar.getInstance()): String {
        val format = "MM/dd/yyyy HH:mm"
        return SimpleDateFormat(
                format,
                Locale.US
        ).format(
                calendar.time
        )
    }
}