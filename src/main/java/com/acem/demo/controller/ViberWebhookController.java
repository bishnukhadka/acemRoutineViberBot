package com.acem.demo.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/webhook")
public class ViberWebhookController {
    private static final String VIBER_API_URL = "https://chatapi.viber.com/pa/";

    @Value("${viber.auth.token}")
    private String VIBER_AUTH_TOKEN;

    @Value("${viber.webhook.url}")
    private String viberWebhookUrl;

    @PostMapping("/set_webhook")
    public ResponseEntity<String> setWebhook() {
        String requestUrl = VIBER_API_URL + "set_webhook";
        String requestBody = "{\"url\":\"" + viberWebhookUrl + "\",\"event_types\":[\"delivered\",\"failed\",\"subscribed\",\"unsubscribed\",\"conversation_started\"]}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Viber-Auth-Token", VIBER_AUTH_TOKEN);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);

        System.out.println(response);

        return ResponseEntity.status(HttpStatus.OK).body("Webhook set successfully.");
    }

    @GetMapping("/viber_callback")
    public ResponseEntity<String> viberCallback() {
        return ResponseEntity.status(HttpStatus.OK).body("");
    }


}
