package io.github.breninsul.servlet.logging.handlerResolver

import io.github.breninsul.servlet.logging.ServletLoggerProperties
import jakarta.servlet.http.HttpServletRequest
import java.util.*

/**
 * Defines a contract for resolving the appropriate method handler (often a controller method)
 * for a given HTTP request in a web application.
 */
interface RequestHandlerLoggingAnnotationResolver {
    /**
     * Finds and retrieves the annotation settings associated with the given HTTP request.
     * The method identifies the specific handler method corresponding to the request
     * and checks for configured logging settings, which may include details for
     * logging HTTP requests and responses.
     *
     * @param request The HttpServletRequest object representing the incoming HTTP request.
     * @return An Optional containing the ServletLoggerProperties if annotation settings are found,
     *         or an empty Optional if no settings are available for the provided request.
     */
    fun findAnnotationSettings(request: HttpServletRequest): Optional<ServletLoggerProperties>
}