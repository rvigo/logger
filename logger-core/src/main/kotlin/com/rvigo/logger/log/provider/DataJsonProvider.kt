package com.rvigo.logger.log.provider

import ch.qos.logback.classic.spi.ILoggingEvent
import com.fasterxml.jackson.core.JsonGenerator
import net.logstash.logback.composite.AbstractFieldJsonProvider
import net.logstash.logback.composite.JsonWritingUtils.writeStringField
import org.slf4j.MDC

class DataJsonProvider : AbstractFieldJsonProvider<ILoggingEvent>() {

    init {
        fieldName = "data"
    }

    override fun writeTo(generator: JsonGenerator, event: ILoggingEvent) {
        val data = MDC.get("data")
        data?.let {
            writeStringField(generator, fieldName, data)
        }
    }
}