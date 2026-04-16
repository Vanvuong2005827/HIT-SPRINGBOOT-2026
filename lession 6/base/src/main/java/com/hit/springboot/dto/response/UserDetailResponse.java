package com.hit.springboot.dto.response;

import com.hit.springboot.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Schema(description = "Thông tin user chi tiết — đầy đủ hơn")
public class UserDetailResponse {

    @Schema(description = "ID người dùng", example = "1")
    private Long id;

    @Schema(description = "Tên đầy đủ", example = "Nguyễn Văn A")
    private String name;

    @Schema(description = "Email", example = "a@gmail.com")
    private String email;

    @Schema(description = "Số điện thoại", example = "0912345678")
    private String phoneNumber;

    @Schema(description = "Trạng thái hoạt động")
    private Boolean active;

    @Schema(description = "Thời điểm tạo")
    private LocalDateTime createdAt;

    @Schema(description = "Thời điểm cập nhật")
    private LocalDateTime updatedAt;

    public static UserDetailResponse from(User user) {
        return UserDetailResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
