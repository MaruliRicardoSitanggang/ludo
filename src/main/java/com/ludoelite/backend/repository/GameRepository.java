package com.ludoelite.backend.repository;

import com.ludoelite.backend.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Game entity.
 *
 * Demonstrates:
 * - Repository pattern
 * - Query method naming convention
 * - Data access abstraction
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    /**
     * Find all games with a specific status.
     * Used to find WAITING games that users can join.
     */
    List<Game> findByStatus(String status);

    /**
     * Find all games hosted by a specific user.
     */
    @Query("SELECT g FROM Game g WHERE g.host.id = :hostId")
    List<Game> findByHostId(@Param("hostId") Long hostId);

    /**
     * Find games by status and order by creation date.
     * Used for listing available games.
     */
    List<Game> findByStatusOrderByCreatedAtDesc(String status);

    /**
     * Find all games that are not full.
     * Used to show joinable games.
     */
    @Query("SELECT g FROM Game g WHERE g.currentPlayers < g.maxPlayers")
    List<Game> findByCurrentPlayersLessThanMaxPlayers();
}
