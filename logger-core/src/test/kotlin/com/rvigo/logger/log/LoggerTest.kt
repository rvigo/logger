package com.rvigo.logger.log

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.MDC
import java.util.UUID

class LoggerTest {

    private val testClass: TestClass = TestClass()

    @BeforeEach
    fun setUp() {
        MDC.clear()
    }

    @Test
    fun `should create the logger`() {
        val logger = testClass.logger

        assertEquals(TestClass::class.qualifiedName, logger.name)
    }

    @Test
    fun `should return a null correlationId initially`() {
        val logger = testClass.logger()

        assertNull(logger.getCorrelationId())
    }

    @Test
    fun `should correctly set a new correlation id`() {
        val correlationId = UUID.randomUUID()

        val logger = testClass.logger()
        logger.setCorrelationId(correlationId)

        assertEquals(correlationId.toString(), logger.getCorrelationId())
    }

    @Test
    fun `should append a single tag`() {
        val logger = TestClass::class.logger()

        val tag = "testKey" to "testValue"
        logger.appendTag(tag)

        assertTrue(MDC.getCopyOfContextMap()!!.isNotEmpty())
        assertTrue(MDC.getCopyOfContextMap()!!.containsKey(TAGS))
        val mdcEntry = MDC.get(TAGS)
        val tags: Map<String, String> = objectMapper.readValue(mdcEntry)
        assertTrue(tags.containsEntry(tag))
    }

    @Test
    fun `should append multiple tags`() {
        val logger = TestClass::class.logger()

        val tags = mapOf(
            "testKey1" to "testValue1",
            "testKey2" to "testValue2"
        )
        logger.appendTags(tags)

        assertTrue(MDC.getCopyOfContextMap()!!.isNotEmpty())
        assertTrue(MDC.getCopyOfContextMap()!!.containsKey(TAGS))
        val mdcEntry = MDC.get(TAGS)
        val tagsMap: Map<String, String> = objectMapper.readValue(mdcEntry)
        assertEquals(tags, tagsMap)
    }

    @Test
    fun `should clears all tags`() {
        val logger = TestClass::class.logger()

        logger.appendTags(mapOf("tag1" to "value1", "tag2" to "value2"))

        assertTrue(MDC.getCopyOfContextMap()!!.isNotEmpty())
        assertTrue(MDC.getCopyOfContextMap()!!.containsKey(TAGS))

        logger.clearTags()

        assertTrue(MDC.getCopyOfContextMap()!!.isEmpty())
    }

    companion object {
        private const val TAGS = "tags"

        private val objectMapper = jacksonObjectMapper()
    }
}

class TestClass {

    val logger = logger()
}
