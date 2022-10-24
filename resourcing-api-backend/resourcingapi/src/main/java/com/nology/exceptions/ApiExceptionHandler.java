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
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ApiExceptionHandler {
    
    @ExceptionHandler(value = ApiRequestException.class)
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e) {
        // 1. Create payload containing exception details.
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        
        ApiException apiException = new ApiException(
                e.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
                );
        // 2. Return response entity
        return new ResponseEntity<>(apiException, badRequest);
    }
    
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
//    public Map<String, String> handleInvalidArgument( MethodArgumentNotValidException ex ){
//        Map<String, String> errorMap = new HashMap<>();
//        ex.getBindingResult().getFieldErrors().forEach(error -> {
//            errorMap.put(error.getField(), error.getDefaultMessage());
//        });
//        return errorMap;
//    }
    

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("timestamp", new Date());
        responseBody.put("status", status.value());
        
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
                
        List<String> listErrors = new ArrayList<>();
        
        for (FieldError fieldError : fieldErrors) {
            String errorMessage = fieldError.getDefaultMessage();
            listErrors.add(errorMessage);
        }
        
        responseBody.put("errors", listErrors);
        
        return new ResponseEntity<>(responseBody, headers, status);
    }
    

}
