package com.rvigo.logger.log

import org.slf4j.MDC
import java.io.Closeable

class MDCCloseables : Closeable {

    private val closeables = mutableListOf<MDC.MDCCloseable>()

    fun add(key: String, value: String): MDCCloseables {
        closeables.add(MDC.putCloseable(key, value))

        return this
    }

    override fun close() {
        closeables.forEach(MDC.MDCCloseable::close)
    }
}