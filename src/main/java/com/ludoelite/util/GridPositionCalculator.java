package com.ludoelite.util;

import com.ludoelite.model.*;

import java.util.HashMap;
import java.util.Map;

/**
 * GridPositionCalculator - PAPAN LUDO KLASIK STANDARD
 *
 * Grid 15x15 (koordinat 0-14)
 * Layout Klasik:
 * - MERAH: Kiri-Atas (0-5, 0-5)
 * - HIJAU: Kanan-Atas (9-14, 0-5)
 * - BIRU: Kanan-Bawah (9-14, 9-14)
 * - KUNING: Kiri-Bawah (0-5, 9-14)
 *
 * Track: 52 posisi clockwise mengelilingi papan
 *
 * @version 4.0 - Classic Ludo Standard
 */
public class GridPositionCalculator {

    public static final int GRID_SIZE = 15;
    public static final int CELL_SIZE = 40;
    public static final int BOARD_SIZE = GRID_SIZE * CELL_SIZE;

    // 52 posisi track clockwise (hardcoded sesuai papan Ludo klasik)
    private static final Map<Integer, int[]> TRACK_GRID = new HashMap<>();

    static {
        initializeTrackGrid();

        // DEBUG: Verify corner positions are initialized
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("TRACK_GRID INITIALIZATION CHECK");
        System.out.println("Total positions: " + TRACK_GRID.size());
        System.out.println("Corner positions:");
        System.out.println("  Pos 12 (corner atas): " + java.util.Arrays.toString(TRACK_GRID.get(12)));
        System.out.println("  Pos 25 (corner kanan): " + java.util.Arrays.toString(TRACK_GRID.get(25)));
        System.out.println("  Pos 38 (corner bawah): " + java.util.Arrays.toString(TRACK_GRID.get(38)));
        System.out.println("  Pos 51 (corner kiri): " + java.util.Arrays.toString(TRACK_GRID.get(51)));
        System.out.println("Checking nearby positions:");
        System.out.println("  Pos 49: " + java.util.Arrays.toString(TRACK_GRID.get(49)));
        System.out.println("  Pos 50: " + java.util.Arrays.toString(TRACK_GRID.get(50)));
        System.out.println("═══════════════════════════════════════════════════════");
    }

