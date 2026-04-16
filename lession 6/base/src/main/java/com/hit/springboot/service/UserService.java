package com.hit.springboot.service;

import com.hit.springboot.dto.request.CreateUserRequest;
import com.hit.springboot.dto.request.UpdateUserRequest;
import com.hit.springboot.dto.response.CursorPageResponse;
import com.hit.springboot.dto.response.UserDetailResponse;
import com.hit.springboot.dto.response.UserListResponse;
import com.hit.springboot.entity.User;
import com.hit.springboot.exception.extended.DuplicateResourceException;
import com.hit.springboot.exception.extended.ResourceNotFoundException;
import com.hit.springboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    // ============================================================
    // Offset Pagination — cho Thymeleaf admin view
    // ============================================================

    @Transactional(readOnly = true)
    public Page<UserListResponse> findAll(Pageable pageable, String search) {
        if (search != null && !search.isBlank()) {
            return userRepository.findByNameContainingIgnoreCase(search.trim(), pageable)
                    .map(UserListResponse::from);
        }
        return userRepository.findAll(pageable).map(UserListResponse::from);
    }

    // ============================================================
    // Keyset Pagination — cho REST API
    // ============================================================

    @Transactional(readOnly = true)
    public CursorPageResponse<UserListResponse> findAllKeyset(
            Long cursor, int size, String search) {

        // Lấy size + 1 để biết có trang tiếp không
        Pageable limit = PageRequest.of(0, size + 1);

        List<User> users;
        if (search != null && !search.isBlank()) {
            users = cursor != null
                    ? userRepository.findNextPageWithSearch(cursor, search.trim(), limit)
                    : userRepository.findFirstPageWithSearch(search.trim(), limit);
        } else {
            users = cursor != null
                    ? userRepository.findNextPage(cursor, limit)
                    : userRepository.findFirstPage(limit);
        }

        // Kiểm tra có trang tiếp không
        boolean hasNext = users.size() > size;
        if (hasNext) {
            users = users.subList(0, size);  // Bỏ record thừa
        }

        // Cursor tiếp theo = id của record cuối cùng
        String nextCursor = hasNext && !users.isEmpty()
                ? String.valueOf(users.get(users.size() - 1).getId())
                : null;

        List<UserListResponse> content = users.stream()
                .map(UserListResponse::from)
                .toList();

        return CursorPageResponse.of(content, size, nextCursor,
                userRepository.count());
    }

    // ============================================================
    // Detail / CRUD
    // ============================================================

    @Transactional(readOnly = true)
    public UserDetailResponse findById(Long id) {
        return userRepository.findById(id)
                .map(UserDetailResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @Transactional
    public UserDetailResponse create(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .active(true)
                .build();

        return UserDetailResponse.from(userRepository.save(user));
    }

    @Transactional
    public UserDetailResponse update(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        user.setName(request.getName());
        user.setPhoneNumber(request.getPhoneNumber());

        return UserDetailResponse.from(user);
    }

    @Transactional
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        userRepository.deleteById(id);
    }
}
