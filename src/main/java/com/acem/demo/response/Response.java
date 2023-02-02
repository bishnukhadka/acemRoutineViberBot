package com.acem.demo.response;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Response implements Serializable {
    private Integer statusCode;
    private Boolean success;
    private String description;
    private Object data;

    public Response(HttpStatus ok, boolean success, String description) {
        this.statusCode = ok.value();
        this.success = success;
        this.description = description;
    }

    public Response(HttpStatus ok, boolean success, String description, String data) {
        this.statusCode = ok.value();
        this.success = success;
        this.description = description;
        this.data = data;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public Response statusCode(Integer statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public Boolean getSuccess() {
        return success;
    }

    public Response success(Boolean success) {
        this.success = success;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Response description(String description) {
        this.description = description;
        return this;
    }

    public Object getData() {
        return data;
    }

    public Response data(Object data) {
        this.data = data;
        return this;
    }
}


