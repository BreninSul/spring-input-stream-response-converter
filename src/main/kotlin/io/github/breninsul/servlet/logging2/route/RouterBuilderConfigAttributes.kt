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

package io.github.breninsul.servlet.logging2.route

import io.github.breninsul.logging2.HttpBodyType
import io.github.breninsul.logging2.HttpConfigHeaders
import io.github.breninsul.logging2.HttpConfigHeaders.LOG_REQUEST_MASK_BODY_KEYS
import io.github.breninsul.logging2.HttpConfigHeaders.LOG_RESPONSE_MASK_BODY_KEYS
import io.github.breninsul.logging2.JavaLoggingLevel
import io.github.breninsul.servlet.logging2.RequestSemaphore
import org.springframework.web.servlet.function.RouterFunctions.Builder

fun Builder.loggingEnabled(enable: Boolean?) = withAttribute(HttpConfigHeaders.LOG_LOGGING_ENABLED, enable)

fun Builder.loggingLevel(level: JavaLoggingLevel?) = withAttribute(HttpConfigHeaders.LOG_LOGGING_LEVEL, level)

fun Builder.requestLoggingLevel(level: JavaLoggingLevel?) = withAttribute(HttpConfigHeaders.LOG_REQUEST_LOGGING_LEVEL, level)

fun Builder.logRequestId(enable: Boolean?) = withAttribute(HttpConfigHeaders.LOG_REQUEST_ID, enable)

fun Builder.logRequestUri(enable: Boolean?) = withAttribute(HttpConfigHeaders.LOG_REQUEST_URI, enable)

fun Builder.logRequestHeaders(enable: Boolean?) = withAttribute(HttpConfigHeaders.LOG_REQUEST_HEADERS, enable)

fun Builder.logRequestBody(enable: Boolean?) = withAttribute(HttpConfigHeaders.LOG_REQUEST_BODY, enable)

fun Builder.logRequestTookTime(enable: Boolean?) = withAttribute(HttpConfigHeaders.LOG_REQUEST_TOOK_TIME, enable)

fun Builder.loggingRequestMaxBodySize(size: Long?) = withAttribute(HttpConfigHeaders.LOG_REQUEST_MAX_BODY_SIZE, size)

fun Builder.loggingRequestMaxBodySizeToUseTempFileCaching(size: Long?) = withAttribute(HttpConfigHeaders.LOG_REQUEST_BODY_SIZE_TO_USE_FILE_CACHING, size)

fun Builder.loggingRequestMaskHeaders(headers: Collection<String>?) = withAttribute(HttpConfigHeaders.LOG_REQUEST_MASK_HEADERS, headers)

fun Builder.loggingRequestMaskQueryParameters(queryParameters: Collection<String>?) = withAttribute(HttpConfigHeaders.LOG_REQUEST_MASK_QUERY_PARAMETERS, queryParameters)

fun Builder.loggingRequestMaskBodyKeys(maskingKeys: Map<HttpBodyType, Collection<String>?>?): Builder {
    RequestSemaphore.acquire()
    try {
        return withAttribute(LOG_REQUEST_MASK_BODY_KEYS, maskingKeys)
    } finally {
        RequestSemaphore.release()
    }
}

fun Builder.responseLoggingLevel(level: JavaLoggingLevel?) = withAttribute(HttpConfigHeaders.LOG_RESPONSE_LOGGING_LEVEL, level)

fun Builder.logResponseId(enable: Boolean?) = withAttribute(HttpConfigHeaders.LOG_RESPONSE_ID, enable)

fun Builder.logResponseUri(enable: Boolean?) = withAttribute(HttpConfigHeaders.LOG_RESPONSE_URI, enable)

fun Builder.logResponseHeaders(enable: Boolean?) = withAttribute(HttpConfigHeaders.LOG_RESPONSE_HEADERS, enable)

fun Builder.logResponseBody(enable: Boolean?) = withAttribute(HttpConfigHeaders.LOG_RESPONSE_BODY, enable)

fun Builder.logResponseTookTime(enable: Boolean?) = withAttribute(HttpConfigHeaders.LOG_RESPONSE_TOOK_TIME, enable)

fun Builder.loggingResponseMaxBodySize(size: Long?) = withAttribute(HttpConfigHeaders.LOG_RESPONSE_MAX_BODY_SIZE, size)

fun Builder.loggingResponseMaxBodySizeToUseTempFileCaching(size: Long?) = withAttribute(HttpConfigHeaders.LOG_RESPONSE_BODY_SIZE_TO_USE_FILE_CACHING, size)

fun Builder.loggingResponseMaskHeaders(headers: Collection<String>?) = withAttribute(HttpConfigHeaders.LOG_RESPONSE_MASK_HEADERS, headers)

fun Builder.loggingResponseMaskQueryParameters(queryParameters: Collection<String>?) = withAttribute(HttpConfigHeaders.LOG_RESPONSE_MASK_QUERY_PARAMETERS, queryParameters)

fun Builder.loggingResponseMaskBodyKeys(maskingKeys: Map<HttpBodyType, Collection<String>?>?): Builder {
    RequestSemaphore.acquire()
    try {
        return withAttribute(LOG_RESPONSE_MASK_BODY_KEYS, maskingKeys)
    } finally {
        RequestSemaphore.release()
    }
}
