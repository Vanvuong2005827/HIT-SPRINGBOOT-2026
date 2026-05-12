package com.example.base.service;

import com.example.base.domain.dto.request.*;
import com.example.base.domain.dto.response.CommonResponseDto;
import com.example.base.domain.dto.response.LoginResponseDto;

public interface AuthService {

  LoginResponseDto authentication(LoginRequestDto request);

  CommonResponseDto logout(LogoutRequestDto request);
}