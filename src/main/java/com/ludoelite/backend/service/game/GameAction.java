package com.ludoelite.backend.service.game;

import com.ludoelite.backend.entity.Game;
import com.ludoelite.backend.entity.User;

import java.util.Map;

/**
 * Abstract base class for all game actions.
 * 
 * Demonstrates:
 * - ABSTRACTION: Abstract class with abstract methods
 * - ENCAPSULATION: Encapsulates action execution logic
 * - Template Method Pattern: execute() is template method
 * 
 * This is the foundation for Inheritance and Polymorphism in game logic.
 * All concrete action classes (RollDiceAction, MoveAction, etc.) 
 * will extend this class.
 */
public abstract class GameAction {
    
    /**
     * Template method that defines the execution flow.
     * This method is FINAL to enforce the algorithm structure.
     * 
     * Flow:
     * 1. Validate the action
     * 2. Perform the action
     * 3. Save move history
     * 4. Return updated game state
     * 
     * Demonstrates: Template Method Pattern
     */
    public final GameStateResponse execute(Game game, User player, Map<String, Object> params) {
        // Step 1: Validation
        if (!validate(game, player, params)) {
            throw new IllegalStateException("Invalid action: " + getActionType());
        }
        
        // Step 2: Check if it's player's turn
        if (!isPlayerTurn(game, player)) {
            throw new IllegalStateException("Not your turn!");
        }
        
        // Step 3: Perform specific action (polymorphic call)
        performAction(game, player, params);
        
        // Step 4: Check win condition
        checkWinCondition(game);
        
        // Step 5: Return updated state
        return createResponse(game);
    }
    
    /**
     * Abstract method: Validate if action can be performed.
     * Each concrete action implements its own validation logic.
     * 
     * Demonstrates: ABSTRACTION (must be implemented by children)
     */
    protected abstract boolean validate(Game game, User player, Map<String, Object> params);
    
    /**
     * Abstract method: Perform the specific action.
     * Each concrete action implements its own logic.
     * 
     * Demonstrates: ABSTRACTION & POLYMORPHISM
     */
    protected abstract void performAction(Game game, User player, Map<String, Object> params);
    
    /**
     * Abstract method: Get action type name.
     * Used for logging and history.
     */
    protected abstract String getActionType();
    
    /**
     * Concrete method: Check if it's the player's turn.
     * Shared logic used by all actions.
     * 
     * Demonstrates: Code reuse through inheritance
     */
    protected boolean isPlayerTurn(Game game, User player) {
        // Parse game state JSON and check current turn
        // For now, simplified version
        return true; // TODO: Implement proper turn checking
    }
    
    /**
     * Concrete method: Check if someone won the game.
     * Shared logic used by all actions.
     */
    protected void checkWinCondition(Game game) {
        // Parse game state and check if any player has all pieces finished
        // TODO: Implement win condition checking
    }
    
    /**
     * Concrete method: Create response with current game state.
     * Shared logic used by all actions.
     */
    protected GameStateResponse createResponse(Game game) {
        GameStateResponse response = new GameStateResponse();
        response.setGameId(game.getId());
        response.setStatus(game.getStatus());
        response.setCurrentTurn(game.getCurrentTurn());
        response.setGameStateJson(game.getGameStateJson());
        return response;
    }
    
    /**
     * Helper method to parse integer from params.
     */
    protected Integer getIntParam(Map<String, Object> params, String key) {
        Object value = params.get(key);
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof String) {
            return Integer.parseInt((String) value);
        }
        return null;
    }
}
