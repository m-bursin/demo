package com.example.demo.converter;

import com.example.demo.model.CameraFinal;
import com.example.demo.model.Camera;
import com.example.demo.model.Source;
import com.example.demo.model.Token;
import org.springframework.stereotype.Component;

@Component
public class CameraSourceTokenToCameraFinalConverter {
    public CameraFinal convert(Camera camera, Source source, Token token) {
        return new CameraFinal()
                .setId(camera.getId())
                .setUrlType(source.getUrlType())
                .setVideoUrl(source.getVideoUrl())
                .setValue(token.getValue())
                .setTtl(token.getTtl());
    }
}
