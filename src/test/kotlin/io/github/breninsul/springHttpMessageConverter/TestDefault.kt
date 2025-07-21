import io.github.breninsul.springHttpMessageConverter.TestApplication
import org.apache.commons.io.IOUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


@TestPropertySource(locations = ["classpath:application-test.properties"])

@EnableWebMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = [TestApplication::class])
@ExtendWith(SpringExtension::class)
@TestConfiguration
class TestDefault {

    @Test
    fun testChunkedResourceRoute() {
        Thread.sleep(10000)
        val originalResource = IOUtils.resourceToURL("1.webp", javaClass.classLoader).readBytes()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://127.0.0.1:5367/test-chunked-resource-route"))
            .GET()
            .build();
        val httpClient = HttpClient
            .newBuilder()
            .build();
        val send = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
        assert(send.body().contentEquals(originalResource))
    }

    @Test
    fun testChunkedResourceController() {
        Thread.sleep(10000)
        val originalResource = IOUtils.resourceToURL("1.webp", javaClass.classLoader).readBytes()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://127.0.0.1:5367/test-chunked-resource-controller"))
            .GET()
            .build();
        val httpClient = HttpClient
            .newBuilder()
            .build();
        val send = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
        assert(send.body().contentEquals(originalResource))
    }

    @Test
    fun testChunkedResourceController2() {
        Thread.sleep(10000)
        val originalResource = IOUtils.resourceToURL("1.webp", javaClass.classLoader).readBytes()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://127.0.0.1:5367/test-chunked-resource-controller-2"))
            .GET()
            .build();
        val httpClient = HttpClient
            .newBuilder()
            .build();
        val send = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
        assert(send.body().contentEquals(originalResource))
    }
}