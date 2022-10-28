package com.nology.exceptions;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;

public class ApiException {
    
    private final String message;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timetamp;
    
    public ApiException(String message, 
						HttpStatus httpStatus, 
						ZonedDateTime timetamp) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.timetamp = timetamp;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ZonedDateTime getTimetamp() {
        return timetamp;
    }
    
}
