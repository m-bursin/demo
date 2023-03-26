package com.example.demo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CameraRaw {
    private Integer id;
    private String sourceDataUrl;
    private String tokenDataUrl;
}
