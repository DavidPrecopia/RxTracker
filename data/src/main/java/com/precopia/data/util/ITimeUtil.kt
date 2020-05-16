package com.precopia.data.util

import java.util.*

internal const val TIME_UTIL_FORMAT = "MM/dd/yyyy HH:mm"

interface ITimeUtil {
    fun getCurrentTime(): String

    fun calendarToString(calendar: Calendar): String
}