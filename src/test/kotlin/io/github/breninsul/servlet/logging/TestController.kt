package io.github.breninsul.servlet.logging


import io.github.breninsul.logging.JavaLoggingLevel
import io.github.breninsul.servlet.logging.annotation.HttpLogSettings
import io.github.breninsul.servlet.logging.annotation.HttpMaskSettings
import io.github.breninsul.servlet.logging.annotation.ServletLoggingFilter
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping


@Controller
class TestController {

    @PostMapping("/test-controller-default")
    fun testController(): ResponseEntity<String> {
        return ResponseEntity.ok("Test Response")
    }

    @ServletLoggingFilter(loggingLevel = JavaLoggingLevel.SEVERE,
        responseSettings = HttpLogSettings(JavaLoggingLevel.SEVERE,false,false,false,false,false,false, mask = HttpMaskSettings(ma))
    )
    @PostMapping("/test-controller-annotation")
    fun testControllerAnnotation(): ResponseEntity<String> {
        return ResponseEntity.ok("Test Response")
    }
}