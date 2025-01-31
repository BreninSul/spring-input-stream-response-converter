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

import io.github.breninsul.logging.HttpConfigHeaders
import io.github.breninsul.logging.HttpConfigHeaders.TECHNICAL_HEADERS
import io.github.breninsul.logging.HttpMaskSettings
import io.github.breninsul.logging.JavaLoggingLevel
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.util.logging.Level

/**
 * Returns the formatted query string for the current HTTP servlet request.
 * If the query string is null, an empty string is returned.
 *
 * @return the formatted query string or an empty string
 */
fun HttpServletRequest.getFormattedQueryString() = if (this.queryString == null) "" else "?${this.queryString}"

/**
 * Returns a formatted URI string by combining the request URI and query string (if it exists).
 *
 * @return The formatted URI string.
 */
fun HttpServletRequest.getFormattedUriString() = "${this.requestURI}${this.getFormattedQueryString()}"

/**
 * Converts the HttpHeaders to a MultiMap where keys are strings and values are lists of strings.
 *
 * @return The converted MultiMap.
 */
fun HttpServletRequest.getHeadersMultiMap(): Map<String, List<String>> =
    this.headerNames
        .asSequence()
        .filter { it != null }
        .map { it to this.getHeaders(it).asSequence().toList() }
        .toMap()

/**
 * Retrieves all the headers of the HttpServletResponse and returns them as a Map with header names as keys and
 * corresponding header values as a list of strings.
 *
 * @return map of headers, where each header name is associated with a list of header values
 */
fun HttpServletResponse.getHeadersMultiMap(): Map<String, List<String>> =
    this.headerNames
        .asSequence()
        .filter { it != null }
        .map { it to this.getHeaders(it).toList() }
        .toMap()

/**
 * Returns a list of technical headers present in the HTTP request.
 *
 * @return A list of pairs representing the technical headers, where each pair consists of a header name and its corresponding value.
 */
fun HttpServletRequest.getTechnicalHeaders(): List<Pair<String, String>> = TECHNICAL_HEADERS.map { it to this.getHeaders(it).asSequence().firstOrNull() }.filter { it.second != null } as List<Pair<String, String>>

/**
 * Sets the logging enabled state for the HTTP request.
 *
 * @param enable Boolean value indicating whether logging is enabled (true), disabled (false),
 *               or null if no specific logging state is provided.
 */
fun HttpServletRequest.loggingEnabled(enable: Boolean?) {
    setAttribute(HttpConfigHeaders.LOG_LOGGING_ENABLED, enable)
}

/**
 * Extension function for `HttpServletRequest` to determine if logging is enabled for the request.
 *
 * This method checks if the attribute associated with the logging configuration key,
 * `HttpConfigHeaders.LOG_LOGGING_ENABLED`, exists and retrieves its value.
 *
 * @receiver The HTTP servlet request object from which the logging configuration is retrieved.
 * @return The value of the logging configuration attribute, if present, or `null` otherwise.
 */
fun HttpServletRequest.loggingEnabled() =getAttribute(HttpConfigHeaders.LOG_LOGGING_ENABLED ) as Boolean?
/**
 * Sets the logging level for the current HTTP servlet request by adding it as an attribute.
 *
 * @param level The desired logging level to be set. If null, no logging level is defined.
 */
fun HttpServletRequest.loggingLevel(level: JavaLoggingLevel?) {
    setAttribute(HttpConfigHeaders.LOG_LOGGING_LEVEL, level)
}

/**
 * Retrieves the logging level attribute from the current HttpServletRequest.
 * This value corresponds to the attribute key defined by `HttpConfigHeaders.LOG_LOGGING_LEVEL`.
 *
 * @receiver The HttpServletRequest instance from which the logging level will be retrieved.
 * @return The logging level attribute value as set in the request, or `null` if the attribute is not present.
 */
