package com.hit.springboot.repository;

import com.hit.springboot.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    List<Order> findByProductContaining(String keyword);

    List<Order> findByPriceBetween(Double minPrice, Double maxPrice);
}
