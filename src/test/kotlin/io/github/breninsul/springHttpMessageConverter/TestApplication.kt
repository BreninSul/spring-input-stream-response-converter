package io.github.breninsul.springHttpMessageConverter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(
    exclude = [WebSocketServletAutoConfiguration::class],
    scanBasePackageClasses = [InputStreamResourceHttpMessageConverterConfiguration::class, TestRoute::class]
)
class TestApplication

fun main(args: Array<String>) {
    runApplication<TestApplication>(*args)
}
