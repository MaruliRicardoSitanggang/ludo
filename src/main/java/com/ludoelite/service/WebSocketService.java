package com.ludoelite.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ludoelite.util.SessionManager;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * WebSocket Service for real-time communication with backend.
 * 
 * Handles:
 * - Connection to backend WebSocket
 * - JWT authentication
 * - STOMP message sending/receiving
 * - Subscription management
 * 
 * Demonstrates:
 * - Encapsulation: All WebSocket logic in one service
 * - Singleton pattern: One connection per session
 */
public class WebSocketService {
    
    private static WebSocketService instance;
    
    private final String wsUrl = "ws://localhost:8080/ws-ludo";
    private WebSocketStompClient stompClient;
    private StompSession stompSession;
    private final ObjectMapper objectMapper;
    private final Map<String, StompSession.Subscription> subscriptions;
    
    // Callbacks for connection events
    private Consumer<Void> onConnectedCallback;
    private Consumer<String> onErrorCallback;
    
    private WebSocketService() {
        this.objectMapper = new ObjectMapper();
        this.subscriptions = new HashMap<>();
        initializeStompClient();
    }
    
    public static synchronized WebSocketService getInstance() {
        if (instance == null) {
            instance = new WebSocketService();
        }
        return instance;
    }
    
    /**
     * Initialize STOMP client with Jackson message converter.
     */
    private void initializeStompClient() {
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        stompClient = new WebSocketStompClient(webSocketClient);
        
        // Set message converter for JSON
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setObjectMapper(objectMapper);
        stompClient.setMessageConverter(messageConverter);
    }
    
    /**
     * Connect to WebSocket with JWT authentication.
     * 
     * @param onConnected Callback when connected
     * @param onError Callback on error
     */
    public void connect(Consumer<Void> onConnected, Consumer<String> onError) {
        this.onConnectedCallback = onConnected;
        this.onErrorCallback = onError;
        
        // Get JWT token from session
        String token = SessionManager.getInstance().getPersistedToken();
        
        if (token == null || token.isEmpty()) {
            if (onError != null) {
                onError.accept("No authentication token found. Please login first.");
            }
            return;
        }
        
        // Create headers with JWT token
        StompHeaders headers = new StompHeaders();
        headers.add("Authorization", "Bearer " + token);
        
        // Create session handler
        StompSessionHandler sessionHandler = new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                stompSession = session;
                System.out.println("✅ WebSocket connected!");
                if (onConnected != null) {
                    onConnected.accept(null);
                }
            }
            
            @Override
            public void handleException(StompSession session, StompCommand command, 
                                       StompHeaders headers, byte[] payload, Throwable exception) {
                String error = "WebSocket error: " + exception.getMessage();
                System.err.println(error);
                if (onError != null) {
                    onError.accept(error);
                }
            }
            
            @Override
            public void handleTransportError(StompSession session, Throwable exception) {
                String error = "Transport error: " + exception.getMessage();
                System.err.println(error);
                if (onError != null) {
                    onError.accept(error);
                }
            }
        };
        
        // Connect asynchronously
        try {
            stompClient.connectAsync(wsUrl, (WebSocketHttpHeaders) null, headers, sessionHandler);
        } catch (Exception e) {
            String error = "Failed to connect: " + e.getMessage();
            System.err.println(error);
            if (onError != null) {
                onError.accept(error);
            }
        }
    }
    
    /**
     * Subscribe to a topic and receive messages.
     * 
     * @param destination Topic to subscribe (e.g., "/topic/game/123")
     * @param messageType Class type of message
     * @param handler Callback for received messages
     * @return Subscription ID
     */
    public <T> String subscribe(String destination, Class<T> messageType, Consumer<T> handler) {
        if (stompSession == null || !stompSession.isConnected()) {
            System.err.println("Cannot subscribe - not connected");
            return null;
        }
        
        StompSession.Subscription subscription = stompSession.subscribe(destination, 
            new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return messageType;
                }
                
                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    if (payload != null && messageType.isInstance(payload)) {
                        handler.accept(messageType.cast(payload));
                    }
                }
            });
        
        subscriptions.put(destination, subscription);
        System.out.println("✅ Subscribed to: " + destination);
        return destination;
    }
    
    /**
     * Unsubscribe from a topic.
     */
    public void unsubscribe(String subscriptionId) {
        StompSession.Subscription subscription = subscriptions.remove(subscriptionId);
        if (subscription != null) {
            subscription.unsubscribe();
            System.out.println("❌ Unsubscribed from: " + subscriptionId);
        }
    }
    
    /**
     * Send message to server.
     * 
     * @param destination Destination (e.g., "/app/game/123/move")
     * @param payload Message payload
     */
    public void send(String destination, Object payload) {
        if (stompSession == null || !stompSession.isConnected()) {
            System.err.println("Cannot send - not connected");
            return;
        }
        
        try {
            stompSession.send(destination, payload);
            System.out.println("📤 Sent to " + destination + ": " + payload);
        } catch (Exception e) {
            System.err.println("Failed to send message: " + e.getMessage());
        }
    }
    
    /**
     * Disconnect from WebSocket.
     */
    public void disconnect() {
        // Unsubscribe all
        subscriptions.values().forEach(StompSession.Subscription::unsubscribe);
        subscriptions.clear();
        
        // Disconnect session
        if (stompSession != null && stompSession.isConnected()) {
            stompSession.disconnect();
            stompSession = null;
        }
        
        System.out.println("❌ WebSocket disconnected");
    }
    
    /**
     * Check if connected.
     */
    public boolean isConnected() {
        return stompSession != null && stompSession.isConnected();
    }
    
    /**
     * Get current session.
     */
    public StompSession getSession() {
        return stompSession;
    }
}
