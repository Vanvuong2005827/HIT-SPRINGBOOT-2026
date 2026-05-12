package com.example.base.base;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public class VsResponseUtil {
  public static <T> ResponseEntity<RestData<T>> success(T data) {
    return success(HttpStatus.OK, data);
  }

  public static <T> ResponseEntity<RestData<T>> success(HttpStatus status, T data) {
    RestData<T> response = new RestData<>(data);
    return new ResponseEntity<>(response, status);
  }

  public static <T> ResponseEntity<RestData<T>> success(MultiValueMap<String, String> header, T data) {
    return success(HttpStatus.OK, header, data);
  }

  public static <T> ResponseEntity<RestData<T>> success(HttpStatus status, MultiValueMap<String, String> header,
      T data) {
    RestData<T> response = new RestData<>(data);
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.addAll(header);
    return ResponseEntity.ok().headers(responseHeaders).body(response);
  }

  public static <T> ResponseEntity<RestData<T>> error(HttpStatus status, T message) {
    RestData<T> response = RestData.error(message);
    return new ResponseEntity<>(response, status);
  }
}