    /**
     * Inisialisasi 52 posisi track - PAPAN LUDO KLASIK STANDAR
     * FIXED: Include ALL corner positions - no skips!
     *
     * Jalur clockwise mengelilingi papan, dimulai dari MERAH.
     *
     * START POSITIONS (Titik keluar dari base):
     * - MERAH: [1, 6] (Position 0)
     * - HIJAU: [8, 1] (Position 13)
     * - BIRU: [13, 8] (Position 26)
     * - KUNING: [6, 13] (Position 39)
     */
    private static void initializeTrackGrid() {
        int pos = 0;

        // ========== MERAH START & JALUR KE ATAS (0-12) ==========
        // Mulai dari [1,6], bergerak ke KANAN menuju tengah, lalu NAIK
        TRACK_GRID.put(pos++, new int[]{1, 6});   // 0: MERAH START ★
        TRACK_GRID.put(pos++, new int[]{2, 6});   // 1
        TRACK_GRID.put(pos++, new int[]{3, 6});   // 2
        TRACK_GRID.put(pos++, new int[]{4, 6});   // 3
        TRACK_GRID.put(pos++, new int[]{5, 6});   // 4
        TRACK_GRID.put(pos++, new int[]{6, 6});   // 5: Corner (masuk lengan atas)

        // Naik di lengan atas (kolom 6)
        TRACK_GRID.put(pos++, new int[]{6, 5});   // 6
        TRACK_GRID.put(pos++, new int[]{6, 4});   // 7
        TRACK_GRID.put(pos++, new int[]{6, 3});   // 8
        TRACK_GRID.put(pos++, new int[]{6, 2});   // 9: SAFE ★
        TRACK_GRID.put(pos++, new int[]{6, 1});   // 10
        TRACK_GRID.put(pos++, new int[]{6, 0});   // 11: Ujung atas

        // Belok kanan - INCLUDE CORNER POSITION!
        TRACK_GRID.put(pos++, new int[]{7, 0});   // 12: Corner ✅ PENTING!

        // ========== HIJAU START & JALUR KE KANAN (13-25) ==========
        // Mulai dari [8,1], bergerak ke BAWAH menuju tengah, lalu KE KANAN
        TRACK_GRID.put(pos++, new int[]{8, 1});   // 13: HIJAU START ★
        TRACK_GRID.put(pos++, new int[]{8, 2});   // 14
        TRACK_GRID.put(pos++, new int[]{8, 3});   // 15
        TRACK_GRID.put(pos++, new int[]{8, 4});   // 16
        TRACK_GRID.put(pos++, new int[]{8, 5});   // 17
        TRACK_GRID.put(pos++, new int[]{8, 6});   // 18: Corner (masuk lengan kanan)

        // Ke kanan di lengan kanan (row 6, col 8→14)
        TRACK_GRID.put(pos++, new int[]{9, 6});   // 19
        TRACK_GRID.put(pos++, new int[]{10, 6});  // 20
        TRACK_GRID.put(pos++, new int[]{11, 6});  // 21
        TRACK_GRID.put(pos++, new int[]{12, 6});  // 22: SAFE ★
        TRACK_GRID.put(pos++, new int[]{13, 6});  // 23
        TRACK_GRID.put(pos++, new int[]{14, 6});  // 24: Ujung kanan

        // Belok bawah - INCLUDE CORNER POSITION!
        TRACK_GRID.put(pos++, new int[]{14, 7});  // 25: Corner ✅ PENTING!

        // ========== BIRU START & JALUR KE BAWAH (26-38) ==========
        // Mulai dari [13,8], bergerak ke KIRI menuju tengah, lalu TURUN
        TRACK_GRID.put(pos++, new int[]{13, 8});  // 26: BIRU START ★
        TRACK_GRID.put(pos++, new int[]{12, 8});  // 27
        TRACK_GRID.put(pos++, new int[]{11, 8});  // 28
        TRACK_GRID.put(pos++, new int[]{10, 8});  // 29
        TRACK_GRID.put(pos++, new int[]{9, 8});   // 30
        TRACK_GRID.put(pos++, new int[]{8, 8});   // 31: Corner (masuk lengan bawah)

        // Turun di lengan bawah (col 8, row 8→14)
        TRACK_GRID.put(pos++, new int[]{8, 9});   // 32
        TRACK_GRID.put(pos++, new int[]{8, 10});  // 33
        TRACK_GRID.put(pos++, new int[]{8, 11});  // 34
        TRACK_GRID.put(pos++, new int[]{8, 12});  // 35: SAFE ★
        TRACK_GRID.put(pos++, new int[]{8, 13});  // 36
        TRACK_GRID.put(pos++, new int[]{8, 14});  // 37: Ujung bawah

        // Belok kiri - INCLUDE CORNER POSITION!
        TRACK_GRID.put(pos++, new int[]{7, 14});  // 38: Corner ✅ PENTING!

        // ========== KUNING START & JALUR KE KIRI (39-51) ==========
        // Mulai dari [6,13], bergerak ke ATAS menuju tengah, lalu KE KIRI
        TRACK_GRID.put(pos++, new int[]{6, 13});  // 39: KUNING START ★
        TRACK_GRID.put(pos++, new int[]{6, 12});  // 40
        TRACK_GRID.put(pos++, new int[]{6, 11});  // 41
        TRACK_GRID.put(pos++, new int[]{6, 10});  // 42
        TRACK_GRID.put(pos++, new int[]{6, 9});   // 43
        TRACK_GRID.put(pos++, new int[]{6, 8});   // 44: Corner (masuk lengan kiri)

        // Ke kiri di lengan kiri (row 8, col 6→0)
        TRACK_GRID.put(pos++, new int[]{5, 8});   // 45
        TRACK_GRID.put(pos++, new int[]{4, 8});   // 46
        TRACK_GRID.put(pos++, new int[]{3, 8});   // 47
        TRACK_GRID.put(pos++, new int[]{2, 8});   // 48: SAFE ★
        TRACK_GRID.put(pos++, new int[]{1, 8});   // 49
        TRACK_GRID.put(pos++, new int[]{0, 8});   // 50: Ujung kiri

        // Belok naik - INCLUDE CORNER POSITION!
        TRACK_GRID.put(pos++, new int[]{0, 7});   // 51: Corner ✅ PENTING!

        // Position 52 (setelah 51) = masuk home lane

        // TOTAL: 52 positions (0-51), ALL positions accessible!
        // Corner positions (12, 25, 38, 51) are ONE SQUARE BEHIND start positions
        // These ARE part of the track and SHOULD be accessible!
    }

