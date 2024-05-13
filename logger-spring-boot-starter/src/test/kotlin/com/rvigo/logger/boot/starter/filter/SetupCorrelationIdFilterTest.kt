package com.rvigo.logger.boot.starter.filter

import com.rvigo.logger.boot.starter.TestController
import com.rvigo.logger.log.getCorrelationId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import java.util.UUID

class SetupCorrelationIdFilterTest {
    private val internalLogger = LoggerFactory.getLogger(SetupCorrelationIdFilter::class.java)

    private val filter = SetupCorrelationIdFilter()

    private val mockMvc: MockMvc = standaloneSetup(TestController())
        .addFilter<StandaloneMockMvcBuilder>(filter)
        .build()

    @Test
    fun `should set correlationId when request has correlationId header`() {
        val expectedCorrelationId = UUID.randomUUID().toString()

        mockMvc.get("/test") {
            header("correlationId", expectedCorrelationId)
        }

        val actual = internalLogger.getCorrelationId()

        assertNotNull(actual)
        assertEquals(expectedCorrelationId, actual)
    }

    @Test
    fun `should set correlationId when request does not have correlationId header`() {
        mockMvc.get("/test")

        val actual = internalLogger.getCorrelationId()
        assertNotNull(actual)
    }
}

