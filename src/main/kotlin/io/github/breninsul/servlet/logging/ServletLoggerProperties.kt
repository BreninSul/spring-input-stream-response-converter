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

package io.github.breninsul.servlet.logging

import io.github.breninsul.logging.HttpLogSettings
import io.github.breninsul.logging.HttpLoggingProperties
import io.github.breninsul.logging.JavaLoggingLevel
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Class representing the properties for configuring the ServletLogger.
 *
 * @param enabled Whether the ServletLogger is enabled. Default is true.
 * @param loggingLevel The logging level for the ServletLogger. Default is INFO.
 * @param requestRaw The settings for logging the raw request. Default settings are used if not specified.
 * @param response The settings for logging the response. Default settings are used if not specified.
 * @param order The order in which the ServletLogger should be executed. Default is 0.
 * @param newLineColumnSymbols The number of symbols used for displaying new line characters in logs. Default is 14.
 */
@ConfigurationProperties("servlet.logging-interceptor")
open class ServletLoggerProperties(
    var enabled: Boolean = true,
    var loggingLevel: JavaLoggingLevel = JavaLoggingLevel.INFO,
    var request: ServletHttpRequestLogSettings = ServletHttpRequestLogSettings(tookTimeIncluded = false),
    var response: HttpLogSettings = HttpLogSettings(),
    var order: Int = 0,
    var newLineColumnSymbols: Int = 14,
) {
    open fun toHttpLoggingProperties() = HttpLoggingProperties(enabled, loggingLevel, request, response, order, newLineColumnSymbols)
}
