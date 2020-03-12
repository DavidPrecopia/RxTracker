package com.precopia.rxtracker.view.timestampview

import com.precopia.domain.datamodel.TimeStamp
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class TimeStampDiffCallbackTest {


    private val one = TimeStamp(1, "titleOne", "timeOne")
    private val two = TimeStamp(2, "titleTwo", "timeTwo")


    private val diffCallback = TimeStampDiffCallback()


    @Test
    fun `areItemsTheSame - true`() {
        assertThat(diffCallback.areItemsTheSame(one, one)).isTrue()
    }

    @Test
    fun `areItemsTheSame - false`() {
        assertThat(diffCallback.areItemsTheSame(one, two)).isFalse()
    }


    @Test
    fun `areContentsTheSame - true`() {
        assertThat(diffCallback.areContentsTheSame(one, one)).isTrue()
    }

    @Test
    fun `areContentsTheSame - false`() {
        assertThat(diffCallback.areContentsTheSame(one, two)).isFalse()
    }
}