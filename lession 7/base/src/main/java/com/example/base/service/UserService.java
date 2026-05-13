package com.example.base.service;

import com.example.base.domain.dto.request.ChangePasswordRequestDto;
import com.example.base.domain.dto.request.CreateUserRequestDto;
import com.example.base.domain.dto.response.AdminStatsResponseDto;
import com.example.base.domain.dto.response.CommonResponseDto;
import com.example.base.domain.dto.response.UserResponseDto;

public interface UserService {

  UserResponseDto getMyProfile(String userId);

  UserResponseDto getUserById(String userId);

  UserResponseDto createUser(CreateUserRequestDto request);

  CommonResponseDto changePassword(String userId, ChangePasswordRequestDto request);

  CommonResponseDto toggleUserStatus(String userId);

  CommonResponseDto deleteUser(String userId);

  AdminStatsResponseDto getStatistics();
}
