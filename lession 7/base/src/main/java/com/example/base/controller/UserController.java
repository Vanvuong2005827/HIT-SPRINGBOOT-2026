package com.example.base.controller;

import com.example.base.base.RestApiV1;
import com.example.base.base.RestData;
import com.example.base.base.VsResponseUtil;
import com.example.base.constant.UrlConstant;
import com.example.base.domain.dto.request.ChangePasswordRequestDto;
import com.example.base.domain.dto.request.CreateUserRequestDto;
import com.example.base.domain.dto.response.AdminStatsResponseDto;
import com.example.base.domain.dto.response.CommonResponseDto;
import com.example.base.domain.dto.response.UserResponseDto;
import com.example.base.security.SecurityUtils;
import com.example.base.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestApiV1
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

  UserService userService;

  @Operation(summary = "Lấy thông tin profile", description = "Dùng để người dùng lấy thông tin profile đầy đủ", security = @SecurityRequirement(name = "Bearer Token"))
  @GetMapping(UrlConstant.User.GET_PROFILE)
  @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
  public ResponseEntity<RestData<UserResponseDto>> getMyProfile() {
    String userId = SecurityUtils.getCurrentUserId();
    UserResponseDto response = userService.getMyProfile(userId);
    return VsResponseUtil.success(response);
  }

  @Operation(summary = "Đổi password", description = "Dùng để người dùng đổi password", security = @SecurityRequirement(name = "Bearer Token"))
  @PatchMapping(UrlConstant.User.CHANGE_PASSWORD)
  @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
  public ResponseEntity<RestData<CommonResponseDto>> changePassword(
      @Valid @RequestBody ChangePasswordRequestDto request) {
    String userId = SecurityUtils.getCurrentUserId();
    CommonResponseDto response = userService.changePassword(userId, request);
    return VsResponseUtil.success(response);
  }

  // //
  // Methods for ADMIN //
  // //

  @Tag(name = "admin-controller")
  @Operation(summary = "Lấy thông tin user theo ID", description = "Dùng để admin lấy thông tin chi tiết của một user", security = @SecurityRequirement(name = "Bearer Token"))
  @GetMapping(UrlConstant.Admin.GET_USER)
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<RestData<UserResponseDto>> getUserById(@PathVariable String userId) {
    UserResponseDto response = userService.getUserById(userId);
    return VsResponseUtil.success(response);
  }

  @Tag(name = "admin-controller")
  @Operation(summary = "Tạo user mới", description = "Dùng để admin tạo user mới trong hệ thống", security = @SecurityRequirement(name = "Bearer Token"))
  @PostMapping(UrlConstant.Admin.CREATE_USER)
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<RestData<UserResponseDto>> createUser(@Valid @RequestBody CreateUserRequestDto request) {
    UserResponseDto response = userService.createUser(request);
    return VsResponseUtil.success(response);
  }

  @Tag(name = "admin-controller")
  @Operation(summary = "Khóa/Mở khóa user", description = "Dùng để admin khóa hoặc mở khóa một user trong hệ thống", security = @SecurityRequirement(name = "Bearer Token"))
  @PatchMapping(UrlConstant.Admin.TOGGLE_USER_STATUS)
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<RestData<CommonResponseDto>> toggleUserStatus(@PathVariable String userId) {
    CommonResponseDto response = userService.toggleUserStatus(userId);
    return VsResponseUtil.success(response);
  }

  @Tag(name = "admin-controller")
  @Operation(summary = "Xóa user", description = "Dùng để admin xóa một user khỏi hệ thống", security = @SecurityRequirement(name = "Bearer Token"))
  @DeleteMapping(UrlConstant.Admin.DELETE_USER)
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<RestData<CommonResponseDto>> deleteUser(@PathVariable String userId) {
    CommonResponseDto response = userService.deleteUser(userId);
    return VsResponseUtil.success(response);
  }

  @Tag(name = "admin-controller")
  @Operation(summary = "Thống kê", description = "Dùng để admin thống kê số lượng user và số lượng token đang hoạt động", security = @SecurityRequirement(name = "Bearer Token"))
  @GetMapping(UrlConstant.Admin.GET_STATS)
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  public ResponseEntity<RestData<AdminStatsResponseDto>> getStatistics() {
    AdminStatsResponseDto response = userService.getStatistics();
    return VsResponseUtil.success(response);
  }
}