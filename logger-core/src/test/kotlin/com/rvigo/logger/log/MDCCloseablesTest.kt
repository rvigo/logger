package com.rvigo.logger.log

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Test
import org.slf4j.MDC

class MDCCloseablesTest {

    @Test
    fun `should close all MDCCloseables`() {
        val mockMDCCloseable: MDC.MDCCloseable = mockk(relaxed = true)
        val closeables = MDCCloseables()

        closeables.setPrivateField("closeables", mutableListOf(mockMDCCloseable))

        closeables.close()

        verify {
            mockMDCCloseable.close()
        }
    }

    @Test
    fun `should not throw exception when no MDCCloseables are present`() {
        val closeables = MDCCloseables()
        assertDoesNotThrow { closeables.close() }
    }
}