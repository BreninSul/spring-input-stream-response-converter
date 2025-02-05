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