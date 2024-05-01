package com.rvigo.logger.boot.starter

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "logger.http")
data class ServletLoggerProperties(
    val enabled: Boolean = true,
    val exclude: List<String> = emptyList()
)