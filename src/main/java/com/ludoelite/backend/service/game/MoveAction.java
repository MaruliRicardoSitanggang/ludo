package com.ludoelite.backend.service.game;

import com.ludoelite.backend.entity.Game;
import com.ludoelite.backend.entity.User;

import java.util.Map;

/**
 * Concrete implementation of GameAction for moving pieces.
 * 
 * Demonstrates:
 * - INHERITANCE: Extends GameAction
 * - POLYMORPHISM: Overrides abstract methods with specific logic
 * - ENCAPSULATION: Move validation and execution logic encapsulated
 */
public class MoveAction extends GameAction {
    
    /**
     * Validate that piece can be moved.
     * Rules:
     * - Dice must have been rolled
     * - Piece must be valid (0-3)
     * - Move must be legal according to Ludo rules
     * 
     * Demonstrates: POLYMORPHISM (specific implementation)
     */
    @Override
    protected boolean validate(Game game, User player, Map<String, Object> params) {
        // Check game is in progress
        if (!"IN_PROGRESS".equals(game.getStatus())) {
            return false;
        }
        
        // Get move parameters
        Integer pieceNumber = getIntParam(params, "pieceNumber");
        Integer diceValue = getIntParam(params, "diceValue");
        
        if (pieceNumber == null || diceValue == null) {
            return false;
        }
        
        // Validate piece number (0-3)
        if (pieceNumber < 0 || pieceNumber > 3) {
            return false;
        }
        
        // Validate dice value (1-6)
        if (diceValue < 1 || diceValue > 6) {
            return false;
        }
        
        // TODO: Validate move is legal according to Ludo rules
        // - Check piece can move from current position
        // - Check destination is valid
        // - Check for captures
        
        return true;
    }
    
    /**
     * Perform piece move action.
     * 
     * Demonstrates: POLYMORPHISM (specific implementation)
     */
    @Override
    protected void performAction(Game game, User player, Map<String, Object> params) {
        Integer pieceNumber = getIntParam(params, "pieceNumber");
        Integer diceValue = getIntParam(params, "diceValue");
        
        // Parse current game state
        String currentState = game.getGameStateJson();
        if (currentState == null) {
            currentState = "{}";
        }
        
        // Calculate new position
        // TODO: Implement proper piece movement logic
        // - Get current piece position from game state
        // - Calculate new position based on dice value
        // - Check for captures
        // - Check if piece reaches home
        // - Update game state JSON
        
        String updatedState = updateStateWithMove(
            currentState, 
            player.getId(), 
            pieceNumber, 
            diceValue
        );
        
        game.setGameStateJson(updatedState);
        
        // Advance turn if didn't roll 6
        if (diceValue != 6) {
            advanceTurn(game);
        }
        
        System.out.println("Player " + player.getUsername() + 
                          " moved piece " + pieceNumber + 
                          " by " + diceValue + " steps");
    }
    
    @Override
    protected String getActionType() {
        return "MOVE_PIECE";
    }
    
    /**
     * Helper method to update game state with piece move.
     * In production, use proper JSON library and game state class.
     */
    private String updateStateWithMove(String currentState, Long playerId, 
                                      int pieceNumber, int diceValue) {
        // Simplified JSON update
        // TODO: Use proper JSON parsing and game state management
        return currentState + "{\"lastMove\":{\"playerId\":" + playerId + 
               ",\"piece\":" + pieceNumber + ",\"steps\":" + diceValue + "}}";
    }
    
    /**
     * Advance to next player's turn.
     */
    private void advanceTurn(Game game) {
        int currentTurn = game.getCurrentTurn();
        int nextTurn = (currentTurn + 1) % game.getCurrentPlayers();
        game.setCurrentTurn(nextTurn);
    }
}
