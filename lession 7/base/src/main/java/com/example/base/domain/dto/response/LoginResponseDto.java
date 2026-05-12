package com.example.base.domain.dto.response;

import com.example.base.constant.CommonConstant;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class LoginResponseDto {

  HttpStatus status;

  String message;

  String accessToken;

  String refreshToken;

  String id;

  @Builder.Default
  String tokenType = CommonConstant.BEARER_TOKEN;

}