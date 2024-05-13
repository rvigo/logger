package com.rvigo.logger.boot.starter.autoconfiguration

import com.rvigo.logger.boot.starter.doesNotHaveValueSatisfying
import com.rvigo.logger.boot.starter.filter.SetupCorrelationIdFilter
import com.rvigo.logger.boot.starter.setupContextRunner
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Condition
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.runner.ApplicationContextRunner
import org.springframework.boot.web.servlet.AbstractFilterRegistrationBean


class SetupCorrelationIdFilterAutoConfigurationTest {

    private val runner: ApplicationContextRunner = setupContextRunner(SetupCorrelationIdFilterAutoConfiguration::class)

    private val filterExistsCondition = Condition<AbstractFilterRegistrationBean<*>>(
        { bean -> bean.filter is SetupCorrelationIdFilter },
        "SetupCorrelationIdFilter"
    )

    @Test
    fun `should register the filter`() {
        runner.run {
            val beans = it.getBeansOfType(AbstractFilterRegistrationBean::class.java)

            assertThat(beans).hasValueSatisfying(filterExistsCondition)
        }
    }

    @Test
    fun `should not register the filter`() {
        runner.withPropertyValues(
            "logger.correlation.enabled=false",
        ).run {
            val beans = it.getBeansOfType(AbstractFilterRegistrationBean::class.java)

            assertThat(beans).doesNotHaveValueSatisfying(filterExistsCondition)
        }
    }
}
