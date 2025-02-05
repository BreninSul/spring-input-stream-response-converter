package io.github.breninsul.springHttpMessageConverter


import io.github.breninsul.springHttpMessageConverter.inputStream.InputStreamResponse
import io.github.breninsul.springHttpMessageConverter.inputStream.toResourceInputInputStreamResponse
import org.apache.commons.io.IOUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.RouterFunctions
import org.springframework.web.servlet.function.ServerResponse


@Configuration
class TestRoute {


    @Bean
    fun testGetChunkedResourceRoute(): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route()
            .GET("/test-chunked-resource-route") { serverRq ->
                val toResourceInputInputStreamResponse: InputStreamResponse =
                    IOUtils.resourceToURL("1.webp", javaClass.classLoader).toResourceInputInputStreamResponse()
                return@GET ServerResponse.status(200).body(
                    toResourceInputInputStreamResponse
                )
            }.build()
    }


}