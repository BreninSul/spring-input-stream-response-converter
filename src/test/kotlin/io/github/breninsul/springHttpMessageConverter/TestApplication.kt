package io.github.breninsul.springHttpMessageConverter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@SpringBootApplication(
    exclude = [WebSocketServletAutoConfiguration::class],
    scanBasePackageClasses = [InputStreamResourceHttpMessageConverterConfiguration::class, TestRoute::class]
)
class TestApplication {
    @Bean(name = ["InputStreamResponseHttpMessageConverter"], value = ["InputStreamResponseHttpMessageConverter"])
    fun inputStreamResponseHttpMessageConverter(properties: InputStreamResourceHttpMessageConverterProperties): InputStreamResponseHttpMessageConverter {
        return InputStreamResponseHttpMessageConverter(properties)
    }

    @Primary
    @Bean
    fun addInputStreamResourceConverterAsFirstMvcConfiguration(inputStreamResponseHttpMessageConverter: InputStreamResponseHttpMessageConverter) = AddInputStreamResourceConverterAsFirstMvcConfiguration(inputStreamResponseHttpMessageConverter)

}

fun main(args: Array<String>) {
    runApplication<TestApplication>(*args)
}
