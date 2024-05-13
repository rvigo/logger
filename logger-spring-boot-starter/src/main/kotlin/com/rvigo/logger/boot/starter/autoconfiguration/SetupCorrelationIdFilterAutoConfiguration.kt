package com.rvigo.logger.boot.starter.autoconfiguration

import com.rvigo.logger.boot.starter.SetupCorrelationIdFilterProperties
import com.rvigo.logger.boot.starter.filter.SetupCorrelationIdFilter
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(SetupCorrelationIdFilterProperties::class)
@ConditionalOnProperty(
    value = ["logger.correlation.enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class SetupCorrelationIdFilterAutoConfiguration {

    @Bean
    fun correlationIdFilter(): FilterRegistrationBean<SetupCorrelationIdFilter> =
        FilterRegistrationBean<SetupCorrelationIdFilter>().apply {
            filter = SetupCorrelationIdFilter()
            order = -999
        }
}