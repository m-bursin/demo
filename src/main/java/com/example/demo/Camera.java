package com.example.demo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class Camera {
    private Integer id;
    private String urlType;
    private String videoUrl;
    private String value;
    private Integer ttl;
}
