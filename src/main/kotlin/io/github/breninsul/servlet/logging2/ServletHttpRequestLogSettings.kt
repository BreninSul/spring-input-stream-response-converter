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

package io.github.breninsul.servlet.logging2

import io.github.breninsul.logging2.HttpLogSettings
import io.github.breninsul.logging2.HttpMaskSettings
import io.github.breninsul.logging2.JavaLoggingLevel

open class ServletHttpRequestLogSettings(
    loggingLevel: JavaLoggingLevel = JavaLoggingLevel.INFO,
    idIncluded: Boolean = true,
    uriIncluded: Boolean = true,
    tookTimeIncluded: Boolean = true,
    headersIncluded: Boolean = true,
    bodyIncluded: Boolean = true,
    maxBodySize: Int = Int.MAX_VALUE,
    mask: HttpMaskSettings = HttpMaskSettings(),
    var bodySizeToUseTempFileCaching: Long = 1024 * 1000 * 1, // Default 1 MB
) : HttpLogSettings(loggingLevel,idIncluded, uriIncluded, tookTimeIncluded, headersIncluded, bodyIncluded, maxBodySize, mask){
    @Deprecated(message = "Use constructor with loggingLevel")
    constructor(idIncluded: Boolean,uriIncluded: Boolean,tookTimeIncluded: Boolean,headersIncluded: Boolean,bodyIncluded: Boolean,maxBodySize: Int,mask: HttpMaskSettings) : this(JavaLoggingLevel.INFO,idIncluded,uriIncluded,tookTimeIncluded,headersIncluded,bodyIncluded,maxBodySize,mask)
}
