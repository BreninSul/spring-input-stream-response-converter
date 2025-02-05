BreninSul Custom Input Stream Resource Http Messages Writer

| Parameter                                                                       | Type    | Description                                    |
|---------------------------------------------------------------------------------|---------|------------------------------------------------|
| `input-stream-response-http-message-converter.enabled`                          | Boolean | Enable autoconfig for this starter             |
| `input-stream-response-http-message-converter.chunked-response-chunk-size`      | Long    | Chunked responses chunk sizes                  |
| `input-stream-response-http-message-converter.request-always-detect-media-type` | Boolean | Always automatically detect request media type |




add the following dependency:

````kotlin
dependencies {
//Other dependencies
    implementation("io.github.breninsul:spring-input-stream-response-converter:1.0.0")
//Other dependencies
}





