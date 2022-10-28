package com.nology.exceptions;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ApiExceptionHandler {
   
    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
      ErrorMessage message = new ErrorMessage(
          HttpStatus.NOT_FOUND.value(),
          new Date(),
          ex.getMessage(),
          request.getDescription(false));
      
      return message;
    }
    
    @ExceptionHandler(value = JobDatesClashException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorMessage handleJobDatesClashException(JobDatesClashException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
            HttpStatus.CONFLICT.value(),
            new Date(),
            ex.getMessage(),
            request.getDescription(false));
        
        return message;
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleInvalidArgument( MethodArgumentNotValidException ex, WebRequest request){
        List<String> details = new ArrayList<>();
        for(ObjectError error : ex.getBindingResult().getAllErrors()) {
          details.add(error.getDefaultMessage());
        }
        ErrorMessage message = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false),
                details);
            
            return message;
        }

}
