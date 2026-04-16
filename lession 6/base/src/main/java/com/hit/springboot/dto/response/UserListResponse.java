package com.hit.springboot.dto.response;

import com.hit.springboot.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "Thông tin user cho danh sách — chỉ field cơ bản")
public class UserListResponse {

    @Schema(description = "ID người dùng", example = "1")
    private Long id;

    @Schema(description = "Tên đầy đủ", example = "Nguyễn Văn A")
    private String name;

    @Schema(description = "Email", example = "a@gmail.com")
    private String email;

    @Schema(description = "Trạng thái hoạt động")
    private Boolean active;

    public static UserListResponse from(User user) {
        return UserListResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .active(user.getActive())
                .build();
    }
}
