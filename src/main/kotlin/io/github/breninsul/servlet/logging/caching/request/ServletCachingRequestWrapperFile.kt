
package io.github.breninsul.servlet.logging.caching.request

import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.io.path.deleteIfExists

open class ServletCachingRequestWrapperFile(
    protected open val request: HttpServletRequest,
) : ServletCachingRequestWrapper,
    HttpServletRequest by request {
    val tempFile: Path

    init {
        tempFile = kotlin.io.path.createTempFile("ServletCachingRequestWrapperFile_${request.requestId}_${UUID.randomUUID()}")
        request.inputStream.toFile(tempFile.toFile())
    }

    protected open fun InputStream.toFile(file: File) {
        use { input ->
            file.outputStream().use { input.copyTo(it) }
        }
    }

    override fun bodyContentByteArray(): ByteArray? = getInputStream().readAllBytes()

    override fun clear() {
        tempFile.deleteIfExists()
    }

    override fun getInputStream(): ServletInputStream {
        val inputStream = Files.newInputStream(tempFile)
        return ServletBodyInputStreamWrapper(inputStream)
    }
}
