package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class CameraController {

    public static void main(String[] args) {
        ObjectMapper om = new ObjectMapper();
        String address = "https://www.mocky.io/v2/5c51b9dd3400003252129fb5";
        StringBuilder page = getPageAsStringBuilder(address);
        List<String> pageJson = makePageJsonFormat(page);
        if (pageJson != null) {
            List<CameraRaw> camerasRaw = pageJson
                    .stream()
                    .map(s -> {
                        try {
                            return om.readValue(s, CameraRaw.class);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());
            ExecutorService pool = Executors.newFixedThreadPool(4);
            List<Camera> camerasResult = new ArrayList<>();
            for (CameraRaw cameraRaw : camerasRaw) {
                pool.submit(() -> {
                    try {
                        CameraSource cameraSource = om.readValue(
                                requireNonNull(getPageAsStringBuilder(cameraRaw.getSourceDataUrl())).toString(),
                                CameraSource.class);
                        CameraToken cameraToken = om.readValue(
                                requireNonNull(getPageAsStringBuilder(cameraRaw.getTokenDataUrl())).toString(),
                                CameraToken.class);
                        Camera camera = aggregateData(cameraRaw, cameraSource, cameraToken);
                        camerasResult.add(camera);
                    } catch (JsonProcessingException ignored) {
                    }
                });
            }
            pool.shutdown();
            try {
                pool.awaitTermination(1, TimeUnit.HOURS);
            } catch (InterruptedException ignored) {
            }
            System.out.println(camerasResult);
        }
    }

    public static StringBuilder getPageAsStringBuilder(String address) {
        try {
            URL url = new URL(address);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
                String stringRead;
                StringBuilder stringWrite = new StringBuilder();
                while ((stringRead = reader.readLine()) != null) {
                    stringWrite.append(stringRead);
                }
                return stringWrite;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static List<String> makePageJsonFormat(StringBuilder page) {
        if (page != null) {
            List<String> list = new ArrayList<>();
            page.setLength(page.length() - 1);
            page.deleteCharAt(0);
            String[] split = page.toString().split("},");
            for (String s : split) {
                String trim = s.trim();
                if (trim.endsWith("\"")) {
                    String concat = s.concat("}");
                    list.add(concat);
                    continue;
                }
                list.add(s);
            }
            return list;
        }
        return null;
    }

    public static Camera aggregateData(CameraRaw cameraRaw, CameraSource cameraSource, CameraToken cameraToken) {
        return new Camera()
                .setId(cameraRaw.getId())
                .setUrlType(cameraSource.getUrlType())
                .setVideoUrl(cameraSource.getVideoUrl())
                .setValue(cameraToken.getValue())
                .setTtl(cameraToken.getTtl());
    }
}
