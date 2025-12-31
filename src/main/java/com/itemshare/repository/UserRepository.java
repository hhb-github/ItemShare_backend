package com.itemshare.repository;

import com.itemshare.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.status = 1 AND (u.username LIKE %?1% OR u.nickname LIKE %?1%)")
    Page<User> findByKeyword(String keyword, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.lastLoginAt >= ?1")
    Page<User> findActiveUsers(LocalDateTime since, Pageable pageable);
}
