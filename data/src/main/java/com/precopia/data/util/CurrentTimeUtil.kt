package com.precopia.data.util

import java.text.SimpleDateFormat
import java.util.*

internal class CurrentTimeUtil: ICurrentTimeUtil {
    override fun currentTime(): String = getFormattedDate()

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