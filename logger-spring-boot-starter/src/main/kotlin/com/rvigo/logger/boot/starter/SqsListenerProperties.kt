package com.rvigo.logger.boot.starter

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "logger.sqs")
data class SqsListenerProperties(val enabled: Boolean = true)