package com.example.base.domain.dto.response;

import com.example.base.domain.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponseDto {
  String id;

  String username;

  String email;

  String firstName;

  String lastName;

  Role role;

}