fun HttpServletRequest.loggingLevel() = getAttribute(HttpConfigHeaders.LOG_LOGGING_LEVEL) as JavaLoggingLevel?

/**
 * Sets the value of the attribute `HttpConfigHeaders.LOG_REQUEST_URI` to enable or disable logging of request URI.
 *
 * @param enable Boolean value indicating whether to enable or disable logging of request URI.
 */
fun HttpServletRequest.logRequestUri(enable: Boolean?) {
    setAttribute(HttpConfigHeaders.LOG_REQUEST_URI, enable)
}

/**
 * Retrieves the value of the "LOG_REQUEST_URI" header from the current HTTP request headers and
 * converts it to a Boolean.
 *
 * @return The Boolean value of the "LOG_REQUEST_URI" header, or null if the header is not present
 * or cannot be converted to a Boolean.
 */
fun HttpServletRequest.logRequestUri(): Boolean? = getAttribute(HttpConfigHeaders.LOG_REQUEST_URI) as Boolean?

/**
 * Sets the attribute to log the request headers value in the HttpServletRequest object.
 * @param enable If set to true, the request headers will be logged. If set to false, the request headers will not be logged.
 */
fun HttpServletRequest.logRequestHeaders(enable: Boolean?) {
    setAttribute(HttpConfigHeaders.LOG_REQUEST_HEADERS, enable)
}

/**
 * Retrieves the value of the "LOG_REQUEST_HEADERS" header from the HttpRequest headers.
 *
 * @return The value of the "LOG_REQUEST_HEADERS" header as a Boolean, or null if the header is not present or cannot be parsed as a Boolean.
 */
fun HttpServletRequest.logRequestHeaders(): Boolean? = getAttribute(HttpConfigHeaders.LOG_REQUEST_HEADERS) as Boolean?

/**
 * Sets the attribute "LOG_REQUEST_BODY" for the current HttpServletRequest.
 *
 * @param enable Whether to enable logging of request body.
 */
fun HttpServletRequest.logRequestBody(enable: Boolean?) {
    setAttribute(HttpConfigHeaders.LOG_REQUEST_BODY, enable)
}

/**
 * Retrieves the value of the "Log-Request-Body" header from the HttpRequest instance.
 *
 * @return The boolean value of the "Log-Request-Body" header, or null if the header is not present or cannot be parsed as a boolean.
 */
fun HttpServletRequest.logRequestBody(): Boolean? = getAttribute(HttpConfigHeaders.LOG_REQUEST_BODY) as Boolean?

/**
 * Sets the attribute to log the request took time to the provided HttpServletRequest object.
 *
 * @param enable Indicates whether to enable or disable the logging of request took time.
 */
fun HttpServletRequest.logRequestTookTime(enable: Boolean?) {
    setAttribute(HttpConfigHeaders.LOG_REQUEST_TOOK_TIME, enable)
}

/**
 * Retrieves the value of the LOG_REQUEST_TOOK_TIME header and converts it to a Boolean.
 *
 * @return The Boolean value of the LOG_REQUEST_TOOK_TIME header, or null if the header is not present or cannot be parsed as a Boolean.
 */
fun HttpServletRequest.logRequestTookTime(): Boolean? = getAttribute(HttpConfigHeaders.LOG_REQUEST_TOOK_TIME) as Boolean?

/**
 * Sets the maximum allowable size for logging the request body.
 *
 * @param size The maximum body size in bytes to be logged. If null, no size restriction is applied.
 */
fun HttpServletRequest.loggingRequestMaxBodySize(size: Long?) {
    setAttribute(HttpConfigHeaders.LOG_REQUEST_MAX_BODY_SIZE, size)
}

/**
 * Retrieves the maximum body size allowed for logging in the current HTTP request.
 *
 * This method accesses the attribute associated with the key `HttpConfigHeaders.LOG_REQUEST_MAX_BODY_SIZE`
 * from the current HttpServletRequest. It can be used to dynamically obtain the maximum body size
 * configured for logging purposes.
 *
 * @receiver HttpServletRequest instance from which the attribute is fetched.
 * @return The maximum body size allowed for logging, or `null` if the attribute is not set.
 */
