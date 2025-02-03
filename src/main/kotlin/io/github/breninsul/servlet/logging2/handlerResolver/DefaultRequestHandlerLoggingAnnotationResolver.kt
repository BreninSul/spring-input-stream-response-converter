package io.github.breninsul.servlet.logging2.handlerResolver

import io.github.breninsul.servlet.logging2.ServletLoggerProperties
import io.github.breninsul.servlet.logging2.annotation.ServletLoggingFilter
import io.github.breninsul.servlet.logging2.annotation.toServletLoggerProperties
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerExecutionChain
import org.springframework.web.servlet.HandlerMapping
import org.springframework.web.servlet.function.support.RouterFunctionMapping
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import java.lang.reflect.Method
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

open class DefaultRequestHandlerLoggingAnnotationResolver(protected open val handlerMappings: List<HandlerMapping>):RequestHandlerLoggingAnnotationResolver {
    protected open val routerMappings = handlerMappings.filterIsInstance<RouterFunctionMapping>()
    protected open val controllerMappings = handlerMappings.filterIsInstance<RequestMappingHandlerMapping>()

    protected open val logger: Logger = Logger.getLogger(DefaultRequestHandlerLoggingAnnotationResolver::class.java.name)
    protected open val debugLoggingLevel = Level.FINEST
    override fun findAnnotationSettings(request: HttpServletRequest):Optional<ServletLoggerProperties> {
        val time = System.nanoTime()
        val handlerMethod = findHandlerMethod(request)
        val annotation = handlerMethod
            //Get anno from method
            ?.getAnnotation(ServletLoggingFilter::class.java)
        //Or class
            ?: handlerMethod?.declaringClass?.getAnnotation(ServletLoggingFilter::class.java)
        val result = Optional.ofNullable(annotation?.toServletLoggerProperties())
        logger.log(debugLoggingLevel, "Resolving controller took ${System.nanoTime() - time} ns")
        return result
    }

    override fun isRouterMapping(request: HttpServletRequest): Boolean {
        val time = System.nanoTime()
        val anyRouter = routerMappings.asSequence().mapNotNull { handles(request, it).orElse(null)?.handler }.any()
        logger.log(debugLoggingLevel, "Resolving router took ${System.nanoTime() - time} ns")
        return anyRouter
    }

    protected open fun findHandlerMethod(request: HttpServletRequest): Method? {
        val matchingHandlers = controllerMappings
            .asSequence()
            .mapNotNull { handles(request, it).orElse(null)?.handler }
        val controllers=matchingHandlers.filterIsInstance<HandlerMethod>()
        //Routes are instance of HttpRequestHandler
        return controllers
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