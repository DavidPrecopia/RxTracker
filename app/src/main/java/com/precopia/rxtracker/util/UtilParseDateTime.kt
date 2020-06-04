package com.precopia.rxtracker.util

import com.precopia.domain.time.TIME_FORMAT
import java.util.*

/**
 * This operates on the assumption that the date and time String
 * argument is formatted as such: [TIME_FORMAT].
 *
 * The returned month is formatted for use with [Calendar];
 * it is decremented by 1 due to the way [Calendar]
 * stores months - i.e., January is 0.
 */
class UtilParseDateTime: IUtilParseDateTime {
    override fun parsedDate(dateTime: String): List<Int> {
        return getParsedList(dateTime).subList(0, 3).apply {
            this[0] -= 1
        }
    }

    override fun parsedTime(dateTime: String) =
            getParsedList(dateTime).subList(3, 5)


    private fun getParsedList(dateTime: String) =
            dateTime.split("/", " ", ":").map { it.toInt() }.toMutableList()
}