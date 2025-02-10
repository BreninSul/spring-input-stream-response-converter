BreninSul Custom Input Stream Resource Http Messages Writer

| Parameter                                                                       | Type    | Description                                    |
|---------------------------------------------------------------------------------|---------|------------------------------------------------|
| `input-stream-response-http-message-converter.enabled`                          | Boolean | Enable autoconfig for this starter             |
| `input-stream-response-http-message-converter.request-always-detect-media-type` | Boolean | Always automatically detect request media type |
| `input-stream-response-http-message-converter.request-always-detect-media-type` | Boolean | Always automatically detect request media type |




add the following dependency:

````kotlin
dependencies {
//Other dependencies
    implementation("io.github.breninsul:spring-input-stream-response-converter:1.0.6")
//Other dependencies
}
````

### Example Route:

````kotlin
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
                return@GET ServerResponse.ok()
                    .body(IOUtils.resourceToURL("1.webp").toResourceInputInputStreamResponse())
            }.build()
    }
}
````

### Example Controller:

````kotlin
package io.github.breninsul.springHttpMessageConverter.example

import io.github.breninsul.springHttpMessageConverter.inputStream.InputStreamResponse
import io.github.breninsul.springHttpMessageConverter.inputStream.toResourceInputInputStreamResponse
import org.apache.commons.io.IOUtils
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping

@Controller
class ExampleController {

    @PostMapping("/test-controller-default")
    fun exampleController(): ResponseEntity<InputStreamResponse> {
        return ResponseEntity.ok(IOUtils.resourceToURL("1.webp").toResourceInputInputStreamResponse())
    }
}
````
