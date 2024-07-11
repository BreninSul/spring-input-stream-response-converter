
BreninSul Servlet interceptor with Spring Boot starter.

Logging filter implementation and stater to auto register it in Spring context




| Parameter                                                                | Type             | Description                                                    |
|--------------------------------------------------------------------------|------------------|----------------------------------------------------------------|
| `servlet.logging-interceptor.enabled`                                    | Boolean          | Enable autoconfig for this starter                             |
| `servlet.logging-interceptor.logging-level`                              | JavaLoggingLevel | Logging level of messages                                      |
| `servlet.logging-interceptor.order`                                      | Int              | Filter order (Ordered interface)                               |
| `servlet.logging-interceptor.new-line-column-symbols`                    | Int              | How many symbols in first column (param name)                  |
| `servlet.logging-interceptor.request.id-included`                        | Boolean          | Is request id included to log message (request)                |
| `servlet.logging-interceptor.request.uri-included`                       | Boolean          | Is uri included to log message (request)                       |
| `servlet.logging-interceptor.request.took-time-included`                 | Boolean          | Is timing included to log message (request)                    |
| `servlet.logging-interceptor.request.headers-included`                   | Boolean          | Is headers included to log message (request)                   |
| `servlet.logging-interceptor.request.body-included`                      | Boolean          | Is body included to log message (request)                      |
| `servlet.logging-interceptor.request.max-body-size`                      | Int              | Max logging body size   (request)                              |
| `servlet.logging-interceptor.request.body-size-to-use-temp-file-caching` | Long             | Body size to use temp file caching instead of memory (request) |
| `servlet.logging-interceptor.response.id-included`                       | Boolean          | Is request id included to log message (response)               |
| `servlet.logging-interceptor.response.uri-included`                      | Boolean          | Is uri included to log message (response)                      |
| `servlet.logging-interceptor.response.took-time-included`                | Boolean          | Is timing included to log message (response)                   |
| `servlet.logging-interceptor.response.headers-included`                  | Boolean          | Is headers included to log message (response)                  |
| `servlet.logging-interceptor.response.body-included`                     | Boolean          | Is body included to log message (response)                     |
| `servlet.logging-interceptor.response.max-body-size`                     | Int              | Max logging body size   (response)                             |


You can additionally configure logging for each request by passing headers from `io.github.breninsul.rest.logging.RestTemplateConfigHeaders` to request


add the following dependency:

````kotlin
dependencies {
//Other dependencies
    implementation("io.github.breninsul:servlet-logging-starter:1.0.2")
//Other dependencies
}

````
### Example of log messages

````
===========================Servlet Request begin===========================
=ID           : 4079-74
=URI          : POST /test
=Headers      : user-agent:PostmanRuntime/7.40.0;accept:*/*;postman-token:55500dec-8c8e-4925-abe0-350b4c1cc594;host:127.0.0.1:8080;accept-encoding:gzip, deflate, br;connection:keep-alive;content-type:multipart/form-data; boundary=--------------------------468571742219419283376118;content-length:57001384
=Body         : sosadsadasd:application/zip:<FILE 3846228 bytes>;sss:text/plain:asdsad
===========================Servlet Request end  ===========================

2024-07-11T16:50:06.489+02:00  INFO 16109 --- [nio-8080-exec-1] i.g.b.s.logging.ServletLoggingFilter     : 
===========================Servlet Response begin===========================
=ID           : 4079-74
=URI          : 200 POST /test
=Took         : 229 ms
=Headers      : TestHeader:TestVal;RQ_ID:4079-74;Content-Length:5
=Body         : TEST!
===========================Servlet Response end  ===========================
````



