package com.ludoelite.backend.service.game;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response object for game state updates.
 * Sent to clients via WebSocket after each action.
 */
@Data
@NoArgsConstructor
public class GameStateResponse {
    private Long gameId;
    private String status;
    private Integer currentTurn;
    private String gameStateJson;
    private String message;
    private Long timestamp;
    
    public GameStateResponse(String message) {
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }
}
