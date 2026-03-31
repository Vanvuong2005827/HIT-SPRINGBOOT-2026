package com.hit.springboot.controller;

import com.hit.springboot.dto.request.CreateOrderRequest;
import com.hit.springboot.dto.response.ApiResponse;
import com.hit.springboot.entity.Order;
import com.hit.springboot.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Order>>> getAllOrders() {
        return ResponseEntity.ok(ApiResponse.success(orderService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Order>> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(orderService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Order>> createOrder(
            @Valid @RequestBody CreateOrderRequest request) {
        Order created = orderService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(created));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Order>>> getOrdersByUserId(
            @PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(orderService.findByUserId(userId)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa order thành công", null));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Order>>> searchOrders(
            @RequestParam String keyword) {
        return ResponseEntity.ok(ApiResponse.success(orderService.searchByProduct(keyword)));
    }

    @GetMapping("/price")
    public ResponseEntity<ApiResponse<List<Order>>> getOrdersByPriceRange(
            @RequestParam Double min,
            @RequestParam Double max) {
        return ResponseEntity.ok(ApiResponse.success(orderService.findByPriceRange(min, max)));
    }
}
