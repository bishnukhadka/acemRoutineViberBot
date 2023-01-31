package com.acem.demo.controller;

import com.acem.demo.response.ViberResponse;
import com.acem.demo.util.JacksonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;


@RestController
@RequestMapping("/webhook")
public class ViberWebhookController {
    private static final String VIBER_API_URL = "https://chatapi.viber.com/pa/";

    @Value("${viber.auth.token}")
    private String VIBER_AUTH_TOKEN;

    @Value("${viber.webhook.url}")
    private String viberWebhookUrl;

    @PostMapping("/set_webhook")
    public ResponseEntity<String> setWebhook(){
        String requestUrl = VIBER_API_URL + "set_webhook";
        String requestBody = "{\"url\":\"" + viberWebhookUrl + "\",\"event_types\":[\"delivered\",\"failed\",\"subscribed\",\"unsubscribed\",\"conversation_started\"]}";
        System.out.println(requestBody);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Viber-Auth-Token", VIBER_AUTH_TOKEN);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);

        //check if viber webhook check performed or not.
        //viber responds with status code 200 if checked correctly.
        if(response.getStatusCode().equals(200)){
            System.out.println("Viber check completed successfully");
        }else
            System.out.println("Problem while checking webhook availability by viber.");

        String json  = response.getBody();

        ViberResponse viberResponse = JacksonUtil.fromJson(json, ViberResponse.class);

        //check if the response from the viber server has status:0 or not?
        if(viberResponse.getStatus()==0)
            return ResponseEntity.status(HttpStatus.OK).body(json+"\nWebhook set successfully.");
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(json);
    }

    @GetMapping("/viber_callback")
    public ResponseEntity<String> viberCallback() {
        return ResponseEntity.status(HttpStatus.OK).body("Viber webhook callback response.");
    }

    @PostMapping
    public ResponseEntity<String> handleWebhookEvent(@RequestBody String requestBody) {
        // Parse the request body as a JSON object
        ObjectMapper mapper = new ObjectMapper();
        JsonNode eventData;
        try {
            eventData = mapper.readTree(requestBody);
        } catch (IOException e) {
            return new ResponseEntity<>("Error parsing request body as JSON", HttpStatus.BAD_REQUEST);
        }

        // Extract the event type from the request body
        String eventType = eventData.get("event").asText();

        // Handle the event based on its type
        switch (eventType) {
            case "message":
                // Handle a message event
                System.out.println("message event");
                break;
            case "subscribed":
                // Handle a subscribed event
                System.out.println("subscribed event");
                break;
            case "unsubscribed":
                // Handle an unsubscribed event
                System.out.println("unsubscribed event");
                break;
            default:
                // Handle any other unknown event types
                System.out.println("default event");
                break;
        }

        // Return a success response
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
