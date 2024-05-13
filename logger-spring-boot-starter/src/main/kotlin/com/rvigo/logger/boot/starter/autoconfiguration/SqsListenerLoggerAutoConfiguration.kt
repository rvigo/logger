package com.rvigo.logger.boot.starter.autoconfiguration

import com.rvigo.logger.boot.starter.SqsListenerProperties
import com.rvigo.logger.boot.starter.listener.interceptor.SqsMessageInterceptor
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(SqsListenerProperties::class)
@ConditionalOnProperty(
    value = ["logger.sqs.enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class SqsListenerLoggerAutoConfiguration {

    @Bean
    fun sqsMessageInterceptor(): SqsMessageInterceptor = SqsMessageInterceptor()
}