fun HttpServletRequest.loggingRequestMaxBodySize() =getAttribute(HttpConfigHeaders.LOG_REQUEST_MAX_BODY_SIZE) as Long?
/**
 * Sets the maximum request body size that triggers temporary file caching for logging purposes.
 *
 * @param size The maximum body size in bytes. If the request body exceeds this size during logging,
 *             it will be cached using a temporary file. A `null` value may indicate no size limit
 *             or disable this configuration depending on implementation.
 */
fun HttpServletRequest.loggingRequestMaxBodySizeToUseTempFileCaching(size: Long?) {
    setAttribute(HttpConfigHeaders.LOG_REQUEST_BODY_SIZE_TO_USE_FILE_CACHING, size)
}

/**
 * Retrieves the configured maximum body size limit for request logging that will trigger
 * the use of temporary file caching. The value is obtained from the attribute
 * `HttpConfigHeaders.LOG_REQUEST_BODY_SIZE_TO_USE_FILE_CACHING` on the `HttpServletRequest` object.
 *
 * @return The maximum body size threshold as an attribute, or `null` if not set.
 */
fun HttpServletRequest.loggingRequestMaxBodySizeToUseTempFileCaching()=getAttribute(HttpConfigHeaders.LOG_REQUEST_BODY_SIZE_TO_USE_FILE_CACHING) as Long?

/**
 * Sets a list of headers to be masked in the logging configuration for the current HTTP request.
 *
 * @param headers The list of header names to mask in logs. Can be null.
 */
fun HttpServletRequest.loggingRequestMaskHeaders(headers:  List<String>?) {
    setAttribute(HttpConfigHeaders.LOG_REQUEST_MASK_HEADERS, headers)
}

/**
 * Retrieves the masked headers configuration for logging from the current HTTP request.
 *
 * This function accesses the request attribute identified by `HttpConfigHeaders.LOG_REQUEST_MASK_HEADERS`
 * and returns it as a list of strings, if present. The returned list typically contains the names of
 * headers that are configured to be masked in logged HTTP requests to protect sensitive data.
 *
 * @return A list of header names to be masked during logging, or null if the attribute is not present.
 */
fun HttpServletRequest.loggingRequestMaskHeaders() = getAttribute(HttpConfigHeaders.LOG_REQUEST_MASK_HEADERS) as List<String>?

/**
 * Sets an attribute for the HTTP request to specify which query parameters should be masked
 * during request logging. The specified query parameters will be passed as a list and used
 * to filter out sensitive information from logs.
 *
 * @param queryParameters A list of query parameter names to be masked. If null, no masking is applied.
 */
fun HttpServletRequest.loggingRequestMaskQueryParameters(queryParameters:  List<String>?) {
    setAttribute(HttpConfigHeaders.LOG_REQUEST_MASK_QUERY_PARAMETERS, queryParameters)
}

/**
 * Retrieves a list of query parameter names that are masked during request logging.
 *
 * @receiver HttpServletRequest from which to retrieve the masking configuration.
 * @return A list of query parameter names specified for masking, or `null` if no such configuration exists.
 */
fun HttpServletRequest.loggingRequestMaskQueryParameters() = getAttribute(HttpConfigHeaders.LOG_REQUEST_MASK_QUERY_PARAMETERS) as List<String>?

/**
 * Sets a list of JSON key names to be masked in the logged request body.
 *
 * This method is used to specify sensitive keys from the JSON body of the HTTP request
 * that should be masked during logging to prevent exposure of confidential or sensitive data.
 * The provided list of key names will be stored as an attribute in the request for later retrieval.
 *
 * @param queryParameters A list of JSON key names to be masked, or null if no keys should be masked.
 */
