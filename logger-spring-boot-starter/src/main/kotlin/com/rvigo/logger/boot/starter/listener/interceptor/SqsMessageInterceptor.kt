package com.rvigo.logger.boot.starter.listener.interceptor

import com.rvigo.logger.log.clearCorrelationId
import com.rvigo.logger.log.i
import com.rvigo.logger.log.logger
import com.rvigo.logger.log.setCorrelationId
import com.rvigo.logger.log.warn
import io.awspring.cloud.sqs.listener.interceptor.MessageInterceptor
import org.springframework.messaging.Message
import java.util.UUID

class SqsMessageInterceptor : MessageInterceptor<Any> {

    private val logger = logger()

    override fun intercept(message: Message<Any>): Message<Any> {
        val headers = message.headers
        val correlationId = extractCorrelationId(headers)

        val effectiveCorrelationId = correlationId?.let {
            logger.i("Setting correlationId!")
            it
        } ?: run {
            logger.warn(
                tags = mapOf(
                    "messageId" to message.headers.id.toString()
                )
            ) { "CorrelationId not found in the message headers! Setting a new one" }

            UUID.randomUUID().toString()
        }

        logger.setCorrelationId(effectiveCorrelationId)
        return message
    }

    private fun extractCorrelationId(headers: Map<String, Any>): String? {
        return headers["correlationId"] as String?
    }

    override fun afterProcessing(message: Message<Any>, t: Throwable?) {
        logger.clearCorrelationId()
        super.afterProcessing(message, t)
    }
}