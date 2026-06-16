package com.ludoelite.util;

import com.ludoelite.model.User;

import java.util.prefs.Preferences;

/**
 * Singleton that manages the authenticated session.
 * Stores the JWT token in-memory and optionally persists it to Java Preferences
 * so the user stays logged in across restarts.
 *
 * Demonstrates: Encapsulation (private state), Singleton pattern.
 */
public class SessionManager {

    private static final String PREF_TOKEN_KEY = "jwt_token";
    private static final String PREF_USERNAME_KEY = "username";

    private static SessionManager instance;

    private String jwtToken;
    private User   currentUser;

    private final Preferences prefs = Preferences.userNodeForPackage(SessionManager.class);

    // Private constructor — Singleton
    private SessionManager() {
        // Restore persisted token on startup
        this.jwtToken = prefs.get(PREF_TOKEN_KEY, null);
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // ── Token management ────────────────────────────────────────────────────────

    public void saveSession(String token, User user) {
        this.jwtToken    = token;
        this.currentUser = user;
        prefs.put(PREF_TOKEN_KEY, token);
        prefs.put(PREF_USERNAME_KEY, user.getUsername());
    }

    public void clearSession() {
        this.jwtToken    = null;
        this.currentUser = null;
        prefs.remove(PREF_TOKEN_KEY);
        prefs.remove(PREF_USERNAME_KEY);
    }

    public String getJwtToken() {
        return jwtToken;
    }

    /**
     * Returns the Bearer authorization header value ready to attach to HTTP requests.
     */
    public String getBearerHeader() {
        return "Bearer " + jwtToken;
    }

    public boolean isLoggedIn() {
        return jwtToken != null && !jwtToken.isBlank();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public String getPersistedUsername() {
        return prefs.get(PREF_USERNAME_KEY, "");
    }

    /**
     * Returns the persisted token (same as getJwtToken in this implementation).
     * Used by WebSocketService for authentication.
     */
    public String getPersistedToken() {
        return jwtToken;
    }
}
