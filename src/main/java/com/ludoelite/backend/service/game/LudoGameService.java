package com.ludoelite.backend.service.game;

import com.ludoelite.backend.entity.Game;
import com.ludoelite.backend.entity.MoveHistory;
import com.ludoelite.backend.entity.User;
import com.ludoelite.backend.repository.GameRepository;
import com.ludoelite.backend.repository.MoveHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Main game service that orchestrates game logic.
 * 
 * Demonstrates:
 * - POLYMORPHISM: Uses GameAction polymorphically
 * - ENCAPSULATION: Encapsulates game business logic
 * - Strategy Pattern: Different actions via polymorphic dispatch
 * 
 * This is the SERVER as SOURCE OF TRUTH.
 * All game state changes happen here and are validated.
 */
@Service
public class LudoGameService {
    
    private final GameRepository gameRepository;
    private final MoveHistoryRepository moveHistoryRepository;
    
    // Map of action types to action handlers
    // Demonstrates: POLYMORPHISM - Different types stored as GameAction
    private final Map<String, GameAction> actionHandlers;
    
    public LudoGameService(GameRepository gameRepository, 
                          MoveHistoryRepository moveHistoryRepository) {
        this.gameRepository = gameRepository;
        this.moveHistoryRepository = moveHistoryRepository;
        
        // Initialize action handlers (Polymorphic container)
        this.actionHandlers = new HashMap<>();
        this.actionHandlers.put("ROLL", new RollDiceAction());
        this.actionHandlers.put("MOVE", new MoveAction());
        
        // Can easily add more action types:
        // actionHandlers.put("CHAT", new ChatAction());
        // actionHandlers.put("EMOJI", new EmojiAction());
    }
    
    /**
     * Process any game action using polymorphic dispatch.
     * 
     * Demonstrates:
     * - POLYMORPHISM: Calls execute() on different action types
     * - Strategy Pattern: Action selected at runtime
     * 
     * @param actionType Type of action (ROLL, MOVE, etc.)
     * @param gameId Game ID
     * @param user Player performing action
     * @param params Action parameters
     * @return Updated game state
     */
    @Transactional
    public GameStateResponse processAction(String actionType, Long gameId, 
                                          User user, Map<String, Object> params) {
        // Find game
        Game game = gameRepository.findById(gameId)
            .orElseThrow(() -> new IllegalArgumentException("Game not found: " + gameId));
        
        // Get appropriate action handler (Polymorphic retrieval)
        GameAction action = actionHandlers.get(actionType);
        if (action == null) {
            throw new IllegalArgumentException("Unknown action type: " + actionType);
        }
        
        // Execute action (Polymorphic call - different implementations)
        GameStateResponse response = action.execute(game, user, params);
        
        // Save updated game state
        gameRepository.save(game);
        
        // Save move to history for audit trail
        saveMoveHistory(game, user, actionType, params);
        
        return response;
    }
    
    /**
     * Roll dice for a player.
     * Convenience method that calls processAction.
     */
    @Transactional
    public GameStateResponse rollDice(Long gameId, User user) {
        return processAction("ROLL", gameId, user, new HashMap<>());
    }
    
    /**
     * Move a piece for a player.
     * Convenience method that calls processAction.
     */
    @Transactional
    public GameStateResponse movePiece(Long gameId, User user, 
                                      int pieceNumber, int diceValue) {
        Map<String, Object> params = new HashMap<>();
        params.put("pieceNumber", pieceNumber);
        params.put("diceValue", diceValue);
        return processAction("MOVE", gameId, user, params);
    }
    
    /**
     * Save move to history for audit trail and replay.
     */
    private void saveMoveHistory(Game game, User user, String actionType, 
                                Map<String, Object> params) {
        MoveHistory move = new MoveHistory();
        move.setGame(game);
        move.setPlayer(user);
        move.setActionType(actionType);
        
        // Extract relevant params based on action type
        if ("ROLL".equals(actionType) && params.containsKey("diceResult")) {
            move.setDiceValue((Integer) params.get("diceResult"));
        } else if ("MOVE".equals(actionType)) {
            move.setPieceNumber((Integer) params.get("pieceNumber"));
            move.setDiceValue((Integer) params.get("diceValue"));
            // TODO: Set from/to positions
        }
        
        moveHistoryRepository.save(move);
    }
    
    /**
     * Get game state for a specific game.
     */
    public GameStateResponse getGameState(Long gameId) {
        Game game = gameRepository.findById(gameId)
            .orElseThrow(() -> new IllegalArgumentException("Game not found: " + gameId));
        
        GameStateResponse response = new GameStateResponse();
        response.setGameId(game.getId());
        response.setStatus(game.getStatus());
        response.setCurrentTurn(game.getCurrentTurn());
        response.setGameStateJson(game.getGameStateJson());
        response.setTimestamp(System.currentTimeMillis());
        
        return response;
    }
}
