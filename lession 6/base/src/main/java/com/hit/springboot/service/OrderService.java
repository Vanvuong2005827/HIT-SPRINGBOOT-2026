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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
    }

    @Transactional
    public Order create(CreateOrderRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));

        Order order = Order.builder()
                .product(request.getProduct())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .user(user)
                .build();

        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Transactional
    public void delete(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order", "id", id);
        }
        orderRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Order> searchByProduct(String keyword) {
        return orderRepository.findByProductContaining(keyword);
    }

    @Transactional(readOnly = true)
    public List<Order> findByPriceRange(Double min, Double max) {
        return orderRepository.findByPriceBetween(min, max);
    }
}
