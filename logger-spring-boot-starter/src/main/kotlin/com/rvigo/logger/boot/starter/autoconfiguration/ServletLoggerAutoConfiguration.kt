package com.rvigo.logger.boot.starter.autoconfiguration

import com.rvigo.logger.boot.starter.ServletLoggerProperties
import com.rvigo.logger.boot.starter.filter.LogRequestResponseFilter
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(ServletLoggerProperties::class)
@ConditionalOnProperty(
    value = ["logger.http.enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class ServletLoggerAutoConfiguration {

    @Bean
    fun requestResponseFilter(properties: ServletLoggerProperties): FilterRegistrationBean<LogRequestResponseFilter> =
        FilterRegistrationBean<LogRequestResponseFilter>().apply {
            filter = LogRequestResponseFilter(properties.exclude)
            order = 999
        }
}