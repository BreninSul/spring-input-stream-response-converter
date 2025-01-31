package io.github.breninsul.servlet.logging.handlerResolver

import io.github.breninsul.servlet.logging.ServletLoggerProperties
import io.github.breninsul.servlet.logging.annotation.ServletLoggingFilter
import io.github.breninsul.servlet.logging.annotation.toServletLoggingFilter
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerExecutionChain
import org.springframework.web.servlet.HandlerMapping
import java.lang.reflect.Method
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

open class DefaultRequestHandlerLoggingAnnotationResolver(protected open val handlerMappings: List<HandlerMapping>):RequestHandlerLoggingAnnotationResolver {
    protected open val logger: Logger = Logger.getLogger(DefaultRequestHandlerLoggingAnnotationResolver::class.java.name)
    public override fun findAnnotationSettings(request: HttpServletRequest):Optional<ServletLoggerProperties> {
        val handlerMethod = findHandlerMethod(request)
        val annotation= handlerMethod?.getDeclaredAnnotation(ServletLoggingFilter::class.java)
        return Optional.ofNullable(annotation?.toServletLoggingFilter())
    }

    protected open fun findHandlerMethod(request: HttpServletRequest): Method? {
        return handlerMappings
            .asSequence()
            .mapNotNull { handles(request, it).orElse(null)?.handler }
            .filterIsInstance<HandlerMethod>()
            .map { it.method }
            .firstOrNull()
    }

    protected open fun handles(request: HttpServletRequest, mapping: HandlerMapping): Optional<HandlerExecutionChain> {
        return try {
            Optional.ofNullable(mapping.getHandler(request))
        } catch (e: Throwable) {
            logger.log(Level.SEVERE, "Error getting handler", e)
            Optional.empty()
        }
    }

}