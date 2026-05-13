package com.example.base.exception;

import com.example.base.base.RestData;
import com.example.base.base.VsResponseUtil;
import com.example.base.constant.ErrorMessage;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(VsException.class)
  public ResponseEntity<RestData<Object>> handleVsException(VsException ex) {
    return VsResponseUtil.error(ex.getStatus(), ex.getErrMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<RestData<Object>> handleValidationException(MethodArgumentNotValidException ex) {
    String message = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
    return VsResponseUtil.error(HttpStatus.BAD_REQUEST, message);
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<RestData<Object>> handleBindException(BindException ex) {
    String message = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
    return VsResponseUtil.error(HttpStatus.BAD_REQUEST, message);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<RestData<Object>> handleAccessDeniedException(AccessDeniedException ex) {
    return VsResponseUtil.error(HttpStatus.FORBIDDEN, ErrorMessage.FORBIDDEN);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<RestData<Object>> handleInternalServerError(Exception ex) {
    return VsResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
  }
}
