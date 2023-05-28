package com.example.demo.service;

import com.example.demo.converter.CameraRawCameraSourceCameraTokenToCameraConverter;
import com.example.demo.model.Camera;
import com.example.demo.model.CameraRaw;
import com.example.demo.model.CameraSource;
import com.example.demo.model.CameraToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;
import static java.util.concurrent.Executors.newFixedThreadPool;

@Service
@RequiredArgsConstructor
public class CameraService {
    private static final String address = "https://www.mocky.io/v2/5c51b9dd3400003252129fb5";
    private static final WebClient webClient = WebClient.create(address);
    private final CameraRawCameraSourceCameraTokenToCameraConverter converter;

    public List<Camera> getCameras() {
        CameraRaw[] camerasRaw = webClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(CameraRaw[].class)
                .block();

        ExecutorService pool = newFixedThreadPool(requireNonNull(camerasRaw).length);
        List<Camera> camerasResult = new ArrayList<>();

        for (CameraRaw cameraRaw : camerasRaw) {
            pool.submit(() -> {
                Mono<CameraSource> cameraSourceMono = webClient
                        .get()
                        .uri(cameraRaw.getSourceDataUrl())
                        .retrieve()
                        .bodyToMono(CameraSource.class);
                Mono<CameraToken> cameraTokenMono = webClient
                        .get()
                        .uri(cameraRaw.getTokenDataUrl())
                        .retrieve()
                        .bodyToMono(CameraToken.class);

                Mono<Camera> map = Mono
                        .zip(cameraSourceMono, cameraTokenMono)
                        .map(tuple -> converter.convert(cameraRaw, tuple.getT1(), tuple.getT2()));
                camerasResult.add(map.block());
            });
        }
        pool.shutdown();
        try {
            pool.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException ignored) {
        }
        return camerasResult;
    }
}
