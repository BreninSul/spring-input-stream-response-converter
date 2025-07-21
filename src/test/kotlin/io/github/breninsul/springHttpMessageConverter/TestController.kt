package io.github.breninsul.springHttpMessageConverter


import io.github.breninsul.springHttpMessageConverter.inputStream.InputStreamResponse
import io.github.breninsul.springHttpMessageConverter.inputStream.toInputStreamResponse
import io.github.breninsul.springHttpMessageConverter.inputStream.toResourceInputInputStreamResponse
import org.apache.commons.io.IOUtils
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class TestController {

    @GetMapping("/test-chunked-resource-controller")
    fun testController(): ResponseEntity<InputStreamResponse> {
        val toResourceInputInputStreamResponse: InputStreamResponse =
            IOUtils.resourceToURL("1.webp", javaClass.classLoader).toResourceInputInputStreamResponse()
        return ResponseEntity.ok(toResourceInputInputStreamResponse)
    }

    @GetMapping("/test-chunked-resource-controller-2")
    fun testController2(): ResponseEntity<InputStreamResponse> {
        val inputInputStreamResponse: InputStreamResponse =
            IOUtils.resourceToURL("1.webp", javaClass.classLoader).openStream().toInputStreamResponse("1.webp")
        return ResponseEntity.ok(inputInputStreamResponse)
    }
}
