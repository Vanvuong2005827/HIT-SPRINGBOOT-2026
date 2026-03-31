package com.hit.springboot.service;

import com.hit.springboot.dto.request.CreateOrderRequest;
import com.hit.springboot.entity.Order;
import com.hit.springboot.entity.User;
import com.hit.springboot.exception.extended.ResourceNotFoundException;
import com.hit.springboot.repository.OrderRepository;
import com.hit.springboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
    }

    public Order create(CreateOrderRequest request) {
        log.info("Tạo order mới cho user id: {}", request.getUserId());

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));

        Order order = Order.builder()
                .product(request.getProduct())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .user(user)
                .build();

        Order savedOrder = orderRepository.save(order);
        log.info("Đã tạo order id: {} cho user id: {}", savedOrder.getId(), user.getId());
        return savedOrder;
    }

    public List<Order> findByUserId(Long userId) {
        log.info("Lấy orders của user id: {}", userId);

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }

        return orderRepository.findByUserId(userId);
    }

    public void delete(Long id) {
        Order order = findById(id);
        orderRepository.delete(order);
        log.info("Đã xóa order id: {}", id);
    }

    public List<Order> searchByProduct(String keyword) {
        return orderRepository.findByProductContaining(keyword);
    }

    public List<Order> findByPriceRange(Double min, Double max) {
        return orderRepository.findByPriceBetween(min, max);
    }
}
