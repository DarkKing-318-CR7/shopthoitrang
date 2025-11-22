package com.uth.shoptmdt.repository;

import com.uth.shoptmdt.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "role")
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    // Lấy bản ghi mới nhất theo username (an toàn hơn single result)
    Optional<User> findTopByUsernameOrderByIdDesc(String username);
    boolean existsByEmail(String email);
}
