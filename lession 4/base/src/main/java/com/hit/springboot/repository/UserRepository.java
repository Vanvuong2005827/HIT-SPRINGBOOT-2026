package com.hit.springboot.repository;

import com.hit.springboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByNameContaining(String keyword);

    List<User> findByAgeGreaterThan(Integer age);

    List<User> findByAgeBetweenOrderByNameAsc(Integer minAge, Integer maxAge);
}
