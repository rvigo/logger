package com.rvigo.logger.boot.starter.filter

import com.rvigo.logger.boot.starter.TestController
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder

class LogRequestResponseFilterTest {

    private val excludePaths = listOf("/my/*/path", "/info*")

    private val filter = LogRequestResponseFilter(excludePaths)

    private val spy = spyk(filter, recordPrivateCalls = true)

    private val mockMvc = MockMvcBuilders.standaloneSetup(TestController())
        .addFilter<StandaloneMockMvcBuilder>(spy)
        .build()

    @Test
    fun `should not filter when request path is excluded`() {
        var shouldNotFilterResult: Boolean? = null

        every {
            spy["shouldNotFilter"](any<HttpServletRequest>())
        } answers {
            shouldNotFilterResult = callOriginal() as Boolean
            shouldNotFilterResult
        }

        mockMvc.get("/my/cool/path")

        assertNotNull(shouldNotFilterResult)
        assertTrue(shouldNotFilterResult!!)
        verify {
            spy["shouldNotFilter"](any<HttpServletRequest>())
        }
    }
}
