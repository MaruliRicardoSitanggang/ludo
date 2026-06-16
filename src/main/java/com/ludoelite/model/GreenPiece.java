package com.ludoelite.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Concrete implementation of GamePiece for Green player.
 * 
 * Demonstrates:
 * - Inheritance: Extends GamePiece
 * - Polymorphism: Implements abstract render() method with Green-specific rendering
 */
public class GreenPiece extends GamePiece {
    
    public GreenPiece(int pieceNumber) {
        super(pieceNumber, PlayerColor.GREEN);
    }
    
    /**
     * Renders the green piece with a green circle and border.
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
        gc.setStroke(Color.rgb(0, 100, 0)); // Dark green border
        gc.setLineWidth(2);
        gc.strokeOval(x, y, size, size);
        
        // Center dot (piece number indicator)
        gc.setFill(Color.WHITE);
        gc.fillOval(x + size * 0.35, y + size * 0.35, size * 0.3, size * 0.3);
    }
}
