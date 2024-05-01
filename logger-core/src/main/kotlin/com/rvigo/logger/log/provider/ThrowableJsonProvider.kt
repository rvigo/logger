package com.rvigo.logger.log.provider

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.classic.spi.IThrowableProxy
import com.fasterxml.jackson.core.JsonGenerator
import net.logstash.logback.composite.AbstractFieldJsonProvider


class ThrowableJsonProvider : AbstractFieldJsonProvider<ILoggingEvent>() {

    init {
        fieldName = "error"
    }

    private val IThrowableProxy.stackTrace: String
        get() = this.stackTraceElementProxyArray.joinToString("\n")

    override fun writeTo(generator: JsonGenerator, event: ILoggingEvent) {
        event.throwableProxy?.let {
            generator.writeFieldName("error")
            generator.writeStartObject()
            generator.writeStringField("message", it.message)
            generator.writeStringField("class", it.className)
            generator.writeStringField("stackTrace", it.stackTrace)
            generator.writeEndObject()
        }
    }
}
