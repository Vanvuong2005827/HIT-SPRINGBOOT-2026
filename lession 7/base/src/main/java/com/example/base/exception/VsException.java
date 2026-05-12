package com.example.base.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class VsException extends RuntimeException {

  private Object errMessage;

  private HttpStatus status;

  private String[] params;

  public VsException(String errMessage) {
    super(errMessage);
    this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    this.errMessage = errMessage;
  }

  public VsException(HttpStatus status, Object errMessage) {
    super(String.valueOf(errMessage));
    this.errMessage = errMessage;
    this.status = status;
  }

  public VsException(String errMessage, String[] params) {
    super(errMessage);
    this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    this.errMessage = errMessage;
    this.params = params;
  }

  public VsException(HttpStatus status, String errMessage, String[] params) {
    super(errMessage);
    this.status = status;
    this.errMessage = errMessage;
    this.params = params;
  }

}