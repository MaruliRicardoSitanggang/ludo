package com.ludoelite.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Concrete implementation of GamePiece for Red player.
 * 
 * Demonstrates:
 * - Inheritance: Extends GamePiece
 * - Polymorphism: Implements abstract render() method with Red-specific rendering
 */
public class RedPiece extends GamePiece {
    
    public RedPiece(int pieceNumber) {
        super(pieceNumber, PlayerColor.RED);
    }
    
    /**
     * Renders the red piece with a red circle and border.
     * 
     * Demonstrates: Polymorphism (specific implementation of abstract method)
     */
    @Override
    public void render(GraphicsContext gc, double x, double y, double size) {
        // Shadow effect
        gc.setFill(Color.rgb(0, 0, 0, 0.3));
        gc.fillOval(x + 2, y + 2, size, size);
        
        // Main piece body
        gc.setFill(Color.web(getOwnerColor().getHexColor()));
        gc.fillOval(x, y, size, size);
        
        // Border
        gc.setStroke(Color.rgb(139, 0, 0)); // Dark red border
        gc.setLineWidth(2);
        gc.strokeOval(x, y, size, size);
        
        // Center dot (piece number indicator)
        gc.setFill(Color.WHITE);
        gc.fillOval(x + size * 0.35, y + size * 0.35, size * 0.3, size * 0.3);
    }
}
