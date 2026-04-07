package com.hit.springboot.controller;

import com.hit.springboot.dto.request.CreateOrderRequest;
import com.hit.springboot.dto.response.ApiResponse;
import com.hit.springboot.entity.Order;
import com.hit.springboot.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "API quản lý đơn hàng")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Lấy tất cả đơn hàng")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Order>>> getAllOrders() {
        return ResponseEntity.ok(ApiResponse.success(orderService.findAll()));
    }

    @Operation(summary = "Lấy đơn hàng theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Order>> getOrderById(
            @Parameter(description = "ID đơn hàng", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(orderService.findById(id)));
    }

    @Operation(summary = "Tạo đơn hàng mới")
    @PostMapping
    public ResponseEntity<ApiResponse<Order>> createOrder(
            @Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(orderService.create(request)));
    }

    @Operation(summary = "Lấy đơn hàng theo user ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Order>>> getOrdersByUserId(
            @Parameter(description = "ID người dùng", example = "1")
            @PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(orderService.findByUserId(userId)));
    }

    @Operation(summary = "Xóa đơn hàng theo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa order thành công", null));
    }

    @Operation(summary = "Tìm đơn hàng theo sản phẩm")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Order>>> searchOrders(
            @RequestParam String keyword) {
        return ResponseEntity.ok(ApiResponse.success(orderService.searchByProduct(keyword)));
    }

    @Operation(summary = "Lấy đơn hàng theo khoảng giá")
    @GetMapping("/price")
    public ResponseEntity<ApiResponse<List<Order>>> getOrdersByPriceRange(
            @RequestParam Double min,
            @RequestParam Double max) {
        return ResponseEntity.ok(ApiResponse.success(orderService.findByPriceRange(min, max)));
    }
}
