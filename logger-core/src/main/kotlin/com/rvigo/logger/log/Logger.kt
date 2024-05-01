package com.rvigo.logger.log

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import java.util.UUID

private const val CORRELATION_ID = "correlationId"
private const val TAGS = "tags"
private const val DATA = "data"

private val defaultObjectMapper: ObjectMapper = jacksonObjectMapper()
    .registerKotlinModule()
    .registerModule(JavaTimeModule())
    .apply {
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
        configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
        configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true)
        configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true)
    }
    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
    .setSerializationInclusion(JsonInclude.Include.NON_ABSENT)

inline fun <reified T> T.logger(): Logger = LoggerFactory.getLogger(T::class.java)

fun Logger.getCorrelationId(): String = MDC.get(CORRELATION_ID) ?: run {
    val correlationId = UUID.randomUUID().toString()
    MDC.put(CORRELATION_ID, correlationId)
    correlationId
}

fun Logger.setCorrelationId(correlationId: String) = MDC.put(CORRELATION_ID, correlationId)

fun Logger.appendTag(pair: Pair<String, String>) = appendTags(mapOf(pair))

fun Logger.appendTags(map: Map<String, String>) {
    val newMap = appendTagsMap(map)
    MDC.put(TAGS, defaultObjectMapper.writeValueAsString(newMap))
}

private fun appendTagsMap(map: Map<String, String>): Map<String, String> {
    val old = MDC.get(TAGS) ?: "{}"
    val oldMap = defaultObjectMapper.readValue<Map<String, String>>(old)
    return oldMap + map
}

fun Logger.t(message: String) = this.trace(message)

fun Logger.t(message: () -> String) = this.trace(message())

fun Logger.trace(
    tags: Map<String, String>? = null,
    data: Any? = null,
    message: () -> String
) = tryWith(data, tags, message, ::trace)

fun Logger.d(message: String) = this.debug(message)

fun Logger.d(message: () -> String) = this.debug(message())

fun Logger.debug(
    tags: Map<String, String>? = null,
    data: Any? = null,
    message: () -> String
) = tryWith(data, tags, message, ::debug)

fun Logger.i(message: String) = this.info(message)

fun Logger.i(message: () -> String) = this.info(message())

fun Logger.info(
    tags: Map<String, String>? = null,
    data: Any? = null,
    message: () -> String
) = tryWith(data, tags, message, ::info)

fun Logger.w(
    message: String,
    cause: Throwable? = null
) = this.warn(message, cause)

fun Logger.w(
    cause: Throwable? = null,
    message: () -> String
) = this.warn(message(), cause)

fun Logger.warn(
    cause: Throwable? = null,
    tags: Map<String, String>? = null,
    data: Any? = null,
    message: () -> String
) = tryWithCause(data, tags, message, cause, ::warn)

fun Logger.e(
    message: String,
    cause: Throwable? = null
) = this.error(message, cause)

fun Logger.e(
    cause: Throwable? = null,
    message: () -> String
) = this.error(message(), cause)

fun Logger.error(
    tags: Map<String, String>? = null,
    cause: Throwable? = null,
    data: Any? = null,
    message: () -> String
) = tryWithCause(data, tags, message, cause, ::error)

private fun <T> tryWith(
    data: Any?,
    tags: Map<String, String>?,
    message: () -> String,
    wrapped: (String) -> T
) {
    val closeables = MDCCloseables()
    data?.let {
        closeables.add(DATA, defaultObjectMapper.writeValueAsString(it))
    }
    val current = MDC.get(TAGS) ?: "{}"
    tags?.let {
        val newMap = appendTagsMap(it)
        closeables.add(TAGS, defaultObjectMapper.writeValueAsString(newMap))
    }

    closeables.use {
        wrapped(message())
    }
    // restore previous tags
    MDC.put(TAGS, current)
}

private fun <T> tryWithCause(
    data: Any?,
    tags: Map<String, String>?,
    message: () -> String,
    cause: Throwable?,
    wrapped: (String, Throwable?) -> T
) {
    val closeables = MDCCloseables()
    data?.let {
        closeables.add(DATA, defaultObjectMapper.writeValueAsString(it))
    }
    val current = MDC.get(TAGS) ?: "{}"
    tags?.let {
        val newMap = appendTagsMap(it)
        closeables.add(TAGS, defaultObjectMapper.writeValueAsString(newMap))
    }

    closeables.use {
        wrapped(message(), cause)
    }

    // restore previous tags
    MDC.put(TAGS, current)
}
