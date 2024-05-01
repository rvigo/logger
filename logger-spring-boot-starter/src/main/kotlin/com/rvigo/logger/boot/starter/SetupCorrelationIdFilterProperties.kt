package com.rvigo.logger.boot.starter

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "logger.correlation")
data class SetupCorrelationIdFilterProperties(val enabled: Boolean = true)