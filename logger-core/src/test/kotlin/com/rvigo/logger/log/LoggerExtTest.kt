package com.rvigo.logger.log

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.MDC

class LoggerExtTest {

    // since this test class is about the extension functions, its ok to mock the logger
    private val logger: Logger = mockk(relaxUnitFun = true)

    @Test
    fun `should temporarily set data in MDC`() {
        val customData = Wrapper(DATA)

        every { logger.info(any()) } answers {
            val entry = MDC.get(DATA)
            val wrapper = deserialize<Wrapper>(entry)

            assertEquals(customData, wrapper)
        }

        logger.info(data = customData) {
            "This is a info message"
        }

        assertNull(MDC.get(DATA))
    }

    @Test
    fun `should temporally set tags in MDC`() {
        val tags = mapOf("key" to "value")

        every { logger.info(any()) } answers {
            val entry = MDC.get(TAGS)
            val tagsMap = deserialize<Map<String, String>>(entry)

            assertEquals(tags, tagsMap)
        }

        logger.info(tags = tags) {
            "This is a info message"
        }

        assertEmptyMap(MDC.get(TAGS))
    }

    companion object {
        private const val DATA = "data"
        private const val TAGS = "tags"
    }
}

data class Wrapper(val data: String)
