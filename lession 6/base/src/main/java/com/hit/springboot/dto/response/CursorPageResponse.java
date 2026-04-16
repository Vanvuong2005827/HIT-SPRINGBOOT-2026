package com.hit.springboot.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@Schema(description = "Response cho Keyset (Cursor-based) Pagination")
public class CursorPageResponse<T> {

    @Schema(description = "Danh sách dữ liệu")
    private List<T> content;

    @Schema(description = "Số phần tử trả về", example = "10")
    private int size;

    @Schema(description = "Còn trang tiếp không", example = "true")
    private boolean hasNext;

    @Schema(description = "Cursor cho request tiếp theo", example = "15")
    private String nextCursor;

    @Schema(description = "Tổng số phần tử (optional)", example = "100")
    private long totalElements;

    public static <T> CursorPageResponse<T> of(
            List<T> content, int size, String nextCursor, long totalElements) {
        return CursorPageResponse.<T>builder()
                .content(content)
                .size(content.size())
                .hasNext(nextCursor != null)
                .nextCursor(nextCursor)
                .totalElements(totalElements)
                .build();
    }
}
