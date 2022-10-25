package com.mediscreen.patient.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ValidationErrorHandlerController extends Throwable {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationErrorHandler.class)
    public static ResponseEntity<Object> getBindingResultErrors(BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            List<String> messages = new ArrayList<>();
            for (FieldError error : fieldErrors) {
                messages.add(error.getDefaultMessage());
            }
            log.error("Message d'erreur : " + messages);
            return new ResponseEntity<>(messages, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

}