/*
 * MIT License
 *
 * Copyright (c) 2024 BreninSul
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.breninsul.servlet.logging

import io.github.breninsul.logging.HttpLoggingHelper
import io.github.breninsul.servlet.logging.caching.request.ServletCachingRequestWrapper
import io.github.breninsul.servlet.logging.caching.request.ServletCachingRequestWrapperByteArray
import io.github.breninsul.servlet.logging.caching.request.ServletCachingRequestWrapperFile
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.Part
import org.springframework.core.Ordered
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingResponseWrapper
import java.nio.charset.Charset
import java.util.*
import java.util.function.Supplier
import java.util.logging.Level
import java.util.logging.Logger

/**
 * ServletLoggingFilter is a filter that logs HTTP requests and responses for a servlet application.
 *
 * This filter wraps around incoming requests and outgoing responses to capture and log
 * relevant data based on the provided configuration.
 *
 * @property properties The configuration properties for logging.
 * @property helper An instance of HttpLoggingHelper to assist with logging operations.
 * @property servletLogger A Logger instance for logging any errors that occur during processing.
 */
open class ServletLoggingFilter(
    protected open val properties: ServletLoggerProperties,
    uriMaskers: List<ServletUriMasking>,
    requestBodyMaskers: List<ServletRequestBodyMasking>,
    responseBodyMaskers: List<ServletResponseBodyMasking>,
) : OncePerRequestFilter(),
    Ordered {
    protected open val helper = HttpLoggingHelper("Servlet", properties.toHttpLoggingProperties(), uriMaskers, requestBodyMaskers, responseBodyMaskers)
    protected open val servletLogger: Logger = Logger.getLogger(ServletLoggingFilter::class.java.name)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        if (!properties.enabled) {
            filterChain.doFilter(request, response)
        }
        val time = System.currentTimeMillis()
        try {
            val id = helper.getIdString()
            val wrappedRequest = wrapRequest(request)
            val wrappedResponse = wrapResponse(wrappedRequest, response)
            try {
                logRequest(wrappedRequest, id, time)
                filterChain.doFilter(wrappedRequest, wrappedResponse)
                if (wrappedRequest is ServletCachingRequestWrapper) {
                    wrappedRequest.clear()
                }
            } finally {
                afterChain(id, wrappedRequest, wrappedResponse, time)
            }
        } catch (t: Throwable) {
            servletLogger.log(Level.SEVERE, "Logging servlet request error !", t)
        }
    }

    open fun afterChain(
        rqId: String,
        request: HttpServletRequest,
        response: HttpServletResponse,
        startTime: Long,
    ) {
        try {
            if (helper.loggingLevel == Level.OFF) {
                return
            }
            response.setHeader("RQ_ID", rqId)
            logResponse(request, response, rqId, startTime)
            if (response is ContentCachingResponseWrapper) {
                response.copyBodyToResponse()
            }
        } catch (t: Throwable) {
            servletLogger.log(Level.SEVERE, "Logging servlet response error !", t)
        }
    }

    /**
     * Log the request details.
     *
     * @param request The HttpServletRequest object representing the request.
     * @param rqId The ID of the request.
     * @param startTime The start time of the request.
     */
    open fun logRequest(
        request: HttpServletRequest,
        rqId: String,
        startTime: Long,
    ) {
        if (helper.loggingLevel == Level.OFF) {
            return
        }

        try {
            val logString =
                constructRqBody(rqId, request, startTime) {
                    val haveToLogBody = request.logRequestBody() ?: properties.request.bodyIncluded
                    val contentLength = request.contentLengthLong
                    val emptyBody = contentLength == 0L
                    val isMultipart = request.isMultipart()

                    if (contentLength > properties.request.maxBodySize && !isMultipart) {
                        return@constructRqBody helper.constructTooBigMsg(contentLength)
                    } else if (!haveToLogBody) {
                        return@constructRqBody ""
                    } else if (emptyBody) {
                        return@constructRqBody ""
                    }

                    if (isMultipart) {
                        val parts: List<Part> = ArrayList(request.parts)
                        val loggedParts =
                            parts
                                .joinToString(";") {
                                    val size = it.size
                                    if (size >= properties.request.maxBodySize) {
                                        return@joinToString "${it.name}:${it.partContentType()}:${helper.constructTooBigMsg(size)}"
                                    } else {
                                        val partContent =
                                            if (it.haveToLogContent()) {
                                                if (request !is ServletCachingRequestWrapper) {
                                                    throw IllegalStateException("Cached request is required for body logging")
                                                }
                                                String(it.inputStream.readAllBytes(), request.getContentEncoding())
                                            } else {
                                                "<FILE $size bytes>"
                                            }
                                        return@joinToString "${it.name}:${it.partContentType()}:$partContent"
                                    }
                                }
                        return@constructRqBody loggedParts
                    } else {
                        if (request !is ServletCachingRequestWrapper) {
                            throw IllegalStateException("Cached request is required for body logging")
                        }
                        return@constructRqBody request.bodyContentString()
                    }
                }
            servletLogger.log(helper.loggingLevel, logString)
        } catch (t: Throwable) {
            servletLogger.log(Level.SEVERE, "", t)
        }
    }

    /**
     * Logs the HTTP response.
     *
     * @param request*/
    protected open fun logResponse(
        request: HttpServletRequest,
        response: HttpServletResponse,
        rqId: String,
        time: Long,
    ) {
        val logString =
            constructRsBody(rqId, response, request, time) {
                val haveToLogBody = request.logResponseBody() ?: properties.response.bodyIncluded
                if (!haveToLogBody) {
                    return@constructRsBody ""
                }
                if (response !is ContentCachingResponseWrapper) {
                    throw IllegalStateException("Cached response is required for body logging")
                }
                return@constructRsBody String(response.contentAsByteArray, Charset.forName(response.characterEncoding))
            }
        servletLogger.log(helper.loggingLevel, logString)
    }

    /**
     * Constructs the request body log message.
     *
     * @param rqId The request ID.
     * @param request The request object.
     * @param time The time taken for the request.
     * @param contentSupplier The request body Supplier.
     * @return The constructed request body log message.
     */
    protected open fun constructRqBody(
        rqId: String,
        request: HttpServletRequest,
        time: Long,
        contentSupplier: Supplier<String?>,
    ): String {
        val type = HttpLoggingHelper.Type.REQUEST
        val message =
            listOf(
                helper.getHeaderLine(type),
                helper.getIdString(rqId, type),
                helper.getUriString(request.logRequestUri(), "${request.method} ${request.getFormattedUriString()}", type),
                helper.getTookString(request.logRequestTookTime(), time, type),
                helper.getHeadersString(request.logRequestHeaders(), request.getHeadersMultiMap(), type),
                helper.getBodyString(request.logRequestBody(), contentSupplier, type),
                helper.getFooterLine(type),
            ).filter { !it.isNullOrBlank() }
                .joinToString("\n")
        return message
    }

    /**
     * Constructs the response body for logging.
     *
     * @param rqId The request ID.
     * @param response The HttpServletResponse response.
     * @param request The HttpServletRequest request.
     * @param time The time taken for the request/response.
     * @param contentSupplier The response body content Supplier.
     * @return The formatted response body.
     */
    protected open fun constructRsBody(
        rqId: String,
        response: HttpServletResponse,
        request: HttpServletRequest,
        time: Long,
        contentSupplier: Supplier<String?>,
    ): String {
        val type = HttpLoggingHelper.Type.RESPONSE
        val message =
            listOf(
                helper.getHeaderLine(type),
                helper.getIdString(rqId, type),
                helper.getUriString(request.logResponseUri(), "${response.status} ${request.method} ${request.getFormattedUriString()}", type),
                helper.getTookString(request.logResponseTookTime(), time, type),
                helper.getHeadersString(request.logResponseHeaders(), response.getHeadersMultiMap(), type),
                helper.getBodyString(request.logResponseBody(), contentSupplier, type),
                helper.getFooterLine(type),
            ).filter { !it.isNullOrBlank() }
                .joinToString("\n")
        return message
    }

    /**
     * Wraps the given HttpServletRequest object to provide caching functionality for the request body if necessary.
     *
     * @param request The HttpServletRequest object to be wrapped.
     * @return The wrapped HttpServletRequest object.
     */
    protected open fun wrapRequest(request: HttpServletRequest): HttpServletRequest {
        if (helper.loggingLevel == Level.OFF) {
            return request
        }
        if (request is ServletCachingRequestWrapper) {
            return request
        }
        val haveToLogBody = request.logRequestBody() ?: properties.request.bodyIncluded
        if (!haveToLogBody) {
            return request
        }
        val isMultipart = request.isMultipart()

        val contentLength = if (isMultipart) request.parts.filter { it.haveToLogContent() }.minOfOrNull { it.size } ?: 0 else request.contentLengthLong
        if (properties.request.maxBodySize <= contentLength) {
            return request
        }
        return if (properties.request.bodySizeToUseTempFileCaching <= contentLength) {
            ServletCachingRequestWrapperFile(request)
        } else {
            ServletCachingRequestWrapperByteArray(request)
        }
    }

    /**
     * Wraps the given HttpServletResponse with a ContentCachingResponseWrapper.
     *
     * @param response The HttpServletResponse to be wrapped.
     * @return The ContentCachingResponseWrapper that wraps the given response.
     */
    protected open fun wrapResponse(
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): HttpServletResponse {
        if (helper.loggingLevel == Level.OFF) {
            return response
        }
        if (response is ContentCachingResponseWrapper) {
            return response
        }
        val haveToLogBody = request.logResponseBody() ?: properties.response.bodyIncluded
        if (!haveToLogBody) {
            return response
        }
        return ContentCachingResponseWrapper(response)
    }

    protected open fun HttpServletRequest.isMultipart() = this.contentType != null && this.contentType.startsWith("multipart/")

    protected open fun Part.partContentType(): String = this.contentType ?: "text/plain"

    protected open fun Part.haveToLogContent(): Boolean = this.partContentType().startsWith("text", true)

    override fun getOrder(): Int = properties.order
}
