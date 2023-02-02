package com.acem.demo.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ViberResponse {
    private Integer status;
    private String status_message;
    private String chat_hostname;
    private List<String> event_types;

    public boolean isSuccessful(){
        return status ==0;
    }
}
