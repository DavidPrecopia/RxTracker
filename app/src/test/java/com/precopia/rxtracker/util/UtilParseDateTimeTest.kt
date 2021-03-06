package com.precopia.rxtracker.util

import com.precopia.domain.time.TIME_FORMAT
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

/**
 * [UtilParseDateTime] operates on the assumption that the date and time String
 * argument is formatted as such: [TIME_FORMAT].
 */
internal class UtilParseDateTimeTest {


    private val util = UtilParseDateTime()


    /**
     * The returned month is formatted for use with [Calendar];
     * it is decremented by 1 due to the way [Calendar]
     * stores months - i.e., January is 0.
     */
    @Test
    fun parsedDate() {
        val month = "01"
        val day = "01"
        val year = "2020"
        val dateTime = "$month/$day/$year 12:00"

        val parsedList = listOf(month.toInt() - 1, day.toInt(), year.toInt())

        assertThat(
                util.parsedDate(dateTime)
        ).isEqualTo(
                parsedList
        )
    }

    @Test
    fun parsedTime() {
        val hour = "12"
        val minute = "00"
        val dateTime = "01/01/2020 $hour:$minute"

        val parsedList = listOf(hour.toInt(), minute.toInt())

        assertThat(
                util.parsedTime(dateTime)
        ).isEqualTo(
                parsedList
        )
    }
}