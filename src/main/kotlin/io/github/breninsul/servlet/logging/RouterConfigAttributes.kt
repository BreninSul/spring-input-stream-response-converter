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

import org.springframework.web.servlet.function.ServerRequest

fun ServerRequest.logRequestUri() = this.servletRequest().logRequestUri()

fun ServerRequest.logRequestHeaders() = this.servletRequest().logRequestHeaders()

fun ServerRequest.logRequestBody() = this.servletRequest().logRequestBody()

fun ServerRequest.logRequestTookTime() = this.servletRequest().logRequestTookTime()

fun ServerRequest.logRequestUri(enable: Boolean?) {
    this.servletRequest().logRequestUri(enable)
}

fun ServerRequest.logRequestHeaders(enable: Boolean?) {
    this.servletRequest().logRequestHeaders(enable)
}

fun ServerRequest.logRequestBody(enable: Boolean?) {
    this.servletRequest().logRequestBody(enable)
}

fun ServerRequest.logRequestTookTime(enable: Boolean?) {
    this.servletRequest().logRequestTookTime(enable)
}

fun ServerRequest.logResponseUri() = this.servletRequest().logResponseUri()

fun ServerRequest.logResponseHeaders() = this.servletRequest().logResponseHeaders()

fun ServerRequest.logResponseBody() = this.servletRequest().logResponseBody()

fun ServerRequest.logResponseTookTime() = this.servletRequest().logResponseTookTime()

fun ServerRequest.logResponseUri(enable: Boolean?) {
    this.servletRequest().logResponseUri(enable)
}

fun ServerRequest.logResponseHeaders(enable: Boolean?) {
    this.servletRequest().logResponseHeaders(enable)
}

fun ServerRequest.logResponseBody(enable: Boolean?) {
    this.servletRequest().logResponseBody(enable)
}

fun ServerRequest.logResponseTookTime(enable: Boolean?) {
    this.servletRequest().logResponseTookTime(enable)
}
