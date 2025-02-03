package io.github.breninsul.servlet.logging2


import io.github.breninsul.logging2.JavaLoggingLevel
import io.github.breninsul.servlet.logging2.annotation.*
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping


@Controller
class TestController {

    @PostMapping("/test-controller-default")
    fun testController(): ResponseEntity<String> {
        return ResponseEntity.ok("Test Response")
    }

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
    @PostMapping("/test-controller-annotation")
    fun testControllerAnnotation(): ResponseEntity<String> {

        return ResponseEntity.ok("Test Response")
    }

    @ServletLoggingFilter(
        loggingLevel = JavaLoggingLevel.SEVERE,
        requestSettings = HttpLogSettings(
            loggingLevel = JavaLoggingLevel.SEVERE,
            tookTimeIncluded = false,
            mask = HttpMaskSettings(maskQueryParameters = ["queryParamToMask"], maskHeaders = ["headerToMask"], maskBodyKeys = [HttpBodyMaskSettings(type = MASK_TYPE_FORM, maskKeys = ["jsonKeyToMask"])])
        ),
        responseSettings = HttpLogSettings(
            loggingLevel = JavaLoggingLevel.SEVERE,
            mask = HttpMaskSettings(maskQueryParameters = ["queryParamToMask"], maskHeaders = ["headerToMask"], maskBodyKeys = [HttpBodyMaskSettings(type = MASK_TYPE_JSON, maskKeys = ["jsonKeyToMask"])])
        )
    )
    @PostMapping("/test-controller-mask-annotation")
    fun testControllerMaskAnnotation(): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf("jsonKeyToMask" to "SECRET"))
    }
}