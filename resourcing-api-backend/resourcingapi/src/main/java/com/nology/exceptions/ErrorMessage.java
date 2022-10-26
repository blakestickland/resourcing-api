package com.nology.exceptions;

import java.util.Date;
import java.util.List;

public class ErrorMessage {
  private int statusCode;
  private Date timestamp;
  private String message;
  private String description;
  private List<String> details;

  public ErrorMessage(int statusCode, Date timestamp, String message, String description) {
    this.statusCode = statusCode;
    this.timestamp = timestamp;
    this.message = message;
    this.description = description;
  }
  
  public ErrorMessage(int statusCode, Date timestamp, String message, String description, List<String> details) {
      this.statusCode = statusCode;
      this.timestamp = timestamp;
      this.message = message;
      this.description = description;
      this.details = details;
    }

  public int getStatusCode() {
    return statusCode;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public String getMessage() {
    return message;
  }

  public String getDescription() {
    return description;
  }
  
  public List<String> getDetails() {
      return details;
  }
}