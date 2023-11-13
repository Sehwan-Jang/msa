package com.example.user.controller;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> handle(FeignException e) {
        log.error(e.getMessage());
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
}
