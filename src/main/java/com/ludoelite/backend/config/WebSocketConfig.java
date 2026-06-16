package com.ludoelite.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket configuration for real-time game communication.
 *
 * Configures:
 * - STOMP endpoint at /ws-ludo
 * - Message broker for /topic and /queue destinations
 * - Application destination prefix /app
 *
 * Demonstrates:
 * - Spring WebSocket configuration
 * - STOMP protocol setup
 * - Cross-origin support
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configure message broker.
     *
     * - /topic: Used for broadcasting to multiple subscribers (all players in a game)
     * - /queue: Used for point-to-point messaging (errors to specific user)
     * - /app: Prefix for messages bound for @MessageMapping methods
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable simple in-memory message broker
        // Messages to /topic or /queue will be routed to subscribers
        config.enableSimpleBroker("/topic", "/queue");

        // Application destination prefix
        // Client sends to /app/game/1/roll -> routed to @MessageMapping("/game/{gameId}/roll")
        config.setApplicationDestinationPrefixes("/app");

        // User destination prefix for point-to-point messaging
        config.setUserDestinationPrefix("/user");
    }

    /**
     * Register STOMP endpoints.
     *
     * Clients connect to ws://localhost:8080/ws-ludo
     * With SockJS fallback for browsers that don't support WebSocket
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-ludo")
                .setAllowedOriginPatterns("*") // Allow all origins for development
                .withSockJS(); // Enable SockJS fallback

        // Also add endpoint without SockJS for native WebSocket clients
        registry.addEndpoint("/ws-ludo")
                .setAllowedOriginPatterns("*");
    }
}
