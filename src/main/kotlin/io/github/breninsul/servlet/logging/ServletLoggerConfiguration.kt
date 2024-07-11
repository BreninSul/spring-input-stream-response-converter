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

import io.github.breninsul.logging.HttpMaskSettings
import io.github.breninsul.logging.HttpRegexFormUrlencodedBodyMasking
import io.github.breninsul.logging.HttpRegexJsonBodyMasking
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.web.servlet.ConditionalOnMissingFilterBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.boot.web.servlet.server.ServletWebServerFactory
import org.springframework.context.annotation.Bean


@ConditionalOnProperty(value = ["servlet.logging-interceptor.enabled"], havingValue = "true", matchIfMissing = true)
@AutoConfiguration
@EnableConfigurationProperties(ServletLoggerProperties::class)
open class ServletLoggerConfiguration {

    @Bean(name = ["ServletLoggingFilter"], value = ["ServletLoggingFilter"])
    @ConditionalOnClass(ServletWebServerFactory::class)
    @ConditionalOnMissingFilterBean(ServletLoggingFilter::class)
    @ConditionalOnMissingBean(ServletLoggingFilter::class)
    fun loggingFilter(
        properties: ServletLoggerProperties,
    ): FilterRegistrationBean<ServletLoggingFilter> {
        val requestMaskers= listOf(
            servletRequestRegexJsonBodyMasking(properties.request.mask),
            servletRequestFormUrlencodedBodyMasking(properties.request.mask)
        )
        val responseMaskers= listOf(
            servletResponseRegexJsonBodyMasking(properties.request.mask),
            servletResponseFormUrlencodedBodyMasking(properties.request.mask)
        )
        val registrationBean = FilterRegistrationBean<ServletLoggingFilter>()
        val servletLoggingFilter = ServletLoggingFilter(properties, requestMaskers, responseMaskers)
        registrationBean.filter = servletLoggingFilter
        registrationBean.order = servletLoggingFilter.order
        return registrationBean
    }


    fun servletRequestRegexJsonBodyMasking(properties: HttpMaskSettings): ServletRequestBodyMasking {
        return ServletRequestBodyMaskingDelegate(HttpRegexJsonBodyMasking(properties.maskJsonBodyKeys))
    }


    fun  servletResponseRegexJsonBodyMasking(properties: HttpMaskSettings): ServletResponseBodyMasking {
        return ServletResponseBodyMaskingDelegate(HttpRegexJsonBodyMasking(properties.maskJsonBodyKeys))
    }


    fun servletRequestFormUrlencodedBodyMasking(properties: HttpMaskSettings): ServletRequestBodyMasking {
        return ServletRequestBodyMaskingDelegate(HttpRegexFormUrlencodedBodyMasking(properties.maskJsonBodyKeys))
    }

    fun  servletResponseFormUrlencodedBodyMasking(properties: HttpMaskSettings): ServletResponseBodyMasking {
        return ServletResponseBodyMaskingDelegate(HttpRegexFormUrlencodedBodyMasking(properties.maskJsonBodyKeys))
    }
}