package com.rvigo.logger.log.provider

import ch.qos.logback.classic.spi.ILoggingEvent
import com.fasterxml.jackson.core.JsonGenerator
import net.logstash.logback.composite.AbstractFieldJsonProvider
import net.logstash.logback.composite.JsonWritingUtils.writeStringField
import org.slf4j.MDC

class CorrelationIdProvider : AbstractFieldJsonProvider<ILoggingEvent>() {

    init {
        this.fieldName = "correlationId"
    }

    override fun writeTo(generator: JsonGenerator, event: ILoggingEvent) {
        MDC.get("correlationId")?.let {
            writeStringField(generator, fieldName, it)
        }
    }
}