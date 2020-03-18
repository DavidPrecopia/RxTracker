package com.precopia.rxtracker.util

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.precopia.rxtracker.common.RxTrackerApplication
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class UtilNightModeTest {

    private val utilNightMode = (ApplicationProvider.getApplicationContext() as RxTrackerApplication)
            .appComponent.utilNightMode()

    @Test
    fun setDayTest() {
        utilNightMode.setDay()
        assertNightModeEnabled(false)
    }

    @Test
    fun setNightTest() {
        utilNightMode.setNight()
        assertNightModeEnabled(true)
    }

    private fun assertNightModeEnabled(expectation: Boolean) {
        assertThat(utilNightMode.nightModeEnabled).isEqualTo(expectation)
    }
}