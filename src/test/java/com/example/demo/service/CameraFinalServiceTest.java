package com.example.demo.service;

import com.example.demo.model.Source;
import com.example.demo.model.Token;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

class CameraFinalServiceTest {
    WebTestClient webTestClient = WebTestClient.bindToServer().build();

    @Test
    void retrieveJsonCameraRaw() {
        webTestClient.get().uri("https://www.mocky.io/v2/5c51b9dd3400003252129fb5")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .json("[\n" +
                        "    {\n" +
                        "        \"id\": 1,\n" +
                        "        \"sourceDataUrl\": \"http://www.mocky.io/v2/5c51b230340000094f129f5d\",\n" +
                        "        \"tokenDataUrl\": \"http://www.mocky.io/v2/5c51b5b6340000554e129f7b?mocky-delay=1s\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"id\": 20,\n" +
                        "        \"sourceDataUrl\": \"http://www.mocky.io/v2/5c51b2e6340000a24a129f5f?mocky-delay=100ms\",\n" +
                        "        \"tokenDataUrl\": \"http://www.mocky.io/v2/5c51b5ed340000554e129f7e\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"id\": 3,\n" +
                        "        \"sourceDataUrl\": \"http://www.mocky.io/v2/5c51b4b1340000074f129f6c\",\n" +
                        "        \"tokenDataUrl\": \"http://www.mocky.io/v2/5c51b600340000514f129f7f?mocky-delay=2s\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"id\": 2,\n" +
                        "        \"sourceDataUrl\": \"http://www.mocky.io/v2/5c51b5023400002f4f129f70\",\n" +
                        "        \"tokenDataUrl\": \"http://www.mocky.io/v2/5c51b623340000404f129f82\"\n" +
                        "    }\n" +
                        "]");
    }

    @Test
    void retrieveJsonCameraSource() {
        Source source = new Source()
                .setUrlType("LIVE")
                .setVideoUrl("rtsp://127.0.0.1/1");

        webTestClient.get().uri("http://www.mocky.io/v2/5c51b230340000094f129f5d")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody(Source.class)
                .isEqualTo(source);
    }

    @Test
    void retrieveJsonCameraToken() {
        Token token = new Token()
                .setValue("fa4b588e-249b-11e9-ab14-d663bd873d93")
                .setTtl(120);

        webTestClient.get().uri("http://www.mocky.io/v2/5c51b5b6340000554e129f7b?mocky-delay=1s")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody(Token.class)
                .isEqualTo(token);
    }
}