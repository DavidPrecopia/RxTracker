package com.precopia.rxtracker.util

import com.precopia.domain.time.TIME_FORMAT

/**
 * This operates on the assumption that the date and time String
 * argument is formatted as such: [TIME_FORMAT].
 */
interface IUtilParseDateTime {
    fun parsedDate(dateTime: String): List<Int>

    fun parsedTime(dateTime: String): List<Int>
}