package io.github.breninsul.springHttpMessageConverter.inputStream

import java.io.InputStream

interface InputStreamResponse {
    val name: String
    val contentStream: InputStream
    val contentType: String?
    val size: Long
}