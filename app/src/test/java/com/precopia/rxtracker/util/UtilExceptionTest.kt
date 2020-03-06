package com.precopia.rxtracker.util

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UtilExceptionsTest {
    @Test
    fun `Throw RuntimeException`() {
        assertThrows<RuntimeException> {
            UtilExceptions.throwException(RuntimeException())
        }
    }

    @Test
    fun `Throw ThrowableException`() {
        val throwableMock = mockk<Throwable>(relaxed = true)

        UtilExceptions.throwException(throwableMock)

        verify(exactly = 1) { throwableMock.printStackTrace() }
    }
}