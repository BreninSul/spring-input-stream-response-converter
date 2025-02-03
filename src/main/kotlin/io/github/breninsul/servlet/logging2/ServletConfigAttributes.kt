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

package io.github.breninsul.servlet.logging2

import io.github.breninsul.logging2.HttpBodyType
import io.github.breninsul.logging2.HttpConfigHeaders
import io.github.breninsul.logging2.HttpConfigHeaders.LOG_REQUEST_MASK_BODY_KEYS
import io.github.breninsul.logging2.HttpConfigHeaders.LOG_RESPONSE_MASK_BODY_KEYS
import io.github.breninsul.logging2.HttpConfigHeaders.TECHNICAL_HEADERS
import io.github.breninsul.logging2.JavaLoggingLevel
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.util.concurrent.Semaphore

const val START_TIME_ATTRIBUTE = "START_TIME_ATTRIBUTE_TECHNICAL_HEADER"
const val RQ_ID_ATTRIBUTE = "RQ_ID_ATTRIBUTE_TECHNICAL_HEADER"

fun HttpServletRequest.getFormattedQueryString() = if (this.queryString == null) "" else "?${this.queryString}"


fun HttpServletRequest.getFormattedUriString() = "${this.requestURI}${this.getFormattedQueryString()}"


fun HttpServletRequest.getHeadersMultiMap(): Map<String, Collection<String>> =
    this.headerNames
        .asSequence()
        .filter { it != null }
        .map { it to this.getHeaders(it).asSequence().toList() }
        .toMap()


fun HttpServletResponse.getHeadersMultiMap(): Map<String, Collection<String>> =
    this.headerNames
        .asSequence()
        .filter { it != null }
        .map { it to this.getHeaders(it).toList() }
        .toMap()

fun HttpServletRequest.getTechnicalHeaders(): Collection<Pair<String, String>> = TECHNICAL_HEADERS.map { it to this.getHeaders(it).asSequence().firstOrNull() }.filter { it.second != null } as Collection<Pair<String, String>>

fun HttpServletRequest.loggingEnabled(enable: Boolean?) = setAttribute(HttpConfigHeaders.LOG_LOGGING_ENABLED, enable)

fun HttpServletRequest.loggingEnabled() = getAttribute(HttpConfigHeaders.LOG_LOGGING_ENABLED) as Boolean?

fun HttpServletRequest.loggingLevel(level: JavaLoggingLevel?) = setAttribute(HttpConfigHeaders.LOG_LOGGING_LEVEL, level)

fun HttpServletRequest.loggingLevel() = getAttribute(HttpConfigHeaders.LOG_LOGGING_LEVEL) as JavaLoggingLevel?

fun HttpServletRequest.requestLoggingLevel(level: JavaLoggingLevel?) = setAttribute(HttpConfigHeaders.LOG_REQUEST_LOGGING_LEVEL, level)

fun HttpServletRequest.requestLoggingLevel() = getAttribute(HttpConfigHeaders.LOG_REQUEST_LOGGING_LEVEL) as JavaLoggingLevel?

fun HttpServletRequest.logRequestId(enable: Boolean?) = setAttribute(HttpConfigHeaders.LOG_REQUEST_ID, enable)

fun HttpServletRequest.logRequestId() = getAttribute(HttpConfigHeaders.LOG_REQUEST_ID) as Boolean?

fun HttpServletRequest.logRequestUri(enable: Boolean?) = setAttribute(HttpConfigHeaders.LOG_REQUEST_URI, enable)

fun HttpServletRequest.logRequestUri(): Boolean? = getAttribute(HttpConfigHeaders.LOG_REQUEST_URI) as Boolean?

fun HttpServletRequest.logRequestHeaders(enable: Boolean?) = setAttribute(HttpConfigHeaders.LOG_REQUEST_HEADERS, enable)

fun HttpServletRequest.logRequestHeaders(): Boolean? = getAttribute(HttpConfigHeaders.LOG_REQUEST_HEADERS) as Boolean?

fun HttpServletRequest.logRequestBody(enable: Boolean?) = setAttribute(HttpConfigHeaders.LOG_REQUEST_BODY, enable)

fun HttpServletRequest.logRequestBody(): Boolean? = getAttribute(HttpConfigHeaders.LOG_REQUEST_BODY) as Boolean?

