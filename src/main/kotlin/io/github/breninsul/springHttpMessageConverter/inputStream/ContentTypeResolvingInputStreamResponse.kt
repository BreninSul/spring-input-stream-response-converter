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
 * A response implementation of the InputStreamResponse interface that
 * resolves content type based on the input stream.
 *
 * This class provides a mechanism for detecting the media type of a
 * content stream if it is not explicitly provided. It also allows
 * configuration of additional metadata such as content size, content
 * disposition type, and filename return options.
 *
 * @param contentStream The input stream containing the content.
 * @param name The name associated with the content.
 * @param size Optional size of the content in bytes. Default is -1 if not
 *    specified.
 * @param mediaType Optional media type of the content. If null, it will be
 *    resolved based on content using Apache Tika.
 * @param resolveMediaType Indicates whether to resolve media type if not
 *    provided. Default is true when mediaType is null.
 * @param returnFilename Indicates whether the filename should be returned
 *    in the response. Default is false.
 * @param contentDispositionType Indicates the type of content disposition.
 *    Default is ContentDispositionType.INLINE.
 * @constructor Initializes the ContentTypeResolvingInputStreamResponse
 *    with optional parameters to determine or explicitly define the media
 *    type, size, and other response properties.
 */
open class ContentTypeResolvingInputStreamResponse(
    contentStream: InputStream,
    override val name: String,
    size: Long? = null,
    mediaType: String? = null,
    resolveMediaType: Boolean = mediaType == null,
    override val contentDispositionType: ContentDispositionType = ContentDispositionType.INLINE,
    override val returnFilename: Boolean = false,
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
