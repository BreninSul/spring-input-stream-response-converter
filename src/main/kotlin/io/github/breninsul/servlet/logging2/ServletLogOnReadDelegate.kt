package io.github.breninsul.servlet.logging2

import io.github.breninsul.servlet.caching.request.ServletCachingRequestWrapper
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest


open class ServletLogOnReadDelegate(
    open val delegate: ServletCachingRequestWrapper,
    protected open val actionBeforeRead: ServletOnReadAction
) : HttpServletRequest by delegate {
    fun interface ServletOnReadAction {
        fun performAction(request: HttpServletRequest)
    }

    protected open var performed: Boolean = false
    open fun clear() {
        delegate.clear()
    }

    open fun isPerformed(): Boolean = performed
    open fun performActionIfNotPerformedBefore() {
        if (!performed) {
            actionBeforeRead.performAction(this)
            performed = true
        }
    }

    override fun getInputStream(): ServletInputStream {
        performActionIfNotPerformedBefore()
        return delegate.inputStream
    }
}