fun HttpServletRequest.logRequestTookTime(enable: Boolean?) = setAttribute(HttpConfigHeaders.LOG_REQUEST_TOOK_TIME, enable)

fun HttpServletRequest.logRequestTookTime(): Boolean? = getAttribute(HttpConfigHeaders.LOG_REQUEST_TOOK_TIME) as Boolean?

fun HttpServletRequest.loggingRequestMaxBodySize(size: Long?) = setAttribute(HttpConfigHeaders.LOG_REQUEST_MAX_BODY_SIZE, size)

fun HttpServletRequest.loggingRequestMaxBodySize() = getAttribute(HttpConfigHeaders.LOG_REQUEST_MAX_BODY_SIZE) as Long?

fun HttpServletRequest.loggingRequestMaxBodySizeToUseTempFileCaching(size: Long?) = setAttribute(HttpConfigHeaders.LOG_REQUEST_BODY_SIZE_TO_USE_FILE_CACHING, size)

fun HttpServletRequest.loggingRequestMaxBodySizeToUseTempFileCaching() = getAttribute(HttpConfigHeaders.LOG_REQUEST_BODY_SIZE_TO_USE_FILE_CACHING) as Long?

fun HttpServletRequest.loggingRequestMaskHeaders(headers: Collection<String>?) = setAttribute(HttpConfigHeaders.LOG_REQUEST_MASK_HEADERS, headers)

fun HttpServletRequest.loggingRequestMaskHeaders() = getAttribute(HttpConfigHeaders.LOG_REQUEST_MASK_HEADERS) as Collection<String>?

fun HttpServletRequest.loggingRequestMaskQueryParameters(queryParameters: Collection<String>?) = setAttribute(HttpConfigHeaders.LOG_REQUEST_MASK_QUERY_PARAMETERS, queryParameters)

fun HttpServletRequest.loggingRequestMaskQueryParameters() = getAttribute(HttpConfigHeaders.LOG_REQUEST_MASK_QUERY_PARAMETERS) as Collection<String>?

fun HttpServletRequest.loggingRequestMaskBodyKeys(bodyType: HttpBodyType, keysToMask: Collection<String>?) {
    RequestSemaphore.acquire()
    try {
        val map = getAttribute(LOG_REQUEST_MASK_BODY_KEYS)
        if (map == null) {
            setAttribute(LOG_REQUEST_MASK_BODY_KEYS, mutableMapOf(bodyType to keysToMask))
        } else {
            map as MutableMap<HttpBodyType, Collection<String>?>
            map[bodyType] = keysToMask
        }
    } finally {
        RequestSemaphore.release()
    }
}


fun HttpServletRequest.loggingRequestMaskBodyKeys(maskingKeys: Map<HttpBodyType, Collection<String>?>?) {
    RequestSemaphore.acquire()
    try {
        setAttribute(LOG_REQUEST_MASK_BODY_KEYS, maskingKeys)
    } finally {
        RequestSemaphore.release()
    }
}

fun HttpServletRequest.loggingRequestMaskBodyKeys(): Map<HttpBodyType, Collection<String>?>? = getAttribute(LOG_REQUEST_MASK_BODY_KEYS) as Map<HttpBodyType, Collection<String>?>?

fun HttpServletRequest.loggingRequestMaskBodyKeys(bodyType: HttpBodyType): Collection<String>? {
    val map = getAttribute(LOG_REQUEST_MASK_BODY_KEYS) as MutableMap<HttpBodyType, Collection<String>?>?
    return map?.get(bodyType)
}

fun HttpServletRequest.responseLoggingLevel(level: JavaLoggingLevel?) = setAttribute(HttpConfigHeaders.LOG_RESPONSE_LOGGING_LEVEL, level)

fun HttpServletRequest.responseLoggingLevel() = getAttribute(HttpConfigHeaders.LOG_RESPONSE_LOGGING_LEVEL) as JavaLoggingLevel?

fun HttpServletRequest.logResponseId(enable: Boolean?) = setAttribute(HttpConfigHeaders.LOG_RESPONSE_ID, enable)

fun HttpServletRequest.logResponseId() = getAttribute(HttpConfigHeaders.LOG_RESPONSE_ID) as Boolean?

fun HttpServletRequest.logResponseUri(enable: Boolean?) = setAttribute(HttpConfigHeaders.LOG_RESPONSE_URI, enable)

