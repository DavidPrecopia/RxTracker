package com.precopia.data.util

import java.text.SimpleDateFormat
import java.util.*

internal class TimeUtil: ITimeUtil {
    override fun getCurrentTime(): String = getFormattedDate()

    override fun calendarToString(calendar: Calendar): String =
            getFormattedDate(calendar)


    private fun getFormattedDate(calendar: Calendar = Calendar.getInstance()) =
            SimpleDateFormat(
                    TIME_UTIL_FORMAT,
                    Locale.US
            ).format(
                    calendar.time
            )
}