fun HttpServletRequest.loggingRequestMaskJsonKeys(queryParameters:  List<String>?) {
    setAttribute(HttpConfigHeaders.LOG_REQUEST_MASK_JSON_BODY_KEYS, queryParameters)
}

/**
 * Retrieves a list of JSON key names that should be masked when logging the request body.
 * This function accesses the attribute identified by `HttpConfigHeaders.LOG_REQUEST_MASK_JSON_BODY_KEYS`
 * in the `HttpServletRequest` and casts it to a `List<String>?`.
 *
 * @return A list of JSON key names to be masked, or `null` if no such attribute is set.
 */
fun HttpServletRequest.loggingRequestMaskJsonKeys() = getAttribute(HttpConfigHeaders.LOG_REQUEST_MASK_JSON_BODY_KEYS) as List<String>?

/**
 * Adds a list of query parameter keys to be masked during request logging.
 * The provided list is stored as an attribute in the current HTTP request.
 *
 * @param queryParameters List of query parameter keys to be masked in the form data of the request, or null if no masking is required.
 */
fun HttpServletRequest.loggingRequestMaskFormKeys(queryParameters:  List<String>?) {
    setAttribute(HttpConfigHeaders.LOG_REQUEST_MASK_FORM_DATA_BODY_KEYS, queryParameters)
}

/**
 * Retrieves a list of form keys configured to be masked in the request logging process.
 *
 * This function accesses the `LOG_REQUEST_MASK_FORM_DATA_BODY_KEYS` attribute from the
 * current `HttpServletRequest` and casts it to a list of strings. The retrieved list
 * represents the form keys specified for masking sensitive data in the request body when logging.
 *
 * @receiver HttpServletRequest instance from which to retrieve the configured mask form keys.
 * @return A list of form keys to be masked, or null if the attribute is not set.
 */
fun HttpServletRequest.loggingRequestMaskFormKeys() = getAttribute(HttpConfigHeaders.LOG_REQUEST_MASK_FORM_DATA_BODY_KEYS) as List<String>?



/**
 * Enables or disables logging of request execution time for the current HttpServletRequest object.
 *
 * @param enable If set to true, request execution time will be logged. If set to false, request execution time logging will be disabled.
 */
fun HttpServletRequest.logResponseUri(enable: Boolean?) {
    setAttribute(HttpConfigHeaders.LOG_RESPONSE_URI, enable)
}

/**
 * Retrieves the value of the 'LOG_RESPONSE_URI' header from the HttpRequest headers and converts it to a Boolean.
 *
 * @return The Boolean value of the 'LOG_RESPONSE_URI' header, or null if the header is not present or the value cannot be converted to Boolean.
 */
fun HttpServletRequest.logResponseUri(): Boolean? = getAttribute(HttpConfigHeaders.LOG_RESPONSE_URI) as Boolean?

/**
 * Sets the value of "LOG_RESPONSE_HEADERS" attribute in the HttpServletRequest object.
 *
 * @param enable Whether to enable or disable logging of response headers. If set to true, response headers will be logged. If set to false, response headers will not be logged.
 */
fun HttpServletRequest.logResponseHeaders(enable: Boolean?) {
    setAttribute(HttpConfigHeaders.LOG_RESPONSE_HEADERS, enable)
}

/**
 * Retrieves the value of the "LOG_RESPONSE_HEADERS" header and converts it to a Boolean.
 *
 * @return The Boolean value of the "LOG_RESPONSE_HEADERS" header, or null if the header is not present or cannot be converted to Boolean.
 */
fun HttpServletRequest.logResponseHeaders(): Boolean? = getAttribute(HttpConfigHeaders.LOG_RESPONSE_HEADERS) as Boolean?

/**
 * Sets the attribute in the HttpServletRequest to enable or disable logging of the response body.
 *
 * @param enable a Boolean value indicating whether to enable or disable logging of the response body
 */
