package io.github.breninsul.servlet.logging2.annotation

/**
 * Annotation for specifying masking rules for HTTP body keys during
 * logging.
 *
 * This annotation defines the type of body content (e.g., JSON or
 * form-data) and the specific keys within that body type whose values
 * should be masked in the logs. It allows for customization of sensitive
 * data masking to ensure secure handling of confidential information
 * during logging.
 *
 * @property type The type of HTTP body content, such as JSON or form-data.
 *    Common types include MASK_TYPE_JSON and MASK_TYPE_FORM.
 * @property maskKeys A list of keys in the specified body type whose
 *    values should be masked in the logs. This is intended to protect
 *    sensitive information such as tokens, passwords, or secrets.
 */
@Retention(AnnotationRetention.RUNTIME)
annotation class HttpBodyMaskSettings(
    val type: String = MASK_TYPE_JSON,
    val maskKeys: Array<String> = ["Authorization", "authorization", "token", "secret", "password", "code"]
)

const val MASK_TYPE_JSON = "json"
const val MASK_TYPE_FORM = "form"
