package com.precopia.data.util

import java.util.*

interface ITimeUtil {
    fun getCurrentTime(): String

    fun getCurrentYear(): Int

    fun calendarToString(calendar: Calendar): String

    fun calendarToYear(calendar: Calendar): Int
}