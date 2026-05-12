package com.example.base.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestData<T> {
  private RestStatus status;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private T message;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private T data;

  public RestData(T data) {
    this.status = RestStatus.SUCCESS;
    this.data = data;
  }

  public static <T> RestData<T> error(T message) {
    return new RestData<>(RestStatus.ERROR, message, null);
  }
}