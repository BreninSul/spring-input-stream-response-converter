package io.github.breninsul.servlet.logging.caching.request

import jakarta.servlet.http.HttpServletRequest
import java.nio.charset.Charset

interface ServletCachingRequestWrapper : HttpServletRequest {
    fun bodyContentByteArray(): ByteArray?
    fun clear()

    fun bodyContentString(): String? {
        val byteArray = bodyContentByteArray()
        if (byteArray == null) {
            return null
        }
        return String(byteArray,getContentEncoding())
    }

    fun getContentEncoding(): Charset = characterEncoding?.let { Charset.forName(characterEncoding) } ?: Charset.defaultCharset()
}
