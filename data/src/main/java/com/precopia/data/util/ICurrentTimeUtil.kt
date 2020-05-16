package com.precopia.data.util

import java.util.*

interface ICurrentTimeUtil {
    fun currentTime(): String

    fun calendarToString(calendar: Calendar): String
}