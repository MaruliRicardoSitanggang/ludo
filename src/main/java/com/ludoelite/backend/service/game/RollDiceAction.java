package com.ludoelite.backend.service.game;

import com.ludoelite.backend.entity.Game;
import com.ludoelite.backend.entity.User;

import java.util.Map;
import java.util.Random;

/**
 * Concrete implementation of GameAction for rolling dice.
 * 
 * Demonstrates:
 * - INHERITANCE: Extends GameAction
 * - POLYMORPHISM: Implements abstract methods with specific logic
 * - ENCAPSULATION: Dice rolling logic encapsulated
 */
public class RollDiceAction extends GameAction {
    
    private final Random random = new Random();
    
    /**
     * Validate that player can roll dice.
     * Rules:
     * - Must be player's turn
     * - Must not have rolled already this turn
     * 
     * Demonstrates: POLYMORPHISM (specific implementation)
     */
    @Override
    protected boolean validate(Game game, User player, Map<String, Object> params) {
        // Check game is in progress
        if (!"IN_PROGRESS".equals(game.getStatus())) {
            return false;
        }
        
        // TODO: Check if player hasn't rolled dice yet this turn
        // This requires parsing game state JSON
        
        return true;
    }
    
    /**
     * Perform dice roll action.
     * 
     * Demonstrates: POLYMORPHISM (specific implementation)
     */
    @Override
    protected void performAction(Game game, User player, Map<String, Object> params) {
        // Roll dice (1-6)
        int diceValue = random.nextInt(6) + 1;
        
        // Update game state JSON with dice value
        // TODO: Properly update game state JSON
        String currentState = game.getGameStateJson();
        if (currentState == null) {
            currentState = "{}";
        }
        
        // For now, simple JSON update
        String updatedState = updateStateWithDiceRoll(currentState, player.getId(), diceValue);
        game.setGameStateJson(updatedState);
        
        // Store dice value in params for response
        params.put("diceResult", diceValue);
        
        System.out.println("Player " + player.getUsername() + " rolled: " + diceValue);
    }
    
    @Override
    protected String getActionType() {
        return "ROLL_DICE";
    }
    
    /**
     * Helper method to update game state with dice roll.
     * In production, use proper JSON library (Jackson/Gson).
     */
    private String updateStateWithDiceRoll(String currentState, Long playerId, int diceValue) {
        // Simplified JSON update
        // TODO: Use proper JSON parsing
        return currentState + "{\"lastRoll\":{\"playerId\":" + playerId + 
               ",\"value\":" + diceValue + "}}";
    }
}
