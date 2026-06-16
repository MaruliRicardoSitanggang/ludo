package com.ludoelite.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * POJO representing a Ludo Game session entity.
 * Maps to backend /api/games payloads.
 *
 * Demonstrates: Encapsulation (private fields + getters/setters).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Game {

    private Long   id;
    private String gameName;
    private String status;       // e.g. "WAITING", "IN_PROGRESS", "FINISHED"
    private int    maxPlayers;
    private int    currentPlayers;
    private String hostUsername;
    private String createdAt;

    // ── Constructors ─────────────────────────────────────────────────────────

    public Game() {}

    public Game(Long id, String gameName, String status, int maxPlayers,
                int currentPlayers, String hostUsername, String createdAt) {
        this.id             = id;
        this.gameName       = gameName;
        this.status         = status;
        this.maxPlayers     = maxPlayers;
        this.currentPlayers = currentPlayers;
        this.hostUsername   = hostUsername;
        this.createdAt      = createdAt;
    }

    // ── Getters & Setters ────────────────────────────────────────────────────

    public Long getId()                { return id; }
    public void setId(Long id)         { this.id = id; }

    public String getGameName()                    { return gameName; }
    public void   setGameName(String gameName)     { this.gameName = gameName; }

    public String getStatus()                  { return status; }
    public void   setStatus(String status)     { this.status = status; }

    public int  getMaxPlayers()                    { return maxPlayers; }
    public void setMaxPlayers(int maxPlayers)      { this.maxPlayers = maxPlayers; }

    public int  getCurrentPlayers()                        { return currentPlayers; }
    public void setCurrentPlayers(int currentPlayers)      { this.currentPlayers = currentPlayers; }

    public String getHostUsername()                        { return hostUsername; }
    public void   setHostUsername(String hostUsername)     { this.hostUsername = hostUsername; }

    public String getCreatedAt()                   { return createdAt; }
    public void   setCreatedAt(String createdAt)   { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return gameName + " [" + status + "] (" + currentPlayers + "/" + maxPlayers + ")";
    }
}
