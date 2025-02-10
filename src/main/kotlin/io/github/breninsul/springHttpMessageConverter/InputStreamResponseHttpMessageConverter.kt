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

package io.github.breninsul.springHttpMessageConverter

import io.github.breninsul.springHttpMessageConverter.inputStream.ContentTypeResolvingInputStreamResponse
import io.github.breninsul.springHttpMessageConverter.inputStream.InputStreamResponse
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders.CONTENT_LENGTH
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.HttpInputMessage
import org.springframework.http.HttpOutputMessage
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter

open class InputStreamResponseHttpMessageConverter(
    protected open val flushOutputStreamVal: Int = DEFAULT_FLUSH_OUTPUT_STREAM_VAL,
    protected open val requestAlwaysDetectMediaType: Boolean = false
) : HttpMessageConverter<InputStreamResponse> {
    override fun canRead(clazz: Class<*>, mediaType: MediaType?): Boolean {
        return InputStreamResponse::class.java.isAssignableFrom(clazz)
    }

    override fun canWrite(clazz: Class<*>, mediaType: MediaType?): Boolean {
        return InputStreamResponse::class.java.isAssignableFrom(clazz)
    }

    override fun getSupportedMediaTypes(): MutableList<MediaType> {
        return listOf(MediaType.ALL).toMutableList()
    }

    override fun write(t: InputStreamResponse, contentType: MediaType?, outputMessage: HttpOutputMessage) {
        val headers = outputMessage.headers
        t.contentType?.let { headers.set(CONTENT_TYPE, it) }
        val contentDispositionBuilder = ContentDisposition.builder(t.contentDispositionType.value)
            .name(t.name)
        if (t.returnFilename) {
            contentDispositionBuilder.filename(t.name)
        }
        headers.contentDisposition = contentDispositionBuilder.build()
        if (t.size > -1) {
            headers.set(CONTENT_LENGTH, t.size.toString())
        }
        t.contentStream.use { content ->
            outputMessage.body.use { out ->
                var transferred: Long = 0
                val buffer = ByteArray(flushOutputStreamVal)
                var read: Int
                while ((content.read(buffer, 0, flushOutputStreamVal).also { read = it }) >= 0) {
                    out.write(buffer, 0, read)
                    out.flush()
                    if (transferred < Long.MAX_VALUE) {
                        transferred = try {
                            Math.addExact(transferred, read.toLong())
                        } catch (ignore: ArithmeticException) {
                            Long.MAX_VALUE
                        }
                    }
                }
            }
        }
    }

    override fun read(clazz: Class<out InputStreamResponse>, inputMessage: HttpInputMessage): InputStreamResponse {
        val size = inputMessage.headers[CONTENT_LENGTH]?.firstOrNull()?.toLong() ?: -1
        val mediaTypeHeader = inputMessage.headers[CONTENT_TYPE]?.firstOrNull()
        return ContentTypeResolvingInputStreamResponse(
            inputMessage.body,
            inputMessage.headers.contentDisposition.filename ?: "unknown",
            size,
            mediaTypeHeader,
            if (requestAlwaysDetectMediaType) true else mediaTypeHeader == null
        )
    }

    companion object {
        const val DEFAULT_FLUSH_OUTPUT_STREAM_VAL: Int = DEFAULT_BUFFER_SIZE
    }
}