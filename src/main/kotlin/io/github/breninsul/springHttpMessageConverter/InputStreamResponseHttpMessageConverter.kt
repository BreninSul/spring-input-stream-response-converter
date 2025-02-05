package io.github.breninsul.springHttpMessageConverter

import io.github.breninsul.springHttpMessageConverter.inputStream.DefaultInputStreamResponse
import io.github.breninsul.springHttpMessageConverter.inputStream.InputStreamResponse
import org.springframework.http.HttpHeaders.CONTENT_LENGTH
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.HttpInputMessage
import org.springframework.http.HttpOutputMessage
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter

open class InputStreamResponseHttpMessageConverter(
    protected open val chunkedResponseChunkSize: Int = DEFAULT_BUFFER_SIZE,
    protected open val requestAlwaysDetectMediaType: Boolean = false,
) : HttpMessageConverter<InputStreamResponse> {
    override fun canRead(clazz: Class<*>, mediaType: MediaType?): Boolean {
        return clazz.isAssignableFrom(InputStreamResponse::class.java)
    }

    override fun canWrite(clazz: Class<*>, mediaType: MediaType?): Boolean {
        return clazz.isAssignableFrom(InputStreamResponse::class.java)
    }

    override fun getSupportedMediaTypes(): MutableList<MediaType> {
        return listOf(MediaType.ALL).toMutableList()
    }

    override fun write(t: InputStreamResponse, contentType: MediaType?, outputMessage: HttpOutputMessage) {
        val headers = outputMessage.headers
        t.contentType?.let { headers.set(CONTENT_TYPE, it) }
        if (t.size > -1) {
            headers.set(CONTENT_LENGTH, t.size.toString())
            t.contentStream.use { content ->
                outputMessage.body.use { out ->
                    content.transferTo(out)
                    out.flush()
                    out.close()
                }
            }
        } else {
            headers.set("Transfer-Encoding", "chunked")
            t.contentStream.use { content ->
                outputMessage.body.use { out ->
                    val buffer = ByteArray(chunkedResponseChunkSize)
                    var bytesRead: Int
                    while (content.read(buffer).also { bytesRead = it } != -1) {
                        val chunkSize = "${bytesRead.toString(16)}$LINE_END_SEPARATOR"
                        out.write(chunkSize.toByteArray())
                        out.write(buffer, 0, bytesRead)
                        out.write(LINE_END_SEPARATOR_BYTES)
                        out.flush()
                    }
                    out.write(LINE_END_RESPONSE) // signal end of chunks
                    out.flush()
                    out.close()
                }
            }
        }

    }

    override fun read(clazz: Class<out InputStreamResponse>, inputMessage: HttpInputMessage): InputStreamResponse {
        val size = inputMessage.headers[CONTENT_LENGTH]?.firstOrNull()?.toLong() ?: -1
        val mediaTypeHeader = inputMessage.headers[CONTENT_TYPE]?.firstOrNull()
        return DefaultInputStreamResponse(
            inputMessage.body,
            inputMessage.headers.contentDisposition.filename ?: "unknown",
            size,
            mediaTypeHeader,
            if (requestAlwaysDetectMediaType) true else mediaTypeHeader == null
        )
    }

    companion object {
        const val LINE_END_SEPARATOR = "\r\n"
        val LINE_END_SEPARATOR_BYTES = LINE_END_SEPARATOR.toByteArray()
        val LINE_END_RESPONSE = "0$LINE_END_SEPARATOR$LINE_END_SEPARATOR".toByteArray()
    }
}