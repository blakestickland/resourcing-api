package com.nology.resourcingapi.exception;

public class FieldInvalidError {
	
    private String message;

    public FieldInvalidError(String message) {
        this.message = message;
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
    
}