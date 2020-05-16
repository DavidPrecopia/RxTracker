package com.precopia.data.util

import java.util.*

interface ITimeUtil {
    fun getCurrentTime(): String

    fun calendarToString(calendar: Calendar): String
}