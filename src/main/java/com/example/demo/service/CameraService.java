package com.example.demo.service;

import com.example.demo.converter.CameraSourceTokenToCameraFinalConverter;
import com.example.demo.model.Camera;
import com.example.demo.model.CameraFinal;
import com.example.demo.model.Source;
import com.example.demo.model.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CameraService {
    private final WebClient webClient;
    private final CameraSourceTokenToCameraFinalConverter converter;

    public Flux<CameraFinal> aggregateCameraDataAsynchronously() {
        return webClient.get()
                .retrieve()
                .bodyToFlux(Camera.class)
                .flatMap(camera -> {
                    Mono<Source> cameraSourceMono = webClient
                            .get()
                            .uri(camera.getSourceDataUrl())
                            .retrieve()
                            .bodyToMono(Source.class);
                    Mono<Token> cameraTokenMono = webClient
                            .get()
                            .uri(camera.getTokenDataUrl())
                            .retrieve()
                            .bodyToMono(Token.class);

                    return Mono
                            .zip(cameraSourceMono, cameraTokenMono)
                            .map(data -> converter.convert(camera, data.getT1(), data.getT2()));
                });
    }
}
