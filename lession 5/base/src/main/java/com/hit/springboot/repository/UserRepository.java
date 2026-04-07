package com.hit.springboot.repository;

import com.hit.springboot.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<User> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"orders"})
    List<User> findByActive(Boolean active);

    @Query("SELECT DISTINCT u FROM User u JOIN FETCH u.orders WHERE u.active = true")
    List<User> findActiveUsersWithOrders();

    List<User> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
