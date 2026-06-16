package com.ludoelite.model;

/**
 * Enum representing the state of a game piece.
 * 
 * Demonstrates: Encapsulation (controlled states).
 */
public enum PieceState {
    IN_BASE,        // Piece is in starting base/home
    ON_TRACK,       // Piece is on the main track
    IN_HOME_LANE,   // Piece is in the final home lane
    FINISHED        // Piece has reached the center
}
