package com.example.base.service;

import com.example.base.domain.dto.request.CreateUserRequestDto;
import com.example.base.domain.dto.response.UserResponseDto;

public interface UserService {

  UserResponseDto getMyProfile(String userId);

  UserResponseDto getUserById(String userId);

  UserResponseDto createUser(CreateUserRequestDto request);
}
