package io.github.breninsul.servlet.logging2.test.controller

import io.github.breninsul.servlet.logging2.TestApplication
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.text.Charsets.UTF_8

@TestPropertySource(locations = ["classpath:application-test.properties"])


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = [TestApplication::class])
@ExtendWith(SpringExtension::class)
@TestConfiguration
class ControllerTestAnnotationTimeTook {
    @Test
    fun test1000Times() {
        repeat(1000) {
            testLogEmptyInfoRequest()
        }
    }

    @Test
    fun testLogEmptyInfoRequest() {
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://127.0.0.1:5367/test-controller-annotation"))
            .POST(HttpRequest.BodyPublishers.ofString("Test Request", UTF_8))
            .build();
        val httpClient = HttpClient
            .newBuilder()
            .build();
        val send = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

}