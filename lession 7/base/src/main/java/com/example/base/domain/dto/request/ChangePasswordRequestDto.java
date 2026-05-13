package com.example.base.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequestDto {

  @NotBlank(message = "Mật khẩu cũ không được để trống")
  String oldPassword;

  @NotBlank(message = "Mật khẩu mới không được để trống")
  String newPassword;
}
