package io.github.breninsul.servlet.logging

import io.github.breninsul.servlet.logging.annotation.ServletLoggingFilter
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.RouterFunctions
import org.springframework.web.servlet.function.ServerResponse
import java.io.ByteArrayOutputStream
import java.io.PrintStream


@Configuration
class TestRoute {



    @Bean
    fun testPostRoute(): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route()
            .POST("/test-route") { serverRq ->
                return@POST ServerResponse.ok().body("Test Response")
            }.build()
    }
    @Bean
    @ServletLoggingFilter()
    fun testPostRoute2(): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route()
            .POST("/test-route-2") { serverRq ->
                return@POST ServerResponse.ok().body("Test Response")
            }.build()
    }
}