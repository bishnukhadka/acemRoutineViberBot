package com.acem.demo.util;

import com.acem.demo.response.Response;
import org.springframework.http.ResponseEntity;

public class ViberUtil {

    public static Boolean hasStatusCode200(ResponseEntity<String> response){
        return response.getStatusCode().toString().equals("200 OK");
    }
}
