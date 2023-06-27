package com.example.demo.controller;

import com.example.demo.model.CameraFinal;
import com.example.demo.service.CameraService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@RestController
public class CameraController {
    private final CameraService cameraService;

    @GetMapping("/get-cameras")
    public Flux<CameraFinal> getCameras() {
        return cameraService.aggregateCameraDataAsynchronously();
    }
}
