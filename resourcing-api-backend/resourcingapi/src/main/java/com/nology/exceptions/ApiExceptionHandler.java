package com.nology.exceptions;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
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
//    
//    @ExceptionHandler(value = Exception.class)
//    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorMessage globalExceptionHandler(Exception ex, WebRequest request) {
//        ErrorMessage message = new ErrorMessage(
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                new Date(),
//                ex.getMessage(),
//                request.getDescription(false));
//        
//        return message;
//    }
    
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
    
//    @ExceptionHandler(value = ApiRequestException.class)
//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    public ApiException handleApiRequestException(ApiRequestException e) {
//        // 1. Create payload containing exception details.
//        
//        ApiException apiException = new ApiException(
//                e.getMessage(),
//                HttpStatus.BAD_REQUEST,
//                ZonedDateTime.now(ZoneId.of("Z"))
//                );
//        // 2. Return response entity
//        return apiException;
//    }
    
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
    

//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
//    public ErrorMessage handleMethodArgumentNotValid(
//            MethodArgumentNotValidException ex,
//            HttpHeaders headers, HttpStatus status, WebRequest request) {
//
//        Map<String, Object> responseBody = new LinkedHashMap<>();
//        responseBody.put("timestamp", new Date());
//        responseBody.put("status", status.value());
//        
//        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
//                
//        List<String> listErrors = new ArrayList<>();
//        
//        for (FieldError fieldError : fieldErrors) {
//            String errorMessage = fieldError.getDefaultMessage();
//            listErrors.add(errorMessage);
//        }
//        
//        responseBody.put("errors", listErrors);
//        
//        ErrorMessage handler = new ErrorMessage(
//                HttpStatus.UNPROCESSABLE_ENTITY.value(),
//                new Date(),
//                ex.getMessage(),
//                request.getDescription(false));
//        
//        return handler;
//    }
    

}
