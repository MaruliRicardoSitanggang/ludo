package com.ludoelite.backend.controller;

import com.ludoelite.backend.entity.User;
import com.ludoelite.backend.repository.UserRepository;
import com.ludoelite.backend.service.game.GameStateResponse;
import com.ludoelite.backend.service.game.LudoGameService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket controller for real-time game communication.
 * 
 * Endpoints:
 * - /app/game/{gameId}/roll: Roll dice
 * - /app/game/{gameId}/move: Move piece
 * - /app/game/{gameId}/join: Join game
 * 
 * Broadcasts to:
 * - /topic/game/{gameId}: All players in game receive updates
 * 
 * Demonstrates:
 * - WebSocket STOMP messaging
 * - Real-time multiplayer communication
 * - Server as source of truth
 */
@Controller
public class GameWebSocketController {
    
    private final LudoGameService gameService;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    
    public GameWebSocketController(LudoGameService gameService,
                                   UserRepository userRepository,
                                   SimpMessagingTemplate messagingTemplate) {
        this.gameService = gameService;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }
    
    /**
     * Handle dice roll request from client.
     * 
     * Client sends to: /app/game/{gameId}/roll
     * Server broadcasts to: /topic/game/{gameId}
     * 
     * @param gameId Game ID from URL
     * @param principal Authenticated user (from JWT)
     */
    @MessageMapping("/game/{gameId}/roll")
    public void rollDice(@DestinationVariable Long gameId, 
                        Principal principal) {
        try {
            // Get authenticated user
            User user = getUserFromPrincipal(principal);
            
            // Process roll dice action (uses polymorphism)
            GameStateResponse response = gameService.rollDice(gameId, user);
            
            // Broadcast updated state to all players in this game
            messagingTemplate.convertAndSend(
                "/topic/game/" + gameId, 
                response
            );
            
            System.out.println("Dice rolled in game " + gameId + " by " + user.getUsername());
            
        } catch (Exception e) {
            // Send error to specific user only
            sendErrorToUser(principal, "Failed to roll dice: " + e.getMessage());
        }
    }
    
    /**
     * Handle move piece request from client.
     * 
     * Client sends to: /app/game/{gameId}/move
     * Server broadcasts to: /topic/game/{gameId}
     * 
     * @param gameId Game ID from URL
     * @param moveRequest Move details (pieceNumber, diceValue)
     * @param principal Authenticated user
     */
    @MessageMapping("/game/{gameId}/move")
    public void movePiece(@DestinationVariable Long gameId,
                         @Payload MoveRequest moveRequest,
                         Principal principal) {
        try {
            // Get authenticated user
            User user = getUserFromPrincipal(principal);
            
            // Process move action (uses polymorphism)
            GameStateResponse response = gameService.movePiece(
                gameId, 
                user, 
                moveRequest.getPieceNumber(), 
                moveRequest.getDiceValue()
            );
            
            // Broadcast updated state to all players
            messagingTemplate.convertAndSend(
                "/topic/game/" + gameId, 
                response
            );
            
            System.out.println("Piece moved in game " + gameId + " by " + user.getUsername());
            
        } catch (Exception e) {
            sendErrorToUser(principal, "Failed to move piece: " + e.getMessage());
        }
    }
    
    /**
     * Handle join game request.
     * 
     * Client sends to: /app/game/{gameId}/join
     */
    @MessageMapping("/game/{gameId}/join")
    public void joinGame(@DestinationVariable Long gameId,
                        Principal principal) {
        try {
            User user = getUserFromPrincipal(principal);
            
            // TODO: Implement join game logic
            // - Add user to game
            // - Assign player color
            // - Update game state
            
            GameStateResponse response = new GameStateResponse("Player joined: " + user.getUsername());
            response.setGameId(gameId);
            response.setTimestamp(System.currentTimeMillis());
            
            messagingTemplate.convertAndSend("/topic/game/" + gameId, response);
            
        } catch (Exception e) {
            sendErrorToUser(principal, "Failed to join game: " + e.getMessage());
        }
    }
    
    /**
     * Get user from Spring Security Principal.
     * Principal contains JWT token information.
     */
    private User getUserFromPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalStateException("User not found: " + username));
    }
    
    /**
     * Send error message to specific user only.
     */
    @SendToUser("/queue/errors")
    private void sendErrorToUser(Principal principal, String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        error.put("timestamp", String.valueOf(System.currentTimeMillis()));
        
        messagingTemplate.convertAndSendToUser(
            principal.getName(),
            "/queue/errors",
            error
        );
    }
    
    /**
     * Inner class for move request DTO.
     */
    public static class MoveRequest {
        private int pieceNumber;
        private int diceValue;
        
        public int getPieceNumber() { return pieceNumber; }
        public void setPieceNumber(int pieceNumber) { this.pieceNumber = pieceNumber; }
        
        public int getDiceValue() { return diceValue; }
        public void setDiceValue(int diceValue) { this.diceValue = diceValue; }
    }
}
