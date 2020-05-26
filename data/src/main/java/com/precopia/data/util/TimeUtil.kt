package com.precopia.data.util

import com.precopia.domain.time.TIME_FORMAT
import java.text.SimpleDateFormat
import java.util.*

internal class TimeUtil: ITimeUtil {
    override fun getCurrentTime(): String = getFormattedDate()

    override fun calendarToString(calendar: Calendar): String =
            getFormattedDate(calendar)


    private fun getFormattedDate(calendar: Calendar = Calendar.getInstance()) =
            SimpleDateFormat(
                    TIME_FORMAT,
                    Locale.US
            ).format(
                    calendar.time
            )
}