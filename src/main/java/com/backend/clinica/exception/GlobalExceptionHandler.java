package com.backend.clinica.exception;

import com.backend.clinica.dto.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

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

  @ExceptionHandler(ParseDateTimeException.class)
  public ResponseEntity<MessageResponse> parseDateTimeException(ParseDateTimeException e) {
    return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<MessageResponse> handleDateParseError(MethodArgumentTypeMismatchException e) {
    return new ResponseEntity<>(
        new MessageResponse("Ingrese correctamente el rango de fechas con el formato: yyyy-MM-dd'T'HH:mm"),
        HttpStatus.BAD_REQUEST
    );
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {
    Map<String, String> errors = new HashMap<>();
    e.getBindingResult().getFieldErrors().forEach(error -> {
      errors.put(error.getField(), error.getDefaultMessage());
    });
    return ResponseEntity.badRequest().body(errors);
  }
}
