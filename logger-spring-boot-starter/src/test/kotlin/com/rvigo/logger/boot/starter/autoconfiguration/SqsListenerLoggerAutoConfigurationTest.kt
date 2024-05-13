package com.rvigo.logger.boot.starter.autoconfiguration

import com.rvigo.logger.boot.starter.listener.interceptor.SqsMessageInterceptor
import com.rvigo.logger.boot.starter.setupContextRunner
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.runner.ApplicationContextRunner

class SqsListenerLoggerAutoConfigurationTest {

    private val runner: ApplicationContextRunner = setupContextRunner(SqsListenerLoggerAutoConfiguration::class)

    @Test
    fun `should register the bean`() {
        runner.run {

            assertThat(it).hasSingleBean(SqsMessageInterceptor::class.java)
        }
    }

    @Test
    fun `should not register the bean`() {
        runner.withPropertyValues(
            "logger.sqs.enabled=false",
        ).run {

            assertThat(it).doesNotHaveBean(SqsMessageInterceptor::class.java)
        }
    }
}
