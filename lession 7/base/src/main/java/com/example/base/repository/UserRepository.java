package com.example.base.repository;

import com.example.base.domain.entity.Role;
import com.example.base.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

  Optional<User> findUserDetailByUsername(String username);

  Optional<User> findByUsername(String username);

  boolean existsUserByUsername(String emailOrUsername);

  boolean existsUserByEmail(String email);

  Optional<User> findByEmail(String email);

  long countByRole(Role role);

}