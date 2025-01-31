package io.github.breninsul.servlet.logging.annotation

import io.github.breninsul.logging.JavaLoggingLevel
import io.github.breninsul.servlet.logging.ServletLoggerProperties

/**
 * Annotation to enable logging of HTTP servlet requests and responses.
 *
 * This annotation can be applied to servlet handler methods to specify logging behavior.
 * It provides options to customize logging settings for both request and response data,
 * as well as control the logging level.
 *
 * @property loggingLevel Specifies the logging level to be used.
 * @property requestSettings Configures the logging behavior for HTTP requests, such as whether
 * sensor information, headers, URI, and body information should be included.
 * @property responseSettings Configures the logging behavior for HTTP responses, similar to request settings.
 */
@Target(AnnotationTarget.FUNCTION,AnnotationTarget.CLASS,AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ServletLoggingFilter(
    val loggingLevel: JavaLoggingLevel = JavaLoggingLevel.INFO,
    val requestSettings: HttpLogSettings = HttpLogSettings(tookTimeIncluded = false),
    val responseSettings: HttpLogSettings = HttpLogSettings(),
)

fun ServletLoggingFilter.toServletLoggerProperties():ServletLoggerProperties{
    return ServletLoggerProperties(true,loggingLevel,requestSettings.toServletHttpRequestLogSettings(),responseSettings.toServletHttpRequestLogSettings())
}
