package com.example.demo.converter;

import com.example.demo.model.Camera;
import com.example.demo.model.CameraRaw;
import com.example.demo.model.CameraSource;
import com.example.demo.model.CameraToken;
import org.springframework.stereotype.Component;

@Component
public class CameraRawCameraSourceCameraTokenToCameraConverter {
    public Camera convert(CameraRaw cameraRaw, CameraSource cameraSource, CameraToken cameraToken) {
        return new Camera()
                .setId(cameraRaw.getId())
                .setUrlType(cameraSource.getUrlType())
                .setVideoUrl(cameraSource.getVideoUrl())
                .setValue(cameraToken.getValue())
                .setTtl(cameraToken.getTtl());
    }
}
