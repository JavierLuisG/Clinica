package com.backend.clinica.exception;

import com.backend.clinica.dto.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<MessageResponse> resourceNotFoundException(ResourceNotFoundException e) {
    return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(IllegalArgException.class)
  public ResponseEntity<MessageResponse> illegalArgException(IllegalArgException e) {
    return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
  }
}
