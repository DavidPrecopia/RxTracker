package com.precopia.data.util

import java.text.SimpleDateFormat
import java.util.*

internal class CurrentTimeUtil : ICurrentTimeUtil {
    override fun currentTime(): String {
        val format = "MM/dd/yyyy HH:mm"
        return SimpleDateFormat(
            format,
            Locale.US
        ).format(
            Calendar.getInstance().time
        )
    }
}