package com.example.demo.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@EqualsAndHashCode
public class Camera {
    private Integer id;
    private String sourceDataUrl;
    private String tokenDataUrl;
}
