package com.acem.demo.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class EventJsonRequest {
  private Integer status;
  private String statusMessage;
  private List<String> eventTypes;

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
}
