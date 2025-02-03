package io.github.breninsul.servlet.logging2.annotation

import io.github.breninsul.logging2.HttpBodyType

/**
 * Annotation for configuring masking rules for sensitive data during HTTP
 * logging.
 *
 * This annotation provides the ability to specify which HTTP headers,
 * query parameters, and body keys should have their values masked in the
 * logs to prevent exposure of confidential or sensitive information.
 *
 * @property maskHeaders Specifies the list of HTTP headers whose values
 *    should be masked in the logs.
 * @property maskQueryParameters Specifies the list of query parameter keys
 *    whose values should be masked in the logs.
 * @property maskJsonBodyKeys Specifies the list of JSON body keys whose
 *    values should be masked in the logs.
 * @property maskFormBodyKeys Specifies the list of keys in form-urlencoded
 *    body content whose values should be masked in the logs.
 */
@Retention(AnnotationRetention.RUNTIME)
annotation class HttpMaskSettings(
    val maskHeaders: Array<String> = ["Authorization"],
    val maskQueryParameters: Array<String> = ["Authorization", "authorization", "token", "secret", "password", "code"],
    val maskBodyKeys: Array<HttpBodyMaskSettings> = [HttpBodyMaskSettings(type = MASK_TYPE_JSON, maskKeys = ["Authorization", "authorization", "token", "secret", "password", "code"]), HttpBodyMaskSettings(
        type = MASK_TYPE_FORM,
        maskKeys = ["Authorization", "authorization", "token", "secret", "password", "code"]
    )],
)

/**
 * Converts an instance of `HttpMaskSettings` to the equivalent
 * `io.github.breninsul.logging2.HttpMaskSettings` representation.
 *
 * This method is used to map the masking configuration for HTTP
 * logging from the annotation-based `HttpMaskSettings` to the
 * implementation-specific representation used by the logging framework. It
 * populates the settings for masking headers, query parameters, JSON body
 * keys, and form-urlencoded body keys.
 *
 * @return An instance of `io.github.breninsul.logging2.HttpMaskSettings`
 *    containing the mapped masking configuration.
 */
fun HttpMaskSettings.toHttpMaskSettings(): io.github.breninsul.logging2.HttpMaskSettings {
    return io.github.breninsul.logging2.HttpMaskSettings(
        maskHeaders.toList(),
        maskQueryParameters.toList(),
        maskBodyKeys = maskBodyKeys.associate { k -> HttpBodyType(k.type) to k.maskKeys.toList() },
    )
}