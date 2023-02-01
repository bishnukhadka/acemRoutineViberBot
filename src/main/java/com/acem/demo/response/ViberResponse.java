package com.acem.demo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ViberResponse {
    private Integer status;
    private String status_message;
    private String chat_hostname;
    private List<String> event_types;
}
