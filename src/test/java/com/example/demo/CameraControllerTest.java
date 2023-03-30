package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.CameraController.aggregateData;
import static com.example.demo.CameraController.getPageAsStringBuilder;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CameraControllerTest {
    ObjectMapper om = new ObjectMapper();

    @Test
    void getFirstPageAsStringBuilder_successful() {
        CameraRaw cameraRaw1 = new CameraRaw()
                .setId(1)
                .setSourceDataUrl("http://www.mocky.io/v2/5c51b230340000094f129f5d")
                .setTokenDataUrl("http://www.mocky.io/v2/5c51b5b6340000554e129f7b?mocky-delay=1s");
        CameraRaw cameraRaw2 = new CameraRaw()
                .setId(20)
                .setSourceDataUrl("http://www.mocky.io/v2/5c51b2e6340000a24a129f5f?mocky-delay=100ms")
                .setTokenDataUrl("http://www.mocky.io/v2/5c51b5ed340000554e129f7e");
        CameraRaw cameraRaw3 = new CameraRaw()
                .setId(3)
                .setSourceDataUrl("http://www.mocky.io/v2/5c51b4b1340000074f129f6c")
                .setTokenDataUrl("http://www.mocky.io/v2/5c51b600340000514f129f7f?mocky-delay=2s");
        CameraRaw cameraRaw4 = new CameraRaw()
                .setId(2)
                .setSourceDataUrl("http://www.mocky.io/v2/5c51b5023400002f4f129f70")
                .setTokenDataUrl("http://www.mocky.io/v2/5c51b623340000404f129f82");
        List<CameraRaw> list = new ArrayList<>();
        list.add(cameraRaw1);
        list.add(cameraRaw2);
        list.add(cameraRaw3);
        list.add(cameraRaw4);

        String address = "https://www.mocky.io/v2/5c51b9dd3400003252129fb5";
        List<String> page = CameraController.makePageJsonFormat(getPageAsStringBuilder(address));
        List<CameraRaw> camerasRaw = page
                .stream()
                .map(s -> {
                    try {
                        return om.readValue(s, CameraRaw.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        assertEquals(list, camerasRaw);
    }

    @Test
    void getAllCameras_successful() {
        Camera camera1 = new Camera()
                .setId(1)
                .setUrlType("LIVE")
                .setVideoUrl("rtsp://127.0.0.1/1")
                .setValue("fa4b588e-249b-11e9-ab14-d663bd873d93")
                .setTtl(120);
        Camera camera2 = new Camera()
                .setId(20)
                .setUrlType("ARCHIVE")
                .setVideoUrl("rtsp://127.0.0.1/2")
                .setValue("fa4b5b22-249b-11e9-ab14-d663bd873d93")
                .setTtl(60);
        Camera camera3 = new Camera()
                .setId(3)
                .setUrlType("ARCHIVE")
                .setVideoUrl("rtsp://127.0.0.1/3")
                .setValue("fa4b5d52-249b-11e9-ab14-d663bd873d93")
                .setTtl(120);
        Camera camera4 = new Camera()
                .setId(2)
                .setUrlType("LIVE")
                .setVideoUrl("rtsp://127.0.0.1/20")
                .setValue("fa4b5f64-249b-11e9-ab14-d663bd873d93")
                .setTtl(180);

        String address = "https://www.mocky.io/v2/5c51b9dd3400003252129fb5";
        List<String> page = CameraController.makePageJsonFormat(getPageAsStringBuilder(address));
        List<CameraRaw> camerasRaw = page
                .stream()
                .map(s -> {
                    try {
                        return om.readValue(s, CameraRaw.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        List<Camera> camerasResult = new ArrayList<>();
        for (CameraRaw cameraRaw : camerasRaw) {
            try {
                CameraSource cameraSource = om.readValue(
                        requireNonNull(getPageAsStringBuilder(cameraRaw.getSourceDataUrl())).toString(),
                        CameraSource.class);
                CameraToken cameraToken = om.readValue(
                        requireNonNull(getPageAsStringBuilder(cameraRaw.getTokenDataUrl())).toString(),
                        CameraToken.class);
                camerasResult.add(aggregateData(cameraRaw, cameraSource, cameraToken));
            } catch (JsonProcessingException ignored) {
            }
        }
        assertEquals(camera1, camerasResult.get(0));
        assertEquals(camera2, camerasResult.get(1));
        assertEquals(camera3, camerasResult.get(2));
        assertEquals(camera4, camerasResult.get(3));
    }
}