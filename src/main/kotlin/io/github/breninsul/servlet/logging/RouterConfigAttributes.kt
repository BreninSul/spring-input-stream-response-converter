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
