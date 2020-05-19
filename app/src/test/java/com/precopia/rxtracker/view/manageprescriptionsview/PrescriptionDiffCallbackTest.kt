package com.precopia.rxtracker.view.manageprescriptionsview

import com.precopia.domain.datamodel.Prescription
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PrescriptionDiffCallbackTest {

    private val one = Prescription(0, "titleOne", 0)
    private val two = Prescription(1, "titleTwo", 1)


    private val diffCallback = PrescriptionDiffCallback()


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