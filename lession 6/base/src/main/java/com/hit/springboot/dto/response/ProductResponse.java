package com.hit.springboot.dto.response;

import com.hit.springboot.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Schema(description = "Thông tin sản phẩm trả về")
public class ProductResponse {

    @Schema(description = "ID sản phẩm", example = "1")
    private Long id;

    @Schema(description = "Tên sản phẩm", example = "Spring Boot in Action")
    private String name;

    @Schema(description = "Mô tả sản phẩm")
    private String description;

    @Schema(description = "Giá", example = "250000")
    private BigDecimal price;

    @Schema(description = "Danh mục", example = "BOOK")
    private String category;

    @Schema(description = "Số lượng tồn kho", example = "50")
    private Integer stockQuantity;

    @Schema(description = "Trạng thái hoạt động")
    private Boolean active;

    @Schema(description = "Thời điểm tạo")
    private LocalDateTime createdAt;

    public static ProductResponse from(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory().name())
                .stockQuantity(product.getStockQuantity())
                .active(product.getActive())
                .createdAt(product.getCreatedAt())
                .build();
    }
}
