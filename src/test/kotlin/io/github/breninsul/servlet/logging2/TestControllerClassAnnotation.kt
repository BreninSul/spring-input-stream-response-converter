package io.github.breninsul.servlet.logging2


import io.github.breninsul.logging2.JavaLoggingLevel
import io.github.breninsul.servlet.logging2.annotation.HttpLogSettings
import io.github.breninsul.servlet.logging2.annotation.ServletLoggingFilter
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping


@ServletLoggingFilter(
    loggingLevel = JavaLoggingLevel.SEVERE,
    requestSettings = HttpLogSettings(
        JavaLoggingLevel.SEVERE,
        false,
        false,
        false,
        false,
        false,
    ),
    responseSettings = HttpLogSettings(
        JavaLoggingLevel.SEVERE,
        false,
        false,
        false,
        false,
        false,
    )
)
@Controller
class TestControllerClassAnnotation {


    @ServletLoggingFilter(
        loggingLevel = JavaLoggingLevel.SEVERE,
        requestSettings = HttpLogSettings(
            JavaLoggingLevel.SEVERE,
            false,
            false,
            false,
            false,
            false,
        ),
        responseSettings = HttpLogSettings(
            JavaLoggingLevel.SEVERE,
            false,
            false,
            false,
            false,
            false,
        )
    )
    @PostMapping("/test-controller-class-annotation")
    fun testControllerClassAnnotation(): ResponseEntity<String> {
        return ResponseEntity.ok("Test Response")
    }
}