fun HttpServletRequest.logResponseBody(enable: Boolean?) {
    setAttribute(HttpConfigHeaders.LOG_RESPONSE_BODY, enable)
}

/**
 * Retrieves the value of the "log_response_body" header and converts it to a Boolean value.
 *
 * @return The Boolean value of the "log_response_body" header, or null if the header is not present or cannot be converted to Boolean.
 */
fun HttpServletRequest.logResponseBody(): Boolean? = getAttribute(HttpConfigHeaders.LOG_RESPONSE_BODY) as Boolean?

/**
 * Sets the attribute "LOG_RESPONSE_TOOK_TIME" on the HttpServletRequest object
 * to enable or disable logging of response time.
 *
 * @param enable A Boolean value indicating whether to enable or disable logging. Pass `true` to enable and `false` to disable.
 */
fun HttpServletRequest.logResponseTookTime(enable: Boolean?) {
    setAttribute(HttpConfigHeaders.LOG_RESPONSE_TOOK_TIME, enable)
}

/**
 * Retrieves the value of the "Log Response Took Time" header and converts it to a Boolean.
 *
 * @return The value of the "Log Response Took Time" header as a Boolean, or null if the header is not present or cannot be converted to a Boolean.
 */
fun HttpServletRequest.logResponseTookTime(): Boolean? = getAttribute(HttpConfigHeaders.LOG_RESPONSE_TOOK_TIME) as Boolean?

/**
 * Sets the maximum response body size for logging purposes in the current HTTP request.
 *
 * @param size the maximum size of the response body to log, in bytes. Passing `null` indicates no limit.
 */
fun HttpServletRequest.loggingResponseMaxBodySize(size: Long?) {
    setAttribute(HttpConfigHeaders.LOG_RESPONSE_MAX_BODY_SIZE, size)
}

/**
 * Retrieves the maximum response body size allowed for logging purposes from the current HTTP request's attributes.
 *
 * This method accesses the `LOG_RESPONSE_MAX_BODY_SIZE` attribute to determine the configured limit for logging
 * response body sizes. It is expected that the attribute is set by the relevant configuration logic or middleware.
 *
 * @receiver The HTTP servlet request from which the maximum response body size for logging is fetched.
 * @return The value of the maximum response body size for logging, typically as an `Any` type, or `null`
 *         if the attribute is not set.
 */
fun HttpServletRequest.loggingResponseMaxBodySize() =getAttribute(HttpConfigHeaders.LOG_RESPONSE_MAX_BODY_SIZE)
/**
 * Sets the maximum Response body size that triggers temporary file caching for logging purposes.
 *
 * @param size The maximum body size in bytes. If the Response body exceeds this size during logging,
 *             it will be cached using a temporary file. A `null` value may indicate no size limit
 *             or disable this configuration depending on implementation.
 */
fun HttpServletRequest.loggingResponseMaxBodySizeToUseTempFileCaching(size: Long?) {
    setAttribute(HttpConfigHeaders.LOG_RESPONSE_BODY_SIZE_TO_USE_FILE_CACHING, size)
}

/**
 * Updates the request attribute to specify which headers should be masked in the logging response.
 *
 * @param headers A list of header names for which the values should be masked. Can be null.
 */
fun HttpServletRequest.loggingResponseMaskHeaders(headers:  List<String>?) {
    setAttribute(HttpConfigHeaders.LOG_RESPONSE_MASK_HEADERS, headers)
}

/**
 * Retrieves a list of header names that should be masked in the logged response.
 *
 * This method accesses the `HttpServletRequest` attributes to fetch the value
 * associated with the `LOG_RESPONSE_MASK_HEADERS` key, which is expected to be
 * a list of strings representing the headers to be masked during response logging.
 *
 * @receiver The HttpServletRequest from which the attribute is retrieved.
 * @return A list of header names to be masked, or null if the attribute is not set.
 */
