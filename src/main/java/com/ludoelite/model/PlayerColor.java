package com.ludoelite.model;

/**
 * Enum representing the four player colors in Ludo.
 * 
 * Demonstrates: Encapsulation (controlled set of values).
 */
public enum PlayerColor {
    RED("#ef4444", "Red"),
    BLUE("#3b82f6", "Blue"),
    GREEN("#22c55e", "Green"),
    YELLOW("#f59e0b", "Yellow");
    
    private final String hexColor;
    private final String displayName;
    
    PlayerColor(String hexColor, String displayName) {
        this.hexColor = hexColor;
        this.displayName = displayName;
    }
    
    public String getHexColor() {
        return hexColor;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Returns the next player color in turn order.
     */
    public PlayerColor getNext() {
        return values()[(this.ordinal() + 1) % values().length];
    }
}
