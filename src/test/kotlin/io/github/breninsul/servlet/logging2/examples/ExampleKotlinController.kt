package io.github.breninsul.servlet.logging2.examples

import io.github.breninsul.logging2.FormBodyType
import io.github.breninsul.logging2.JavaLoggingLevel
import io.github.breninsul.logging2.JsonBodyType
import io.github.breninsul.servlet.logging2.*
import io.github.breninsul.servlet.logging2.annotation.*
import io.github.breninsul.servlet.logging2.route.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.RouterFunctions
import org.springframework.web.servlet.function.ServerResponse


@Configuration
class ExampleKotlinController {


    @ServletLoggingFilter(
        loggingLevel = JavaLoggingLevel.SEVERE,
        requestSettings = HttpLogSettings(
            loggingLevel = JavaLoggingLevel.SEVERE,
            tookTimeIncluded = false,
            mask = HttpMaskSettings(
                maskQueryParameters = ["queryParamToMask"],
                maskHeaders = ["headerToMask"],
                maskBodyKeys = [HttpBodyMaskSettings(type = MASK_TYPE_FORM, maskKeys = ["jsonKeyToMask"])]
            )
        ),
        responseSettings = HttpLogSettings(
            loggingLevel = JavaLoggingLevel.SEVERE,
            mask = HttpMaskSettings(
                maskQueryParameters = ["queryParamToMask"],
                maskHeaders = ["headerToMask"],
                maskBodyKeys = [HttpBodyMaskSettings(
                    type = MASK_TYPE_JSON,
                    maskKeys = ["jsonKeyToMask"]
                )]
            )
        )
    )
    @PostMapping("/example-controller-annotation-kotlin")
    fun testControllerMaskAnnotation(): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf("jsonKeyToMask" to "SECRET"))
    }
}