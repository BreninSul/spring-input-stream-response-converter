
package io.github.breninsul.servlet.logging.caching.request

import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest


open class ServletCachingRequestWrapperByteArray(protected open val request: HttpServletRequest) : ServletCachingRequestWrapper, HttpServletRequest by request {
    fun getBody(): ByteArray =body!!
    private var body: ByteArray? =  request.inputStream.readAllBytes()
    override fun bodyContentByteArray(): ByteArray? {
        return body
    }

    override fun clear() {
        body=null
    }

    override fun getInputStream(): ServletInputStream {
        return  ServletBodyInputStreamWrapper(body!!.inputStream())
    }
}
