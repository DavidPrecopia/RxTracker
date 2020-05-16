package com.precopia.data.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat
import java.util.*

internal class TimeUtilTest {


    private val timeUtil = TimeUtil()


    /**
     * Compare a new instance of [Calendar] to the return from [TimeUtil.getCurrentTime].
     */
    @Test
    fun getCurrentTime() {
        assertThat(
                timeUtil.getCurrentTime()
        ).isEqualTo(
                properlyFormattedDate(Calendar.getInstance())
        )
    }

    /**
     * Compare a custom instance of [Calendar] to the return from [TimeUtil.calendarToString].
     */
    @Test
    fun calendarToString() {
        val calendar = Calendar.getInstance().apply {
            this[Calendar.HOUR_OF_DAY] = 12
            this[Calendar.MINUTE] = 0
            this[Calendar.SECOND] = 0
            this[Calendar.MILLISECOND] = 0
            this[Calendar.MONTH] = 1
            this[Calendar.DATE] = 1
            this[Calendar.YEAR] = 2020
            this.timeZone = TimeZone.getDefault()
        }

        assertThat(
                timeUtil.calendarToString(calendar)
        ).isEqualTo(
                properlyFormattedDate(calendar)
        )
    }

    private fun properlyFormattedDate(calendar: Calendar) =
            SimpleDateFormat(
                    TIME_UTIL_FORMAT,
                    Locale.US
            ).format(
                    calendar.time
            )
}