package com.acem.demo.controller;

import com.acem.demo.request.EventJsonRequest;
import com.acem.demo.response.ViberResponse;
import com.acem.demo.util.JacksonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<String> setWebhook(){
        String requestUrl = VIBER_API_URL + "set_webhook";
        String requestBody = "{\"url\":\"" + viberWebhookUrl + "\",\"event_types\":[\"delivered\",\"failed\",\"subscribed\",\"unsubscribed\",\"conversation_started\"]}";
        System.out.println("Request Body: " + requestBody);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Viber-Auth-Token", VIBER_AUTH_TOKEN);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
        System.out.println("Viber response: " + response);
        System.out.println("Viber Response body" + response.getBody());

        System.out.println("Viber response status code: " + response.getStatusCode());

        //check if viber webhook check performed or not.
        //viber responds with status code 200 if checked correctly.
        if(response.getStatusCode().toString().equals("200 OK")){
            System.out.println("Viber check completed successfully");
        }else
            System.out.println("Problem while checking webhook availability by viber.");

        String json  = response.getBody();

        ViberResponse viberResponse = JacksonUtil.fromJson(json, ViberResponse.class);

        //check if the response from the viber server has status:0 or not?
        if(viberResponse.getStatus()==0)
            return ResponseEntity.status(HttpStatus.OK).body("Webhook set successfully.");
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(json);
    }

    @GetMapping("/set_webhook")
    public ResponseEntity<String> viberCallback() {
        return ResponseEntity.status(HttpStatus.OK).body("Viber webhook callback response.");
    }

    @PostMapping
    public ResponseEntity<String> handleWebhookEvent(@RequestBody String requestBody) {
        System.out.println("HandleWebhookEvent requestBody: " + requestBody);

        // Parse the request body as a JSON object
        EventJsonRequest eventJsonRequest;
        eventJsonRequest = JacksonUtil.fromJson(requestBody, EventJsonRequest.class);

        // Extract the event type from the request body
        String eventType = eventJsonRequest.getEventTypes().get(0);

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
            case "conversation_started":
                //Handle conversation_started event
                System.out.println("conversation_started event");
            default:
                // Handle any other unknown event types
                System.out.println("default event");
                break;
        }

        // Return a success response
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}
