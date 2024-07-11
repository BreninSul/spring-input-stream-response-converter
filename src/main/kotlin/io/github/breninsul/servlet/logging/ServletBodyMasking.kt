package io.github.breninsul.servlet.logging

import io.github.breninsul.logging.HttpBodyMasking
import io.github.breninsul.logging.HttpRequestBodyMasking
import io.github.breninsul.logging.HttpResponseBodyMasking


interface ServletRequestBodyMasking : HttpRequestBodyMasking

interface ServletResponseBodyMasking : HttpResponseBodyMasking

open class ServletRequestBodyMaskingDelegate(
    protected open val delegate: HttpBodyMasking,
) : ServletRequestBodyMasking {
    override fun mask(message: String?): String = delegate.mask(message)
}
open class ServletResponseBodyMaskingDelegate(
    protected open val delegate: HttpBodyMasking
) : ServletResponseBodyMasking {
    override fun mask(message: String?): String = delegate.mask(message)
}
