package com.ludoelite.model;

import java.util.Random;

/**
 * Represents a six-sided dice.
 * 
 * Demonstrates: Encapsulation (dice roll logic is encapsulated).
 */
public class Dice {
    
    private final Random random;
    private int currentValue;
    
    public Dice() {
        this.random = new Random();
        this.currentValue = 1;
    }
    
    /**
     * Rolls the dice and returns a value between 1 and 6.
     */
    public int roll() {
        currentValue = random.nextInt(6) + 1;
        return currentValue;
    }
    
    /**
     * Gets the current value without rolling.
     */
    public int getCurrentValue() {
        return currentValue;
    }
    
    /**
     * Checks if the current value is 6.
     */
    public boolean isSix() {
        return currentValue == 6;
    }
    
    @Override
    public String toString() {
        return "Dice: " + currentValue;
    }
}
