package com.rvigo.logger.boot.starter.filter

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.rvigo.logger.log.info
import com.rvigo.logger.log.logger
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper

class LogRequestResponseFilter(private val excludePaths: List<String>) : OncePerRequestFilter() {

    private val _logger = logger()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val res = ContentCachingResponseWrapper(response)
        val req = ContentCachingRequestWrapper(request)

        req.logInfo()
        filterChain.doFilter(req, res)
        res.logInfo()
        res.copyBodyToResponse()
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean = excludePaths.any {
        AntPathMatcher().match(it, request.requestURI)
    }

    private fun ContentCachingRequestWrapper.logInfo() {
        val parameters = getRequestUrlParameters(this)
        val headers = getRequestHeaders(this)
        val body = getRequestBody(this).nullIfEmpty()

        val bodyAsMap: Map<String, String>? = body?.let(jacksonObjectMapper()::readValue)
        _logger.info(data = bodyAsMap) {
            "Got a request to $parameters with headers: [$headers]"
        }
    }

    private fun ContentCachingResponseWrapper.logInfo() {
        val status = getResponseStatus(this)
        val body = getResponseBody(this).nullIfEmpty()

        val bodyAsMap: Map<String, String>? = body?.let(jacksonObjectMapper()::readValue)
        _logger.info(data = bodyAsMap) {
            "Got a response with status: $status"
        }
    }


    private fun getRequestBody(request: ContentCachingRequestWrapper): String = String(request.contentAsByteArray)

    private fun getRequestUrlParameters(request: ContentCachingRequestWrapper): String =
        request.queryString?.let { "${request.method} ${request.requestURI}?$it" }
            ?: "${request.method} ${request.requestURI}"

    private fun getRequestHeaders(request: ContentCachingRequestWrapper): String =
        request.headerNames.asSequence().map { headerName ->
            request.getHeaders(headerName).asSequence().map { headerValue ->
                "$headerName: $headerValue"
            }.joinToString(", ")
        }.joinToString(", ")

    private fun getResponseStatus(response: ContentCachingResponseWrapper): String {
        val status = response.status
        return "$status ${HttpStatus.valueOf(status).reasonPhrase}"
    }

    private fun getResponseBody(response: ContentCachingResponseWrapper): String {
        val content = response.contentAsByteArray
        return String(content, charset(response.characterEncoding))
    }

    private fun String.nullIfEmpty(): String? = ifEmpty { null }
}
