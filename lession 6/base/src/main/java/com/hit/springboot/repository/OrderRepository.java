package com.hit.springboot.repository;

import com.hit.springboot.entity.Order;
import com.hit.springboot.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.user WHERE o.user.id = :userId")
    List<Order> findByUserId(@Param("userId") Long userId);

    List<Order> findByProductContaining(String keyword);

    List<Order> findByPriceBetween(Double minPrice, Double maxPrice);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Order o SET o.status = :newStatus WHERE o.status = :oldStatus")
    int updateStatusBulk(@Param("oldStatus") OrderStatus oldStatus, @Param("newStatus") OrderStatus newStatus);
}
