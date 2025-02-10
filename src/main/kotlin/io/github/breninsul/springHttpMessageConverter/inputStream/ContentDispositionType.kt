package io.github.breninsul.springHttpMessageConverter.inputStream

enum class ContentDispositionType(val value: String) {
    ATTACHMENT("attachment"),
    FORM_DATA("form-data"),
    INLINE("inline");
}
