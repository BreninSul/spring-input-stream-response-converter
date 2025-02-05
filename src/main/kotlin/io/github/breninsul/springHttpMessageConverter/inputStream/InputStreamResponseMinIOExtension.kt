package io.github.breninsul.springHttpMessageConverter.inputStream

import io.minio.GetObjectResponse

fun GetObjectResponse.toMinIOResource(
    mediaType: String? = null,
    resolveMediaType: Boolean = mediaType == null
): InputStreamResponse = DefaultInputStreamResponse(
    this, this.`object`()?.split('/')?.last() ?: "unknown", this.headers()["Content-Length"]?.toLong() ?: -1,
    mediaType, resolveMediaType
)

