package io.github.breninsul.servlet.logging

import io.github.breninsul.logging.HttpLogSettings
import io.github.breninsul.logging.HttpMaskSettings

open class ServletHttpRequestLogSettings(
    idIncluded: Boolean = true,
    uriIncluded: Boolean = true,
    tookTimeIncluded: Boolean = true,
    headersIncluded: Boolean = true,
    bodyIncluded: Boolean = true,
    maxBodySize: Int = Int.MAX_VALUE,
    mask: HttpMaskSettings = HttpMaskSettings(),
    var bodySizeToUseTempFileCaching: Long = 1024 * 1000 * 1, // Default 1 MB
) : HttpLogSettings(idIncluded, uriIncluded, tookTimeIncluded, headersIncluded, bodyIncluded, maxBodySize, mask)
