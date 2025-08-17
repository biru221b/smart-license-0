package com.ecdsa.ecdsa.utils;


import jakarta.transaction.SystemException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandlerGlobal {
    @ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity invalidParameter(MethodArgumentNotValidException exception) {
        Map<String, String> errors = exception
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        String fieldName = errors.keySet().stream().findFirst().get();
        String message = errors.values().stream().findFirst().get();

        return ResponseMessage.generate(fieldName+' '+message,HttpStatus.BAD_REQUEST);
        //return ResponseMessage.failure(errors, "invalid method arguments", HttpStatus.BAD_REQUEST);
        //return ResponseEntity.of(Optional.of(errors));
    }
    @ExceptionHandler(SystemException.class)
	public ResponseEntity systemError(SystemException exception) {

        return ResponseMessage.failure(exception.getMessage(), "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SendErrorMessageCustom.class)
	public ResponseEntity customErrorMessage(SendErrorMessageCustom exception){
        return ResponseMessage.generate("Bad Request",exception.getMessage(),HttpStatus.BAD_REQUEST);
    }



}
