package com.rvigo.logger.boot.starter.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.web.filter.OncePerRequestFilter

class SetupCorrelationIdFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        logger.info("Setting correlationId for incoming request")
        MDC.put("correlationId", request.getHeader("correlationId")?.also {
            logger.info("got correlationId from request header: $it")
        } ?: run {
            logger.info("generating new correlationId")
            val correlationId = java.util.UUID.randomUUID().toString()
            MDC.put("correlationId", correlationId)
            correlationId
        })

        filterChain.doFilter(request, response)
    }
}