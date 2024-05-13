package com.rvigo.logger.boot.starter.listener.interceptor

import com.rvigo.logger.log.clearCorrelationId
import com.rvigo.logger.log.getCorrelationId
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.messaging.Message
import org.springframework.messaging.MessageHeaders
import java.util.UUID

class SqsMessageInterceptorTest {

    private val internalLogger = LoggerFactory.getLogger(SqsMessageInterceptor::class.java)

    private val interceptor = SqsMessageInterceptor()

    @BeforeEach
    fun setUp() {
        internalLogger.clearCorrelationId()
    }

    @Test
    fun `should extract correlationId from message headers`() {
        val correlationId = UUID.randomUUID().toString()
        val headers = mapOf("correlationId" to correlationId)
        val messageHeaders = MessageHeaders(headers)
        val message: Message<Any> = mockk(relaxed = true) {
            every { getHeaders() } returns messageHeaders
        }

        interceptor.intercept(message)

        val actualCorrelation = internalLogger.getCorrelationId()
        assertNotNull(actualCorrelation)
        assertEquals(correlationId, actualCorrelation)
    }

    @Test
    fun `should create a new correlation when header is missing`() {
        val messageHeaders = MessageHeaders(emptyMap())
        val message: Message<Any> = mockk(relaxed = true) {
            every { headers } returns messageHeaders
        }

        interceptor.intercept(message)

        val actualCorrelation = internalLogger.getCorrelationId()
        assertNotNull(actualCorrelation)
    }
}