package com.hit.springboot.repository;

import com.hit.springboot.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    Page<User> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    // ============================================================
    // Keyset Pagination — Cursor-based queries
    // ============================================================

    // Keyset: lấy records có id > cursor, sắp xếp theo id ASC
    @Query("SELECT u FROM User u WHERE u.id > :cursor ORDER BY u.id ASC")
    List<User> findNextPage(@Param("cursor") Long cursor, Pageable pageable);

    // Keyset: trang đầu tiên (không có cursor)
    @Query("SELECT u FROM User u ORDER BY u.id ASC")
    List<User> findFirstPage(Pageable pageable);

    // Search + Keyset
    @Query("SELECT u FROM User u WHERE u.id > :cursor AND " +
           "(LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY u.id ASC")
    List<User> findNextPageWithSearch(@Param("cursor") Long cursor,
                                      @Param("keyword") String keyword,
                                      Pageable pageable);

    // Search trang đầu (không cursor)
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "ORDER BY u.id ASC")
    List<User> findFirstPageWithSearch(@Param("keyword") String keyword,
                                       Pageable pageable);
}
