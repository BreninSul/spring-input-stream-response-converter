/*
 * MIT License
 *
 * Copyright (c) 2024 BreninSul
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.breninsul.springHttpMessageConverter.inputStream

import io.github.breninsul.io.service.stream.inputStream.CacheReadenInputStream
import org.apache.tika.Tika
import java.io.File
import java.io.InputStream
import java.net.URI
import java.net.URL
import java.util.logging.Level
import java.util.logging.Logger

/**
 * A class representing a response that provides an input stream for
 * reading content and resolves the content type if not explicitly
 * provided.
 *
 * The `ContentTypeResolvingInputStreamResponse` class leverages the Apache
 * Tika library to detect the media type of the content stream when the
 * `mediaType` parameter is null, enhancing the ability to handle streams
 * with undetermined content types.
 *
 * @constructor Creates an instance of
 *    `ContentTypeResolvingInputStreamResponse`.
 */
open class ContentTypeResolvingInputStreamResponse(
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
): InputStreamResponse = ContentTypeResolvingInputStreamResponse(
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
): InputStreamResponse = ContentTypeResolvingInputStreamResponse(this.openStream(), this.file?.split('/')?.last() ?: "unknown", -1, mediaType, resolveMediaType)

fun URI.toLocalFileInputInputStreamResponse(
    mediaType: String? = null,
    resolveMediaType: Boolean = mediaType == null
): InputStreamResponse = this.toURL().toLocalFileInputInputStreamResponse(mediaType, resolveMediaType)

fun URL.toLocalFileInputInputStreamResponse(
    mediaType: String? = null,
    resolveMediaType: Boolean = mediaType == null
): InputStreamResponse = File(this.file).toFileResource(mediaType, resolveMediaType)
