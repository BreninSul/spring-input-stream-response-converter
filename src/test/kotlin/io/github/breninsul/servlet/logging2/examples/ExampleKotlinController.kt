package io.github.breninsul.servlet.logging2.examples

import io.github.breninsul.logging2.JavaLoggingLevel
import io.github.breninsul.servlet.logging2.annotation.*
import org.springframework.context.annotation.Configuration
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping


@Configuration
class ExampleKotlinController {


    @ServletLoggingFilter(
        loggingLevel = JavaLoggingLevel.SEVERE,
        requestSettings = HttpLogSettings(
            loggingLevel = JavaLoggingLevel.SEVERE,
            maxBodySize = Integer.MAX_VALUE,
            idIncluded = true,
            uriIncluded = true,
            tookTimeIncluded = false,
            headersIncluded = true,
            bodyIncluded = true,
            mask = HttpMaskSettings(
                maskQueryParameters = ["queryParamToMask"],
                maskHeaders = ["headerToMask"],
                maskBodyKeys = [HttpBodyMaskSettings(type = MASK_TYPE_FORM, maskKeys = ["jsonKeyToMask"])]
            )
        ),
        responseSettings = HttpLogSettings(
            loggingLevel = JavaLoggingLevel.SEVERE,
            maxBodySize = Integer.MAX_VALUE,
            idIncluded = true,
            uriIncluded = true,
            tookTimeIncluded = true,
            headersIncluded = true,
            bodyIncluded = true,
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