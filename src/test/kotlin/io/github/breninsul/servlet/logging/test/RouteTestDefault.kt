package io.github.breninsul.servlet.logging.test

import io.github.breninsul.servlet.logging.TestApplication
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.text.Charsets.UTF_8

@TestPropertySource(locations = ["classpath:application-test.properties"])


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = [TestApplication::class])
@ExtendWith(SpringExtension::class)
@TestConfiguration
class RouteTestDefault {

    @Test
    fun testLogFullInfoRequest(){
        val  request = HttpRequest.newBuilder()
            .uri(URI.create("http://127.0.0.1:5367/test-route"))
            .POST(HttpRequest.BodyPublishers.ofString("Test Request", UTF_8))
            .build();
        val httpClient = HttpClient
                .newBuilder()
            .build();
        var baos: ByteArrayOutputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(baos));
        val send = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        val lines=baos.toString(UTF_8.name()).lines()
        assert(lines.filter { it.contains("i.g.b.s.logging.ServletLoggingFilter") }.any { it.contains("INFO") })
        val firstLine=lines.indexOf("===========================Servlet Request begin===========================" )
        val realLines=lines.subList(firstLine,lines.size)
        assert(realLines[1].startsWith("=ID"))
        assert(realLines[2].startsWith("=URI          : POST /test-route"))
        assert(realLines[3].startsWith("=Headers      :"))
        assert(realLines[4].startsWith("=Body         : Test Request"))
    }

    @Test
    fun testLogFullInfoResponse(){
        val  request = HttpRequest.newBuilder()
            .uri(URI.create("http://127.0.0.1:5367/test-route"))
            .POST(HttpRequest.BodyPublishers.ofString("Test Request", UTF_8))
            .build();
        val httpClient = HttpClient
            .newBuilder()
            .build();
        var baos: ByteArrayOutputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(baos));
        val send = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        val lines=baos.toString(UTF_8.name()).lines()
        val firstLine=lines.indexOf("===========================Servlet Response begin===========================" )
        val realLines=lines.subList(firstLine,lines.size)
        assert(realLines[1].startsWith("=ID"))
        assert(realLines[2].startsWith("=URI          : 200 POST /test-route"))
        assert(realLines[3].startsWith("=Took         :"))
        assert(realLines[4].startsWith("=Headers      :"))
        assert(realLines[5].startsWith("=Body         : Test Response"))
    }
}