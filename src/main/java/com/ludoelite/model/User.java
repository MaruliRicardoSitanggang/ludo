package com.ludoelite.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * POJO representing a user account.
 * Maps to the backend /api/auth and /api/users payloads.
 *
 * Demonstrates: Encapsulation (private fields + getters/setters).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private Long   id;
    private String username;
    private String email;
    private String role;      // e.g. "ROLE_ADMIN", "ROLE_PLAYER"
    private String createdAt;

    // ── Constructors ─────────────────────────────────────────────────────────

    public User() {}

    public User(Long id, String username, String email, String role, String createdAt) {
        this.id        = id;
        this.username  = username;
        this.email     = email;
        this.role      = role;
        this.createdAt = createdAt;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────

    public Long getId()                  { return id; }
    public void setId(Long id)           { this.id = id; }

    public String getUsername()                    { return username; }
    public void   setUsername(String username)     { this.username = username; }

    public String getEmail()                   { return email; }
    public void   setEmail(String email)       { this.email = email; }

    public String getRole()                  { return role; }
    public void   setRole(String role)       { this.role = role; }

    public String getCreatedAt()                   { return createdAt; }
    public void   setCreatedAt(String createdAt)   { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', role='" + role + "'}";
    }
}
