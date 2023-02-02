package com.acem.demo.request;

import com.acem.demo.dto.MessageDto;
import com.acem.demo.dto.SenderDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import java.util.List;

@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventJsonRequest {
  private String event;
  private Integer status;
  private String statusMessage;
  private List<String> eventTypes;
  private SenderDto sender;
  private MessageDto message;

  public SenderDto getSender() {
    return sender;
  }

  public void setSender(SenderDto sender) {
    this.sender = sender;
  }

  public MessageDto getMessage() {
    return message;
  }

  public void setMessage(MessageDto message) {
    this.message = message;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @JsonProperty("status_message")
  public String getStatusMessage() {
    return statusMessage;
  }

  public void setStatusMessage(String statusMessage) {
    this.statusMessage = statusMessage;
  }

  @JsonProperty("event_types")
  public List<String> getEventTypes() {
    return eventTypes;
  }

  public void setEventTypes(List<String> eventTypes) {
    this.eventTypes = eventTypes;
  }

  @JsonProperty("event")
  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }
}
