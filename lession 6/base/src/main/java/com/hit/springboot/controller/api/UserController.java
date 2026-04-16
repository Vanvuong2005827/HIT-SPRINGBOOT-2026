package com.hit.springboot.controller.api;

import com.hit.springboot.dto.request.CreateUserRequest;
import com.hit.springboot.dto.request.UpdateUserRequest;
import com.hit.springboot.dto.response.ApiResponse;
import com.hit.springboot.dto.response.CursorPageResponse;
import com.hit.springboot.dto.response.UserDetailResponse;
import com.hit.springboot.dto.response.UserListResponse;
import com.hit.springboot.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "API quản lý người dùng — Keyset Pagination")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Danh sách user — keyset pagination")
    @GetMapping
    public ResponseEntity<ApiResponse<CursorPageResponse<UserListResponse>>> getAll(

            @Parameter(description = "Cursor — id record cuối trang trước")
            @RequestParam(required = false) Long cursor,

            @Parameter(description = "Số phần tử / trang (tối đa 100)")
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,

            @Parameter(description = "Từ khóa tìm kiếm (tên hoặc email)")
            @RequestParam(required = false) String search) {

        return ResponseEntity.ok(
                ApiResponse.success(userService.findAllKeyset(cursor, size, search)));
    }

    @Operation(summary = "Lấy thông tin chi tiết user theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDetailResponse>> getUserById(
            @Parameter(description = "ID người dùng", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.findById(id)));
    }

    @Operation(summary = "Tạo user mới")
    @PostMapping
    public ResponseEntity<ApiResponse<UserDetailResponse>> createUser(
            @Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(userService.create(request)));
    }

    @Operation(summary = "Cập nhật thông tin user")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDetailResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công", userService.update(id, request)));
    }

    @Operation(summary = "Xóa user theo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }
}
