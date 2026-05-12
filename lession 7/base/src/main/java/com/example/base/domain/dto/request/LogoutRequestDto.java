package com.example.base.domain.dto.request;

import com.example.base.constant.ErrorMessage;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LogoutRequestDto {

  @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
  String token;

}
