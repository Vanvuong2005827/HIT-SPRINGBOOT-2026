package com.hit.springboot.service;

import com.hit.springboot.dto.request.CreateUserRequest;
import com.hit.springboot.dto.request.UpdateUserRequest;
import com.hit.springboot.entity.User;
import com.hit.springboot.exception.extended.DuplicateResourceException;
import com.hit.springboot.exception.extended.ResourceNotFoundException;
import com.hit.springboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public List<User> findAll() {
        log.info("Lấy danh sách tất cả users");
        return userRepository.findAll();
    }

    public User findById(Long id) {
        log.info("Tìm user với id: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    public User create(CreateUserRequest request) {
        log.info("Tạo user mới với email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .age(request.getAge())
                .phone(request.getPhone())
                .build();

        User savedUser = userRepository.save(user);
        log.info("Đã tạo user thành công với id: {}", savedUser.getId());
        return savedUser;
    }

    public User update(Long id, UpdateUserRequest request) {
        log.info("Cập nhật user id: {}", id);

        User user = findById(id);

        userRepository.findByEmail(request.getEmail())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new DuplicateResourceException("User", "email", request.getEmail());
                    }
                });

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setAge(request.getAge());
        user.setPhone(request.getPhone());

        User updatedUser = userRepository.save(user);
        log.info("Đã cập nhật user id: {} thành công", id);
        return updatedUser;
    }

    public void delete(Long id) {
        log.info("Xóa user id: {}", id);
        User user = findById(id);
        userRepository.delete(user);
        log.info("Đã xóa user id: {} thành công", id);
    }

    public List<User> searchByName(String keyword) {
        log.info("Tìm users theo tên chứa: '{}'", keyword);
        return userRepository.findByNameContaining(keyword);
    }

    public List<User> findByAgeGreaterThan(Integer age) {
        log.info("Tìm users có tuổi > {}", age);
        return userRepository.findByAgeGreaterThan(age);
    }
}
