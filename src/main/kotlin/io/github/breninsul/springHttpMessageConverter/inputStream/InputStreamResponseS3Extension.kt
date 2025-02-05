package io.github.breninsul.springHttpMessageConverter.inputStream

import software.amazon.awssdk.core.ResponseInputStream
import software.amazon.awssdk.services.s3.model.GetObjectResponse


fun ResponseInputStream<GetObjectResponse>.toS3Resource(
    s3Key: String?,
    mediaType: String? = null,
    resolveMediaType: Boolean = mediaType == null
): InputStreamResponse = DefaultInputStreamResponse(
    this,
    s3Key?.split('/')?.last() ?: "unknown",
    this.response().contentLength() ?: -1,
    mediaType, resolveMediaType
)
