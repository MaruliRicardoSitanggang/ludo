package com.ludoelite.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * User entity representing a player in the Ludo Elite system.
 * 
 * Demonstrates:
 * - JPA Entity mapping
 * - Encapsulation (private fields with getters/setters via Lombok)
 * - One-to-Many relationship with Games
 * 
 * @Entity - Marks this as a JPA entity
 * @Table - Specifies the database table name
 */
@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "username"),
    @UniqueConstraint(columnNames = "email")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Column(nullable = false)
    private String password; // BCrypt encoded
    
    @Email(message = "Email should be valid")
    @Column(length = 100)
    private String email;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    
    @Column(name = "is_online")
    private Boolean isOnline = false;
    
    // One user can host many games
    @OneToMany(mappedBy = "host", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Game> hostedGames = new ArrayList<>();
    
    // Statistics
    @Column(name = "games_played")
    private Integer gamesPlayed = 0;
    
    @Column(name = "games_won")
    private Integer gamesWon = 0;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    // Helper methods
    public void incrementGamesPlayed() {
        this.gamesPlayed++;
    }
    
    public void incrementGamesWon() {
        this.gamesWon++;
    }
    
    // Exclude password from toString for security
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", gamesPlayed=" + gamesPlayed +
                ", gamesWon=" + gamesWon +
                '}';
    }
}