    /**
     * Mendapatkan koordinat grid [x, y] untuk piece.
     *
     * DEBUG MODE: Extensive logging untuk troubleshoot rendering issues.
     */
    public static int[] getPieceGridPosition(GamePiece piece) {
        PieceState state = piece.getState();

        switch (state) {
            case IN_BASE:
                return getBaseGridPosition(piece);
            case ON_TRACK:
                // Piece trackPosition can be relative 0-51
                // Must convert to absolute position for rendering
                int relPos = piece.getTrackPosition();
                int absPos = BoardTrack.getAbsolutePosition(
                        piece.getOwnerColor(),
                        relPos
                );

                // Debug log
                int[] gridPos = TRACK_GRID.get(absPos);
                System.out.printf("[RENDER] %s piece #%d: rel=%d → abs=%d → grid=[%s]%n",
                        piece.getOwnerColor(), piece.getPieceNumber(),
                        relPos, absPos,
                        gridPos != null ? gridPos[0] + "," + gridPos[1] : "NULL!");

                if (gridPos == null) {
                    System.err.printf("❌ ERROR: No grid position for abs pos %d!%n", absPos);
                }

                // absPos is already normalized by getAbsolutePosition()
                return gridPos;
            case IN_HOME_LANE:
                return getHomeLaneGridPosition(piece);
            case FINISHED:
                return new int[]{7, 7};
            default:
                return null;
        }
    }

    /**
     * Grid position untuk piece di base - HARDCODED ke grid absolut.
     * Layout Klasik: MERAH (kiri-atas), HIJAU (kanan-atas), BIRU (kanan-bawah), KUNING (kiri-bawah)
     */
    private static int[] getBaseGridPosition(GamePiece piece) {
        int pieceNum = piece.getPieceNumber();

        // Offset dalam base 6x6 (slots di posisi {1,1}, {4,1}, {1,4}, {4,4})
        int[] offset;
        switch (pieceNum) {
            case 0: offset = new int[]{1, 1}; break;
            case 1: offset = new int[]{4, 1}; break;
            case 2: offset = new int[]{1, 4}; break;
            case 3: offset = new int[]{4, 4}; break;
            default: offset = new int[]{1, 1};
        }

        // Base positions - LAYOUT KLASIK!
        int baseX, baseY;
        switch (piece.getOwnerColor()) {
            case RED:
                baseX = 0;  baseY = 0;  // Kiri-Atas
                break;
            case GREEN:
                baseX = 9;  baseY = 0;  // Kanan-Atas
                break;
            case BLUE:
                baseX = 9;  baseY = 9;  // Kanan-Bawah
                break;
            case YELLOW:
                baseX = 0;  baseY = 9;  // Kiri-Bawah
                break;
            default:
                baseX = 0;  baseY = 0;
        }

        return new int[]{baseX + offset[0], baseY + offset[1]};
    }

    /**
     * Grid position untuk piece di home lane - LAYOUT KLASIK.
     *
     * Home Lanes menuju center [7,7]:
     * - MERAH: Y=7, X dari 1→5
     * - HIJAU: X=7, Y dari 1→5
     * - BIRU: Y=7, X dari 13→9
     * - KUNING: X=7, Y dari 13→9
     */
    private static int[] getHomeLaneGridPosition(GamePiece piece) {
        int pos = piece.getTrackPosition();

        // Home lane: 5 posisi (0-4) + position 5 = finish [7,7]
        if (pos >= 5) {
            return new int[]{7, 7};
        }

        switch (piece.getOwnerColor()) {
            case RED:
                // Y=7 konstan, X maju dari 1 ke 5
                return new int[]{1 + pos, 7};

            case GREEN:
                // X=7 konstan, Y maju dari 1 ke 5
                return new int[]{7, 1 + pos};

            case BLUE:
                // Y=7 konstan, X mundur dari 13 ke 9
                return new int[]{13 - pos, 7};

            case YELLOW:
                // X=7 konstan, Y mundur dari 13 ke 9
                return new int[]{7, 13 - pos};

            default:
                return new int[]{7, 7};
        }
    }

    // ========== UTILITY METHODS ==========

    public static double[] gridToPixelCenter(int gridX, int gridY) {
        double pixelX = gridX * CELL_SIZE + CELL_SIZE / 2.0;
        double pixelY = gridY * CELL_SIZE + CELL_SIZE / 2.0;
        return new double[]{pixelX, pixelY};
    }

    public static double[] gridToPixelTopLeft(int gridX, int gridY) {
        return new double[]{gridX * CELL_SIZE, gridY * CELL_SIZE};
    }

    public static double[] getPiecePixelPosition(GamePiece piece) {
        int[] gridPos = getPieceGridPosition(piece);
        if (gridPos == null) return null;
        return gridToPixelCenter(gridPos[0], gridPos[1]);
    }

    public static boolean isClickOnPiece(double clickX, double clickY, GamePiece piece, double clickRadius) {
        double[] piecePixel = getPiecePixelPosition(piece);
        if (piecePixel == null) return false;

        double distance = Math.sqrt(
                Math.pow(clickX - piecePixel[0], 2) +
                        Math.pow(clickY - piecePixel[1], 2)
        );

        return distance <= clickRadius;
    }

    public static Map<Integer, int[]> getTrackGridMap() {
        return new HashMap<>(TRACK_GRID);
    }
}