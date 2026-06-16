package com.ludoelite.backend.repository;

import com.ludoelite.backend.entity.MoveHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for MoveHistory entity.
 * Used for audit trail and game replay functionality.
 */
@Repository
public interface MoveHistoryRepository extends JpaRepository<MoveHistory, Long> {
    
    /**
     * Find all moves for a specific game, ordered by timestamp.
     * Used for game replay and history viewing.
     */
    List<MoveHistory> findByGameIdOrderByTimestampAsc(Long gameId);
    
    /**
     * Find all moves by a specific player in a game.
     * Used for player statistics.
     */
    List<MoveHistory> findByGameIdAndPlayerId(Long gameId, Long playerId);
    
    /**
     * Count total moves in a game.
     * Used for game statistics.
     */
    long countByGameId(Long gameId);
}
