package io.github.breninsul.servlet.logging2.annotation

import io.github.breninsul.logging2.JavaLoggingLevel
import io.github.breninsul.servlet.logging2.ServletHttpRequestLogSettings

/**
 * Annotation for configuring HTTP logging settings.
 *
 * This annotation specifies the customizable options for logging
 * HTTP request and response data. It is useful for controlling the
 * granularity and content of the logs for HTTP interactions. The
 * configuration includes options for logging levels, inclusion of
 * specific request/response details, and masking sensitive information.
 *
 * @property loggingLevel Specifies the logging level to be used for this
 *    setting. This allows logging granularity control such as DEBUG, INFO,
 *    WARN, etc.
 * @property idIncluded Indicates whether the unique ID for the request
 *    should be included in the logs.
 * @property uriIncluded Indicates whether the URI of the request should be
 *    logged.
 * @property tookTimeIncluded Indicates whether the time taken to process
 *    the request/response should be included.
 * @property headersIncluded Configures whether headers should be included
 *    in the logs.
 * @property bodyIncluded Determines whether the body of the
 *    request/response should be logged or excluded.
 * @property maxBodySize Defines the maximum size of the body that can be
 *    logged. Excessive size is truncated.
 * @property mask Configures the masking rules for sensitive data, such as
 *    headers or body content, using `HttpMaskSettings`. This ensures the
 *    security of sensitive data in the logs.
 * @property bodySizeToUseTempFileCaching Configures the threshold size (in
 *    bytes) at which temporary file caching should be used for large body
 *    contents, to avoid excessive memory usage.
 */
@Retention(AnnotationRetention.RUNTIME)
annotation class HttpLogSettings(
    val loggingLevel: JavaLoggingLevel = JavaLoggingLevel.INFO,
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
    return ServletHttpRequestLogSettings(loggingLevel,idIncluded, uriIncluded, tookTimeIncluded, headersIncluded, bodyIncluded, maxBodySize,mask.toHttpMaskSettings(),bodySizeToUseTempFileCaching)
}