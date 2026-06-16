package com.ludoelite.model;

import javafx.scene.canvas.GraphicsContext;

/**
 * Abstract base class for all Ludo game pieces.
 * 
 * Demonstrates:
 * - Abstraction: Abstract class with abstract method render()
 * - Encapsulation: Private fields with controlled access
 * 
 * This serves as the foundation for all colored pieces and enforces
 * a contract that all pieces must implement their own rendering logic.
 */
public abstract class GamePiece {
    
    // Encapsulated fields
    private final int pieceNumber;       // 0-3 for each player
    private final PlayerColor ownerColor;
    private PieceState state;
    private int trackPosition;           // Position on the track (0-51 for main track)
    private Position boardPosition;      // UI position on canvas
    
    /**
     * Constructor for GamePiece.
     * 
     * @param pieceNumber The piece number (0-3)
     * @param ownerColor  The color of the player who owns this piece
     */
    protected GamePiece(int pieceNumber, PlayerColor ownerColor) {
        this.pieceNumber = pieceNumber;
        this.ownerColor = ownerColor;
        this.state = PieceState.IN_BASE;
        this.trackPosition = -1; // -1 means not on track
        this.boardPosition = null;
    }
    
    // ── Abstract method (forces subclasses to implement) ─────────────────────
    
    /**
     * Renders this piece on the canvas.
     * Each colored piece will implement its own rendering logic.
     * 
     * Demonstrates: Abstraction & Polymorphism
     */
    public abstract void render(GraphicsContext gc, double x, double y, double size);
    
    // ── Encapsulated getters and setters ─────────────────────────────────────
    
    public int getPieceNumber() {
        return pieceNumber;
    }
    
    public PlayerColor getOwnerColor() {
        return ownerColor;
    }
    
    public PieceState getState() {
        return state;
    }
    
    public void setState(PieceState state) {
        this.state = state;
    }
    
    public int getTrackPosition() {
        return trackPosition;
    }
    
    public void setTrackPosition(int trackPosition) {
        this.trackPosition = trackPosition;
    }
    
    public Position getBoardPosition() {
        return boardPosition;
    }
    
    public void setBoardPosition(Position boardPosition) {
        this.boardPosition = boardPosition;
    }
    
    /**
     * Checks if this piece can move based on current state.
     */
    public boolean canMove() {
        return state != PieceState.FINISHED;
    }
    
    /**
     * Moves the piece by the given number of steps.
     * Returns true if move was successful.
     */
    public boolean move(int steps) {
        if (!canMove()) {
            return false;
        }
        
        // If in base, needs a 6 to exit
        if (state == PieceState.IN_BASE) {
            if (steps == 6) {
                exitBase();
                return true;
            }
            return false;
        }
        
        // Move on track
        trackPosition += steps;
        return true;
    }
    
    /**
     * Exits the piece from base to the starting position.
     */
    protected void exitBase() {
        state = PieceState.ON_TRACK;
        trackPosition = 0;
    }
    
    /**
     * Sends the piece back to base (when captured).
     */
    public void sendToBase() {
        state = PieceState.IN_BASE;
        trackPosition = -1;
        boardPosition = null;
    }
    
    @Override
    public String toString() {
        return ownerColor.getDisplayName() + " Piece #" + pieceNumber + 
               " [State: " + state + ", Position: " + trackPosition + "]";
    }
}
