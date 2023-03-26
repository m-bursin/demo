package com.example.demo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CameraToken {
    private String value;
    private Integer ttl;
}
