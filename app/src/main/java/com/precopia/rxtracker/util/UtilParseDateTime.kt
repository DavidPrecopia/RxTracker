package com.precopia.rxtracker.util

import com.precopia.domain.time.TIME_FORMAT

/**
 * This operates on the assumption that the date and time String
 * argument is formatted as such: [TIME_FORMAT].
 */
class UtilParseDateTime: IUtilParseDateTime {
    override fun parsedDate(dateTime: String) =
            getParsedList(dateTime).subList(0, 3)

    override fun parsedTime(dateTime: String) =
            getParsedList(dateTime).subList(3, 5)


    private fun getParsedList(dateTime: String) =
            dateTime.split("/", " ", ":").map { it.toInt() }
}