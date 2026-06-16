package com.ludoelite.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Game entity representing a Ludo game session.
 * 
 * Demonstrates:
 * - JPA Entity mapping with relationships
 * - Encapsulation (private fields)
 * - Many-to-One relationship with User
 */
@Entity
@Table(name = "games")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "game_name", nullable = false, length = 100)
    private String gameName;
    
    @Column(nullable = false, length = 20)
    private String status = "WAITING"; // WAITING, IN_PROGRESS, FINISHED
    
    @Column(name = "max_players", nullable = false)
    private Integer maxPlayers = 4;
    
    @Column(name = "current_players", nullable = false)
    private Integer currentPlayers = 0;
    
    @Column(name = "current_turn")
    private Integer currentTurn = 0;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_user_id")
    private User host;
    
    // Store complete game state as JSON
    @Column(name = "game_state_json", columnDefinition = "TEXT")
    private String gameStateJson;
    
    @Column(name = "winner_user_id")
    private Long winnerUserId;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Helper methods
    public boolean isFull() {
        return currentPlayers >= maxPlayers;
    }
    
    public boolean canStart() {
        return currentPlayers >= 2 && "WAITING".equals(status);
    }
    
    public void incrementPlayers() {
        this.currentPlayers++;
    }
    
    public void decrementPlayers() {
        if (this.currentPlayers > 0) {
            this.currentPlayers--;
        }
    }
    
    public void startGame() {
        this.status = "IN_PROGRESS";
    }
    
    public void finishGame(Long winnerId) {
        this.status = "FINISHED";
        this.winnerUserId = winnerId;
    }
    
    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", gameName='" + gameName + '\'' +
                ", status='" + status + '\'' +
                ", currentPlayers=" + currentPlayers +
                ", maxPlayers=" + maxPlayers +
                '}';
    }
}
