package io.github.breninsul.servlet.logging

import io.github.breninsul.logging.HttpConfigHeaders
import io.github.breninsul.logging.HttpConfigHeaders.TECHNICAL_HEADERS
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

/**
 * Returns the formatted query string for the current HTTP servlet request.
 * If the query string is null, an empty string is returned.
 *
 * @return the formatted query string or an empty string
 */
fun HttpServletRequest.getFormattedQueryString()= if (this.queryString == null) "" else "?${this.queryString}"

/**
 * Returns a formatted URI string by combining the request URI and query string (if it exists).
 *
 * @return The formatted URI string.
 */
fun HttpServletRequest.getFormattedUriString()= "${this.requestURI}${this.getFormattedQueryString()}"

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
 * Sets the value of the attribute `HttpConfigHeaders.LOG_REQUEST_URI` to enable or disable logging of request URI.
 *
 * @param enable Boolean value indicating whether to enable or disable logging of request URI.
 */
fun HttpServletRequest.logRequestUri(enable: Boolean?) {
    setAttribute(HttpConfigHeaders.LOG_REQUEST_URI,enable)
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
    setAttribute(HttpConfigHeaders.LOG_REQUEST_HEADERS,enable)
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
    setAttribute(HttpConfigHeaders.LOG_REQUEST_BODY,enable)
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
    setAttribute(HttpConfigHeaders.LOG_REQUEST_TOOK_TIME,enable)
}
/**
 * Retrieves the value of the LOG_REQUEST_TOOK_TIME header and converts it to a Boolean.
 *
 * @return The Boolean value of the LOG_REQUEST_TOOK_TIME header, or null if the header is not present or cannot be parsed as a Boolean.
 */
fun HttpServletRequest.logRequestTookTime(): Boolean? = getAttribute(HttpConfigHeaders.LOG_REQUEST_TOOK_TIME) as Boolean?

/**
 * Enables or disables logging of request execution time for the current HttpServletRequest object.
 *
 * @param enable If set to true, request execution time will be logged. If set to false, request execution time logging will be disabled.
 */
fun HttpServletRequest.logResponseUri(enable: Boolean?) {
    setAttribute(HttpConfigHeaders.LOG_RESPONSE_URI,enable)
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
    setAttribute(HttpConfigHeaders.LOG_RESPONSE_HEADERS,enable)
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
    setAttribute(HttpConfigHeaders.LOG_RESPONSE_BODY,enable)
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
    setAttribute(HttpConfigHeaders.LOG_RESPONSE_TOOK_TIME,enable)
}
/**
 * Retrieves the value of the "Log Response Took Time" header and converts it to a Boolean.
 *
 * @return The value of the "Log Response Took Time" header as a Boolean, or null if the header is not present or cannot be converted to a Boolean.
 */
fun HttpServletRequest.logResponseTookTime(): Boolean? = getAttribute(HttpConfigHeaders.LOG_RESPONSE_TOOK_TIME) as Boolean?
