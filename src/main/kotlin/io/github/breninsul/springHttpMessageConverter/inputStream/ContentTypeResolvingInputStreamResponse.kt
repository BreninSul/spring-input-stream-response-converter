package io.github.breninsul.springHttpMessageConverter.inputStream

import io.github.breninsul.io.service.stream.inputStream.CacheReadenInputStream
import org.apache.tika.Tika
import java.io.File
import java.io.InputStream
import java.net.URI
import java.net.URL
import java.util.logging.Level
import java.util.logging.Logger


open class DefaultInputStreamResponse(
    contentStream: InputStream,
    override val name: String,
    size: Long? = null,
    mediaType: String? = null,
    resolveMediaType: Boolean = mediaType == null
) : InputStreamResponse {
    val contentStreamValue: InputStream;
    val contentTypeValue: String?
    val sizeValue: Long

    init {
        this.sizeValue = size ?: -1
        if (resolveMediaType) {
            val time = System.currentTimeMillis()
            //cache everything what is readn while detecting content type
            val cacheStream = if (contentStream is CacheReadenInputStream) {
                contentStream
            } else {
                val tikaBufferedStreamDefaultSize = 8192
                CacheReadenInputStream(contentStream, false, tikaBufferedStreamDefaultSize)
            }
            this.contentTypeValue = try {
                tika.detect(cacheStream)
            } catch (t: Throwable) {
                logger.log(Level.FINE, "Can't resolve consent type")
                throw t
            }
            this.contentStreamValue = cacheStream.toUnreadPushbackInputStream()
            logger.log(Level.FINE, "Resolving media type took ${System.currentTimeMillis() - time} ms")
        } else {
            this.contentStreamValue = contentStream
            this.contentTypeValue = mediaType
        }
    }

    override val contentType: String? = contentTypeValue
    override val size: Long = sizeValue
    override val contentStream: InputStream = contentStreamValue


    companion object {
        protected val tika = Tika()
        protected val logger: Logger = Logger.getLogger(InputStreamResponse::class.java.name)
    }

}

fun File.toFileResource(
    mediaType: String? = null,
    resolveMediaType: Boolean = mediaType == null
): InputStreamResponse = DefaultInputStreamResponse(
    this.inputStream(),
    this.name,
    this.length(),
    mediaType,
    resolveMediaType
)

fun URI.toResourceInputInputStreamResponse(
    mediaType: String? = null,
    resolveMediaType: Boolean = mediaType == null
): InputStreamResponse = this.toURL().toResourceInputInputStreamResponse(mediaType, resolveMediaType)

fun URL.toResourceInputInputStreamResponse(
    mediaType: String? = null,
    resolveMediaType: Boolean = mediaType == null
): InputStreamResponse = DefaultInputStreamResponse(this.openStream(), this.file?.split('/')?.last() ?: "unknown", -1, mediaType, resolveMediaType)

fun URI.toLocalFileInputInputStreamResponse(
    mediaType: String? = null,
    resolveMediaType: Boolean = mediaType == null
): InputStreamResponse = this.toURL().toLocalFileInputInputStreamResponse(mediaType, resolveMediaType)

fun URL.toLocalFileInputInputStreamResponse(
    mediaType: String? = null,
    resolveMediaType: Boolean = mediaType == null
): InputStreamResponse = File(this.file).toFileResource(mediaType, resolveMediaType)