fun HttpServletRequest.loggingResponseMaskHeaders() = getAttribute(HttpConfigHeaders.LOG_RESPONSE_MASK_HEADERS) as List<String>?

/**
 * Adds query parameters to the request attributes for logging and masking purposes.
 *
 * @param queryParameters A list of query parameter names to be included in the response masking logic.
 *                        Can be null if no specific parameters are provided.
 */
fun HttpServletRequest.loggingResponseMaskQueryParameters(queryParameters:  List<String>?) {
    setAttribute(HttpConfigHeaders.LOG_RESPONSE_MASK_QUERY_PARAMETERS, queryParameters)
}

/**
 * Retrieves a list of query parameter names to be masked in the logging response.
 *
 * This function accesses the attribute `LOG_RESPONSE_MASK_QUERY_PARAMETERS` within the
 * `HttpServletRequest`, which is typically configured to indicate query parameters
 * that should be masked before logging for security or privacy concerns.
 *
 * @receiver HttpServletRequest instance from which query parameter masking rules are retrieved.
 * @return A list of query parameter names to be masked, or `null` if no such configuration exists.
 */
fun HttpServletRequest.loggingResponseMaskQueryParameters() = getAttribute(HttpConfigHeaders.LOG_RESPONSE_MASK_QUERY_PARAMETERS) as List<String>?

/**
 * Sets the specified JSON key names to be masked in the response body for logging purposes.
 *
 * The keys specified in the `queryParameters` will be associated with the request
 * attribute `HttpConfigHeaders.LOG_RESPONSE_MASK_JSON_BODY_KEYS` to indicate
 * which JSON keys should have their values masked when logging response data.
 *
 * @param queryParameters List of JSON key names to be masked or null if no masking is needed.
 */
fun HttpServletRequest.loggingResponseMaskJsonKeys(queryParameters:  List<String>?) {
    setAttribute(HttpConfigHeaders.LOG_RESPONSE_MASK_JSON_BODY_KEYS, queryParameters)
}

/**
 * Retrieves the list of JSON keys that are masked in the response body during logging.
 *
 * This method accesses the HttpServletRequest's attributes to fetch the specific list of keys
 * that should be masked in the response log. If no such list is defined, it returns null.
 *
 * @receiver HttpServletRequest from which the masked keys attribute is retrieved.
 * @return A list of JSON keys to be masked, or null if no keys are configured for masking.
 */
fun HttpServletRequest.loggingResponseMaskJsonKeys() = getAttribute(HttpConfigHeaders.LOG_RESPONSE_MASK_JSON_BODY_KEYS) as List<String>?

/**
 * Sets an attribute on the current HTTP servlet request to configure response masking
 * for specified form body keys in logging operations.
 *
 * @param queryParameters A list of strings representing form data keys that should be
 * masked in logged responses. If null, no specific keys will be masked.
 */
fun HttpServletRequest.loggingResponseMaskFormKeys(queryParameters:  List<String>?) {
    setAttribute(HttpConfigHeaders.LOG_RESPONSE_MASK_FORM_DATA_BODY_KEYS, queryParameters)
}

/**
 * Retrieves a list of form data keys that should be masked in logging for HTTP responses.
 *
 * The keys are stored as an attribute in the current HttpServletRequest under the
 * attribute name defined by `HttpConfigHeaders.LOG_RESPONSE_MASK_FORM_DATA_BODY_KEYS`.
 * This method casts the attribute to a list of strings or returns null if the attribute
 * is not present or not of the expected type.
 *
 * @receiver The HttpServletRequest from which the masking keys attribute is retrieved.
 * @return A list of form data keys to be masked, or null if no keys are configured.
 */
fun HttpServletRequest.loggingResponseMaskFormKeys() = getAttribute(HttpConfigHeaders.LOG_RESPONSE_MASK_FORM_DATA_BODY_KEYS) as List<String>?

