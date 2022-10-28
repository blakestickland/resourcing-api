package com.nology.exceptions;

public class JobDatesClashException extends RuntimeException {
    
    private String message;
    
    public JobDatesClashException() {}
    
    public JobDatesClashException(String msg) {
        super(msg);
        this.message = msg;
    }
}
