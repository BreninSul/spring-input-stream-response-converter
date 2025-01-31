package io.github.breninsul.servlet.logging.annotation

import io.github.breninsul.servlet.logging.ServletHttpRequestLogSettings

/**
 * Annotation to configure HTTP logging settings for servlet requests and responses.
 *
 * This annotation allows customization of the logging behavior, including which parts of the HTTP
 * request or response are included in the logs, the maximum body size for logging, and masking sensitive
 * information. It is designed to be paired with other annotations, enabling detailed and secure
 * logging for HTTP communication.
 *
 * @property idIncluded Indicates whether the unique request or response identifier should be included in the logs.
 * @property uriIncluded Indicates whether the URI of the request should be logged.
 * @property tookTimeIncluded Indicates whether the time taken to process the request or response should be logged.
 * @property headersIncluded Indicates whether HTTP headers should be logged.
 * @property bodyIncluded Indicates whether the body of the HTTP request or response should be logged.
 * @property maxBodySize Specifies the maximum size of the body, in bytes, to be included in the logs. Bodies
 * exceeding this size will be truncated.
 * @property mask Configures masking rules for sensitive data in headers, query parameters, and body content
 * to prevent exposure of confidential information in the logs.
 * @property bodySizeToUseTempFileCaching Specifies the threshold size, in bytes, at which the body content
 * will be temporarily cached to a file for logging, rather than stored in memory. This
 * helps manage memory usage for larger payloads.
 */
@kotlin.annotation.Target
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class HttpLogSettings(
    val idIncluded: Boolean = true,
    val uriIncluded: Boolean = true,
    val tookTimeIncluded: Boolean = true,
    val headersIncluded: Boolean = true,
    val bodyIncluded: Boolean = true,
    val maxBodySize: Int = Int.MAX_VALUE,
    val mask: HttpMaskSettings = HttpMaskSettings(),
    val bodySizeToUseTempFileCaching: Long = 1024 * 1000 * 1, // Default 1 MB
)

/**
 * Converts an instance of `HttpLogSettings` to a `ServletHttpRequestLogSettings`.
 *
 * This method maps the logging configuration defined in `HttpLogSettings`
 * to an equivalent `ServletHttpRequestLogSettings` instance to be used
 * specifically for HTTP servlet request logging.
 *
 * @return A `ServletHttpRequestLogSettings` instance containing the mapped
 * logging configuration, including included fields, max body size, masking
 * rules, and body size threshold for temporary file caching.
 */
fun HttpLogSettings.toServletHttpRequestLogSettings(): ServletHttpRequestLogSettings {
    return ServletHttpRequestLogSettings(idIncluded, uriIncluded, tookTimeIncluded, headersIncluded, bodyIncluded, maxBodySize,mask.toHttpMaskSettings(),bodySizeToUseTempFileCaching)
}