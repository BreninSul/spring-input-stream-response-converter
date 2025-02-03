package io.github.breninsul.servlet.logging2.examples;

import io.github.breninsul.logging2.JavaLoggingLevel;
import io.github.breninsul.servlet.logging2.annotation.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

import static io.github.breninsul.servlet.logging2.annotation.HttpBodyMaskSettingsKt.MASK_TYPE_FORM;
import static io.github.breninsul.servlet.logging2.annotation.HttpBodyMaskSettingsKt.MASK_TYPE_JSON;

@Configuration
public class ExampleJavaController {

    @ServletLoggingFilter(
            loggingLevel = JavaLoggingLevel.SEVERE,
            requestSettings = @HttpLogSettings(
                    loggingLevel = JavaLoggingLevel.SEVERE,
                    tookTimeIncluded = false,
                    mask = @HttpMaskSettings(
                            maskQueryParameters = {"queryParamToMask"},
                            maskHeaders = {"headerToMask"},
                            maskBodyKeys = {@HttpBodyMaskSettings(type = MASK_TYPE_FORM, maskKeys = {"jsonKeyToMask"})})
            ),
            responseSettings = @HttpLogSettings(
                    loggingLevel = JavaLoggingLevel.SEVERE,
                    mask = @HttpMaskSettings(
                            maskQueryParameters = {"queryParamToMask"},
                            maskHeaders = {"headerToMask"},
                            maskBodyKeys = {@HttpBodyMaskSettings(type = MASK_TYPE_JSON, maskKeys = {"jsonKeyToMask"})})
            )
    )
    @PostMapping("/example-controller-annotation-java")
    public ResponseEntity<Map<String, String>> testControllerMaskAnnotation() {
        return ResponseEntity.ok(Map.of("jsonKeyToMask", "SECRET"));
    }
}