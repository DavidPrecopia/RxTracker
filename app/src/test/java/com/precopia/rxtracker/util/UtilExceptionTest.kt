package com.precopia.rxtracker.util

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UtilExceptionsTest {
    @Test
    fun `throwException - RuntimeException`() {
        assertThrows<RuntimeException> {
            UtilExceptions.throwException(RuntimeException())
        }
    }

    @Test
    fun `throwException - Throwable`() {
        val throwableMock = mockk<Throwable>(relaxed = true)

        UtilExceptions.throwException(throwableMock)

        verify(exactly = 1) { throwableMock.printStackTrace() }
    }
}