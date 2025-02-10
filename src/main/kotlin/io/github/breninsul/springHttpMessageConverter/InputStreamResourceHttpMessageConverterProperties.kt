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


import io.github.breninsul.springHttpMessageConverter.InputStreamResponseHttpMessageConverter.Companion.DEFAULT_FLUSH_OUTPUT_STREAM_VAL
import io.github.breninsul.springHttpMessageConverter.inputStream.ContentDispositionType
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Configuration properties for the
 * InputStreamResourceHttpMessageConverter.
 *
 * These properties allow customization of the behavior of the HTTP message
 * converter responsible for handling responses with streams as their body.
 *
 * @property enabled Controls whether the
 *    InputStreamResourceHttpMessageConverter is enabled or not. Defaults
 *    to true.
 * @property mvcConfigurationEnabled Determines whether the converter is
 *    added to the Spring MVC configuration with the highest priority.
 *    Defaults to true.
 * @property flushOutputStreamBuffer Defines the buffer size used for
 *    flushing the output stream. Defaults to
 *    `DEFAULT_FLUSH_OUTPUT_STREAM_VAL`.
 * @property requestAlwaysDetectMediaType Indicates whether to always
 *    detect the media type from the HTTP request. Defaults to false.
 * @property defaultContentDispositionType Specifies the default content
 *    disposition type (e.g., inline, attachment). Defaults to
 *    `ContentDispositionType.INLINE`.
 * @property defaultAddFilename Determines whether to include the filename
 *    in the content disposition by default. Defaults to true.
 */
@ConfigurationProperties("input-stream-response-http-message-converter")
open class InputStreamResourceHttpMessageConverterProperties(
    var enabled: Boolean = true,
    var mvcConfigurationEnabled: Boolean = true,
    var flushOutputStreamBuffer: Int = DEFAULT_FLUSH_OUTPUT_STREAM_VAL,
    var requestAlwaysDetectMediaType: Boolean = false,
    var defaultContentDispositionType: ContentDispositionType = ContentDispositionType.INLINE,
    var defaultAddFilename: Boolean = true
)
