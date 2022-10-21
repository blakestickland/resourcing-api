package com.nology.exceptions;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.*;

public class ResourceNotFoundException extends ResponseStatusException {

	  private static final long serialVersionUID = 1L;

	  public ResourceNotFoundException(String message) {
	      super(HttpStatus.NOT_FOUND, message);
	  }
}
