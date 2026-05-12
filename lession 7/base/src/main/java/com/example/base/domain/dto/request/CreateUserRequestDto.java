package com.example.base.domain.dto.request;

import com.example.base.constant.ErrorMessage;
import com.example.base.domain.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserRequestDto {

  @Schema(description = "Tên đăng nhập", example = "user123")
  @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
  String username;

  @Schema(description = "Email người dùng", example = "user@gmail.com")
  @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
  @Email(message = ErrorMessage.INVALID_SOME_THING_FIELD)
  String email;

  @Schema(description = "Mật khẩu", example = "User123@")
  @NotBlank(message = ErrorMessage.NOT_BLANK_FIELD)
  @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{8,}$", message = ErrorMessage.INVALID_FORMAT_PASSWORD)
  String password;

  @Schema(description = "Họ/Tên đệm", example = "Phạm Văn")
  String firstName;

  @Schema(description = "Tên", example = "A")
  String lastName;

  @Schema(description = "Vai trò", example = "USER")
  @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
  Role role;
}
