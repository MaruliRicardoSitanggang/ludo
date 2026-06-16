package com.ludoelite.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the Ludo game.
 * 
 * Production version - Clean code without debug logging
 * Version: 4.0.0-PRODUCTION
 */
public class Player {

    private final PlayerColor color;
    private final String playerName;
    private final List<GamePiece> pieces;
    private boolean isCurrentTurn;

    public Player(PlayerColor color, String playerName) {
        this.color = color;
        this.playerName = playerName;
        this.pieces = new ArrayList<>();
        this.isCurrentTurn = false;

        // Initialize 4 pieces for this player
        for (int i = 0; i < 4; i++) {
            pieces.add(createPiece(color, i));
        }
    }

    /**
     * Factory method to create pieces based on color.
     */
    private GamePiece createPiece(PlayerColor color, int pieceNumber) {
        switch (color) {
            case RED:    return new RedPiece(pieceNumber);
            case BLUE:   return new BluePiece(pieceNumber);
            case GREEN:  return new GreenPiece(pieceNumber);
            case YELLOW: return new YellowPiece(pieceNumber);
            default:     throw new IllegalArgumentException("Unknown color: " + color);
        }
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public PlayerColor getColor() {
        return color;
    }

    public String getPlayerName() {
        return playerName;
    }

    public List<GamePiece> getPieces() {
        return new ArrayList<>(pieces);
    }

    public GamePiece getPiece(int index) {
        if (index >= 0 && index < pieces.size()) {
            return pieces.get(index);
        }
        return null;
    }

    public boolean isCurrentTurn() {
        return isCurrentTurn;
    }

    public void setCurrentTurn(boolean currentTurn) {
        isCurrentTurn = currentTurn;
    }

    /**
     * Checks if this player has won (all pieces finished).
     */
    public boolean hasWon() {
        return pieces.stream()
                .allMatch(piece -> piece.getState() == PieceState.FINISHED);
    }

    /**
     * Gets count of pieces still in base.
     */
    public int getPiecesInBase() {
        return (int) pieces.stream()
                .filter(piece -> piece.getState() == PieceState.IN_BASE)
                .count();
    }

    /**
     * Gets moveable pieces for the current dice value.
     * 
     * IMPORTANT: ALL positions 0-51 on main track are accessible.
     * This includes corner squares (12, 25, 38, 51) behind start positions.
     */
    public List<GamePiece> getMoveablePieces(int diceValue) {
        List<GamePiece> moveable = new ArrayList<>();

        for (GamePiece piece : pieces) {
            switch (piece.getState()) {
                case IN_BASE:
                    // Can only exit base with a 6
                    if (diceValue == 6) {
                        moveable.add(piece);
                    }
                    break;

                case ON_TRACK:
                    int newPos = piece.getTrackPosition() + diceValue;
                    
                    if (newPos >= 52) {
                        // Entering home lane - check doesn't overshoot
                        int stepsIntoHome = newPos - 52;
                        if (stepsIntoHome <= BoardTrack.HOME_LANE_SIZE) {
                            moveable.add(piece);
                        }
                    } else {
                        // Normal track movement
                        // ALL positions 0-51 are valid, including corners
                        moveable.add(piece);
                    }
                    break;

                case IN_HOME_LANE:
                    // Can move if not overshooting finish
                    int newHomeLanePos = piece.getTrackPosition() + diceValue;
                    if (newHomeLanePos <= BoardTrack.HOME_LANE_SIZE) {
                        moveable.add(piece);
                    }
                    break;

                case FINISHED:
                    // Finished pieces cannot move
                    break;
            }
        }

        return moveable;
    }

    @Override
    public String toString() {
        return color.getDisplayName() + " Player: " + playerName;
    }
}