fun HttpServletRequest.logResponseUri(): Boolean? = getAttribute(HttpConfigHeaders.LOG_RESPONSE_URI) as Boolean?

fun HttpServletRequest.logResponseHeaders(enable: Boolean?) = setAttribute(HttpConfigHeaders.LOG_RESPONSE_HEADERS, enable)

fun HttpServletRequest.logResponseHeaders(): Boolean? = getAttribute(HttpConfigHeaders.LOG_RESPONSE_HEADERS) as Boolean?

fun HttpServletRequest.logResponseBody(enable: Boolean?) = setAttribute(HttpConfigHeaders.LOG_RESPONSE_BODY, enable)

fun HttpServletRequest.logResponseBody(): Boolean? = getAttribute(HttpConfigHeaders.LOG_RESPONSE_BODY) as Boolean?

fun HttpServletRequest.logResponseTookTime(enable: Boolean?) = setAttribute(HttpConfigHeaders.LOG_RESPONSE_TOOK_TIME, enable)

fun HttpServletRequest.logResponseTookTime(): Boolean? = getAttribute(HttpConfigHeaders.LOG_RESPONSE_TOOK_TIME) as Boolean?

fun HttpServletRequest.loggingResponseMaxBodySize(size: Long?) = setAttribute(HttpConfigHeaders.LOG_RESPONSE_MAX_BODY_SIZE, size)

fun HttpServletRequest.loggingResponseMaxBodySize() = getAttribute(HttpConfigHeaders.LOG_RESPONSE_MAX_BODY_SIZE) as Long?

fun HttpServletRequest.loggingResponseMaxBodySizeToUseTempFileCaching(size: Long?) = setAttribute(HttpConfigHeaders.LOG_RESPONSE_BODY_SIZE_TO_USE_FILE_CACHING, size)

fun HttpServletRequest.loggingResponseMaxBodySizeToUseTempFileCaching() = getAttribute(HttpConfigHeaders.LOG_RESPONSE_BODY_SIZE_TO_USE_FILE_CACHING)

fun HttpServletRequest.loggingResponseMaskHeaders(headers: Collection<String>?) = setAttribute(HttpConfigHeaders.LOG_RESPONSE_MASK_HEADERS, headers)

fun HttpServletRequest.loggingResponseMaskHeaders() = getAttribute(HttpConfigHeaders.LOG_RESPONSE_MASK_HEADERS) as Collection<String>?

fun HttpServletRequest.loggingResponseMaskQueryParameters(queryParameters: Collection<String>?) = setAttribute(HttpConfigHeaders.LOG_RESPONSE_MASK_QUERY_PARAMETERS, queryParameters)

fun HttpServletRequest.loggingResponseMaskQueryParameters() = getAttribute(HttpConfigHeaders.LOG_RESPONSE_MASK_QUERY_PARAMETERS) as Collection<String>?

fun HttpServletRequest.loggingResponseMaskBodyKeys(bodyType: HttpBodyType, keysToMask: Collection<String>?) {
    RequestSemaphore.acquire()
    try {
        val map = getAttribute(LOG_RESPONSE_MASK_BODY_KEYS)
        if (map == null) {
            setAttribute(LOG_RESPONSE_MASK_BODY_KEYS, mutableMapOf(bodyType to keysToMask))
        } else {
            map as MutableMap<HttpBodyType, Collection<String>?>
            map[bodyType] = keysToMask
        }
    } finally {
        RequestSemaphore.release()
    }
}


fun HttpServletRequest.loggingResponseMaskBodyKeys(maskingKeys: Map<HttpBodyType, Collection<String>?>?) {
    RequestSemaphore.acquire()
    try {
        setAttribute(LOG_RESPONSE_MASK_BODY_KEYS, maskingKeys)
    } finally {
        RequestSemaphore.release()
    }
}


fun HttpServletRequest.loggingResponseMaskBodyKeys(): Map<HttpBodyType, Collection<String>?>? = getAttribute(LOG_RESPONSE_MASK_BODY_KEYS) as Map<HttpBodyType, Collection<String>?>?

fun HttpServletRequest.loggingResponseMaskBodyKeys(bodyType: HttpBodyType): Collection<String>? {
    val map = getAttribute(LOG_RESPONSE_MASK_BODY_KEYS) as MutableMap<HttpBodyType, Collection<String>?>?
    return map?.get(bodyType)
}

object RequestSemaphore : Semaphore(1)
