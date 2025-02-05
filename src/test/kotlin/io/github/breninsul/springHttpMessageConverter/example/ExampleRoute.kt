package io.github.breninsul.springHttpMessageConverter.example

import io.github.breninsul.springHttpMessageConverter.inputStream.toResourceInputInputStreamResponse
import org.apache.commons.io.IOUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.RouterFunctions
import org.springframework.web.servlet.function.ServerResponse

@Configuration
class ExampleRoute {
    @Bean
    fun exampleResourceRoute(): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route()
            .GET("/example-route") { serverRq ->
                return@GET ServerResponse.ok().body(IOUtils.resourceToURL("1.webp").toResourceInputInputStreamResponse())
            }.build()
    }
}