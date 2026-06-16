package com.ludoelite.backend.repository;

import com.ludoelite.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User entity.
 * Spring Data JPA will automatically implement this interface.
 * 
 * Demonstrates:
 * - Repository pattern
 * - Spring Data JPA abstraction
 * - Encapsulation of data access logic
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find user by username.
     * Used for authentication and user lookup.
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find user by email.
     * Used for registration validation.
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if username exists.
     * Used for registration validation.
     */
    boolean existsByUsername(String username);
    
    /**
     * Check if email exists.
     * Used for registration validation.
     */
    boolean existsByEmail(String email);
}
