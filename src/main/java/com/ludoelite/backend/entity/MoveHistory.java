package com.ludoelite.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * MoveHistory entity for audit trail of all moves in a game.
 * 
 * Demonstrates:
 * - JPA entity with multiple relationships
 * - Audit logging pattern
 * - Encapsulation
 */
@Entity
@Table(name = "move_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private User player;
    
    @Column(name = "action_type", nullable = false, length = 20)
    private String actionType; // ROLL, MOVE, JOIN, LEAVE
    
    @Column(name = "dice_value")
    private Integer diceValue;
    
    @Column(name = "piece_number")
    private Integer pieceNumber;
    
    @Column(name = "from_position")
    private Integer fromPosition;
    
    @Column(name = "to_position")
    private Integer toPosition;
    
    @Column(name = "captured")
    private Boolean captured = false;
    
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    
    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
    
    // Factory methods for creating specific move types
    public static MoveHistory createRollMove(Game game, User player, int diceValue) {
        MoveHistory move = new MoveHistory();
        move.setGame(game);
        move.setPlayer(player);
        move.setActionType("ROLL");
        move.setDiceValue(diceValue);
        return move;
    }
    
    public static MoveHistory createPieceMove(Game game, User player, 
                                             int pieceNumber, int from, int to, 
                                             boolean captured) {
        MoveHistory move = new MoveHistory();
        move.setGame(game);
        move.setPlayer(player);
        move.setActionType("MOVE");
        move.setPieceNumber(pieceNumber);
        move.setFromPosition(from);
        move.setToPosition(to);
        move.setCaptured(captured);
        return move;
    }
    
    @Override
    public String toString() {
        return "MoveHistory{" +
                "id=" + id +
                ", actionType='" + actionType + '\'' +
                ", diceValue=" + diceValue +
                ", pieceNumber=" + pieceNumber +
                ", timestamp=" + timestamp +
                '}';
    }
}
