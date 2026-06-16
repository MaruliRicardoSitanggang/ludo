package com.ludoelite.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the Ludo board track system with 52 main positions
 * plus home lanes and safe zones.
 *
 * Demonstrates:
 * - Encapsulation: Track positions and safe zones are encapsulated
 * - Single Responsibility: Only handles board track logic
 */
public class BoardTrack {

    // Safe zones (cannot be captured)
    // STAR SAFE ZONES: 9, 22, 35, 48 (safe untuk SEMUA warna)
    // START POSITIONS: 0, 13, 26, 39 (safe HANYA untuk pemilik warna)
    private static final int[] STAR_SAFE_ZONES = {9, 22, 35, 48};

    // Starting positions for each color - LAYOUT KLASIK
    private static final Map<PlayerColor, Integer> START_POSITIONS = new HashMap<>();

    static {
        START_POSITIONS.put(PlayerColor.RED, 0);      // [1, 6]
        START_POSITIONS.put(PlayerColor.GREEN, 13);   // [8, 1]
        START_POSITIONS.put(PlayerColor.BLUE, 26);    // [13, 8]
        START_POSITIONS.put(PlayerColor.YELLOW, 39);  // [6, 13]
    }

    public static final int MAIN_TRACK_SIZE = 52;
    public static final int HOME_LANE_SIZE = 5;  // 5 steps to reach center, position 5 = finish [7,7]

    // IMPORTANT: Track has 52 positions (0-51), ALL accessible during gameplay
    // Pieces enter their home lane AFTER completing a full lap (51 steps)
    // Only when reaching their specific home entry position

    /**
     * Checks if a position is a safe zone.
     *
     * ATURAN PROFESSIONAL:
     * - Star safe zones (9, 22, 35, 48): safe untuk SEMUA warna
     * - Start positions (0, 13, 26, 39): safe HANYA untuk warna pemilik
     *
     * @param position Absolute position to check
     * @param movingPieceColor Color of the piece that is moving (for start square check)
     * @return true if position is safe from capture
     */
    public static boolean isSafeZone(int position, PlayerColor movingPieceColor) {
        // Star safe zones: safe untuk SEMUA warna
        for (int starSafe : STAR_SAFE_ZONES) {
            if (starSafe == position) {
                return true;
            }
        }

        // Start square: safe HANYA untuk warna pemilik
        // Contoh: RED piece di start RED (abs 0) = safe
        //         YELLOW piece di start RED (abs 0) = NOT safe (bisa di-capture)
        int ownerStart = getStartPosition(movingPieceColor);
        if (position == ownerStart) {
            return true;
        }

        return false;
    }

    /**
     * Legacy method untuk backward compatibility.
     * Deprecated: Use isSafeZone(int, PlayerColor) instead.
     */
    @Deprecated
    public static boolean isSafeZone(int position) {
        // Star safe zones only (start squares tidak dihitung safe untuk semua)
        for (int starSafe : STAR_SAFE_ZONES) {
            if (starSafe == position) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the starting position for a given color.
     */
    public static int getStartPosition(PlayerColor color) {
        return START_POSITIONS.getOrDefault(color, 0);
    }

    /**
     * Normalizes track position (wraps around at 52).
     * Positions 0-51 are valid track positions (52 total).
     * Position >= 52 should wrap or enter home lane.
     */
    public static int normalizePosition(int position) {
        if (position < 0) {
            return 0;
        }
        // Wrap around: position 52 wraps to 0, position 53 wraps to 1, etc.
        // BUT in game logic, position >= 52 triggers home lane entry
        if (position >= MAIN_TRACK_SIZE) {
            return position % MAIN_TRACK_SIZE;
        }
        return position;
    }

    /**
     * Gets the home lane entry position for each color (absolute position).
     * This is the position where pieces turn off the main track into home lane.
     * It's always ONE POSITION BEFORE the start position.
     */
    public static int getHomeLaneEntryPosition(PlayerColor color) {
        int start = getStartPosition(color);
        // Home entry is one position before start (wrapping at 0)
        return (start + 51) % MAIN_TRACK_SIZE;
    }

    /**
     * Calculates absolute position from player's perspective.
     * Each player sees position 0 as their starting point.
     */
    public static int getAbsolutePosition(PlayerColor color, int relativePosition) {
        int start = getStartPosition(color);
        return normalizePosition(start + relativePosition);
    }
}