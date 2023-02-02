package com.acem.demo.controller;

import com.acem.demo.constant.AppConstants;
import com.acem.demo.constant.ResponseMessageConstant;
import com.acem.demo.entity.enums.CourseName;
import com.acem.demo.entity.enums.DayEnum;
import com.acem.demo.entity.enums.SectionEnum;
import com.acem.demo.entity.enums.Year;
import com.acem.demo.repository.ScheduleRepository;
import com.acem.demo.request.EventJsonRequest;
import com.acem.demo.request.ScheduleRequest;
import com.acem.demo.response.Response;
import com.acem.demo.response.ViberResponse;
import com.acem.demo.util.JacksonUtil;
import com.acem.demo.util.ViberUtil;
import jakarta.annotation.PostConstruct;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.Calendar;

@RestController
@RequestMapping("/api/webhook")
public class ViberWebhookController {
    private static final String VIBER_API_URL = AppConstants.URL.VIBER_API_URL;
    @Value("${viber.auth.token}")
    private String VIBER_AUTH_TOKEN;

    @Value("${viber.webhook.url}")
    private String viberWebhookUrl;

    private final ScheduleRepository scheduleRepository;

    public ViberWebhookController(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @PostConstruct
    public ResponseEntity<String> setWebhook(){
        String requestUrl = VIBER_API_URL + "set_webhook";
        String requestBody = "{\"url\":\"" + viberWebhookUrl + "\",\"event_types\":[\"delivered" +
                "\",\"failed\",\"subscribed\",\"unsubscribed\",\"conversation_started\"]," +
                "\"send_name\": true}";
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
        if(!ViberUtil.hasStatusCode200(response))
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error during receiving response from Viber Server.");

        String json  = response.getBody();
        ViberResponse viberResponse = JacksonUtil.fromJson(json, ViberResponse.class);

        //check if the response from the viber server has status:0 or not?
        if(viberResponse.isSuccessful())
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
    public ResponseEntity<Response> handleWebhookEvent(@RequestBody String requestBody) {
        System.out.println("HandleWebhookEvent requestBody: " + requestBody);

        // Parse the request body as a JSON object
        EventJsonRequest eventJsonRequest;
        eventJsonRequest = JacksonUtil.fromJson(requestBody, EventJsonRequest.class);

        // Extract the event type from the request body
        String eventType;
        if( eventJsonRequest.getEventTypes() !=  null){
            eventType = eventJsonRequest.getEventTypes().get(0);
        }else
            eventType = eventJsonRequest.getEvent();

        System.out.println("EventJsonRequest: " + eventJsonRequest);


        // Handle the event based on its type
        switch (eventType) {
            case "message":
                return new ResponseEntity<>(handleMessageEvent(eventJsonRequest),HttpStatus.OK);
            case "subscribed":
                handleSubscribedEvent(eventJsonRequest);
                break;
            case "unsubscribed":
                handleUnsubscribedEvent(eventJsonRequest);
                break;
            case "conversation_started":
                return new ResponseEntity<>(handleConversationStartedEvent(),HttpStatus.OK);
            default:
                handleDefaultEvent(eventJsonRequest);
                break;
        }

        // Return a success response
        return new ResponseEntity<>(new Response(HttpStatus.OK, true,
                ResponseMessageConstant.Viber.HANDLED_EVENT, "N/A"), HttpStatus.OK);
    }

    private Response handleMessageEvent(EventJsonRequest eventJsonRequest) {
        System.out.println("message event");
        String receivedMessage = eventJsonRequest.getMessage().getText();

        String messageToBeSent = getMessageToBeSent(receivedMessage);
        Boolean isTextSentSuccessful = sendText(eventJsonRequest.getSender().getId(),messageToBeSent);

        return getResponse(isTextSentSuccessful, ResponseMessageConstant.Viber.MESSAGE_SENT,
                ResponseMessageConstant.Viber.MESSAGE_FAILED, messageToBeSent);
    }

    private String getMessageToBeSent(String receivedMessage) {
        String[] space = receivedMessage.split(" ");
        String[] result = space[0].split("-");
        if(space.length>1 || result.length > 4 || result.length < 3) {
            return ResponseMessageConstant.Viber.INVALID_MESSAGE;
        }

        ScheduleRequest scheduleRequest = new ScheduleRequest();
        try{
            scheduleRequest.setCourse(CourseName.valueOf(result[0].toUpperCase()).ordinal());
            scheduleRequest.setBatch(Year.valueOf(result[1].toUpperCase()).ordinal());
            scheduleRequest.setSection(SectionEnum.valueOf(result[2].toUpperCase()).ordinal());
            Calendar calendar = Calendar.getInstance();
            if(result.length==3){
                scheduleRequest.setDay(calendar.get(Calendar.DAY_OF_WEEK));
            }else{
                String temp4 = result[3].toUpperCase();
                if(temp4.equals("TODAY")) {
                    scheduleRequest.setDay(calendar.get(Calendar.DAY_OF_WEEK)-1);
                }else if(temp4.equals("TOMORROW")){
                    scheduleRequest.setDay(calendar.get(Calendar.DAY_OF_WEEK));
                }else{
                    scheduleRequest.setDay(DayEnum.valueOf(temp4).ordinal());
                }
            }
        }catch (Exception ex){
            System.out.println("Exception: " + ex.getMessage());
            return ResponseMessageConstant.Viber.INVALID_MESSAGE_OR_RESPONSE_UNAVAILABLE;
        }
         return scheduleRepository.getByCode(scheduleRequest.toSchedule().getCode()).toString();
    }

    private void handleSubscribedEvent(EventJsonRequest eventJsonRequest) {
        System.out.println("subscribed event");
    }

    private void handleUnsubscribedEvent(EventJsonRequest eventJsonRequest) {
        System.out.println("unsubscribed event");
    }

    private Response handleConversationStartedEvent() {
        System.out.println("conversation_started event");

        Boolean isTextSentSuccessful = sendConversationStartedCallback();

        return getResponse(isTextSentSuccessful, ResponseMessageConstant.Viber.CONVERSATION_STARTED_CALLBACK_SENT,
                ResponseMessageConstant.Viber.CONVERSATION_STARTED_CALLBACK_FAILED,
                "Conversation Started Event.");
    }

    private void handleDefaultEvent(EventJsonRequest eventJsonRequest) {
        System.out.println("default event");
    }

    public Boolean sendText(String recipientId, String textMessage){
        String requestUrl = AppConstants.URL.VIBER_API_URL + "send_message";

        JSONObject requestBody = new JSONObject();
        requestBody.put("receiver", recipientId);
        requestBody.put("sender", new JSONObject().put("name", "acemRoutineBot"));
        requestBody.put("tracking_data", "tracking data");
        requestBody.put("type", "text");
        requestBody.put("text", textMessage);

        ViberResponse viberResponse = getViberResponseFromResponseBody(requestBody, requestUrl);

        return viberResponse.getStatus()==0;
    }

    private ViberResponse getViberResponseFromResponseBody(JSONObject requestBody, String requestUrl){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Viber-Auth-Token", VIBER_AUTH_TOKEN);
        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, request, String.class);
        System.out.println("Message Response: " + response);
        return JacksonUtil.fromJson(response.getBody(), ViberResponse.class);
    }

    public Boolean sendConversationStartedCallback(){
        String requestUrl = AppConstants.URL.VIBER_API_URL + "conversation_started";

        JSONObject requestBody = new JSONObject();
        requestBody.put("sender", new JSONObject().put("name", "acemRoutineBot"));
        requestBody.put("tracking_data", "tracking data");
        requestBody.put("type", "text");
        requestBody.put("text", ResponseMessageConstant.Viber.WELCOME_MESSAGE);

        ViberResponse viberResponse = getViberResponseFromResponseBody(requestBody, requestUrl);

        return viberResponse.getStatus()==0;
    }

    public Response getResponse(Boolean bool, String messageWhenSuccessful,
                                String messageWhenFailed, String data){
        if(bool){
            return new Response(HttpStatus.OK, true, messageWhenSuccessful,
                    data);
        }else
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, false,
                    messageWhenFailed,
                    "N/A");
    }
}
