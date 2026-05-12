package com.example.base.controller;

import com.example.base.base.RestApiV1;
import com.example.base.base.RestData;
import com.example.base.base.VsResponseUtil;
import com.example.base.constant.UrlConstant;
import com.example.base.domain.dto.request.*;
import com.example.base.domain.dto.response.CommonResponseDto;
import com.example.base.domain.dto.response.LoginResponseDto;
import com.example.base.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestApiV1
@Validated
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
  AuthService authService;

  @Operation(summary = "Đăng nhập tài khoản", description = "Dùng để đăng nhập tài khoản")
  @PostMapping(UrlConstant.Auth.LOGIN)
  public ResponseEntity<RestData<LoginResponseDto>> login(@Valid @RequestBody LoginRequestDto requestDto) {
    LoginResponseDto response = authService.authentication(requestDto);
    return VsResponseUtil.success(response);
  }

  @Operation(summary = "Đăng xuất tài khoản", description = "Dùng để đăng xuất tài khoản")
  @PostMapping(UrlConstant.Auth.LOGOUT)
  public ResponseEntity<RestData<CommonResponseDto>> logout(@Valid @RequestBody LogoutRequestDto request) {
    CommonResponseDto response = authService.logout(request);
    return VsResponseUtil.success(response);
  }

}