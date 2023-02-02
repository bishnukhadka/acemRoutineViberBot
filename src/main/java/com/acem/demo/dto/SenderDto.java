package com.acem.demo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SenderDto {
    private String id;
    private String name;
    private String language;
    private String country;
}
