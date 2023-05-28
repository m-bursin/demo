package com.example.demo.controller;

import com.example.demo.model.Camera;
import com.example.demo.service.CameraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CameraController {
    private final CameraService cameraService;

    @GetMapping("/get-cameras")
    public ResponseEntity<List<Camera>> getCameras() {
        return ResponseEntity.ok(cameraService.getCameras());
    }


}
