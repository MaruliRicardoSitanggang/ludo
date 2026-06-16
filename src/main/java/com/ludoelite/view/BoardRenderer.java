package com.ludoelite.view;

import com.ludoelite.engine.GameEngine;
import com.ludoelite.model.*;
import com.ludoelite.util.GridPositionCalculator;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.Map;

/**
 * BoardRenderer - Renderer untuk papan Ludo JavaFX dengan grid 15x15.
 *
 * Struktur papan:
 * - 4 Markas 6x6 di sudut (tidak overlap dengan jalur)
 * - Jalur Track berbentuk cross dengan 52 posisi
 * - Home Lanes berwarna menuju center
 * - Center [7,7] dengan 4 segitiga finish
 *
 * @author Ludo Elite Team
 * @version 2.0 - Complete Rewrite
 */
public class BoardRenderer {

    // Constants
    private static final int BOARD_SIZE = GridPositionCalculator.BOARD_SIZE;
    private static final int CELL = GridPositionCalculator.CELL_SIZE;
    private static final int PIECE_SIZE = 28;

    // Colors - PROFESIONAL & MENARIK
    private static final Color BOARD_BG = Color.rgb(15, 23, 42);
    private static final Color TRACK_COLOR = Color.WHITE;

    // Warna pemain - VIBRANT & JELAS
    private static final Color COLOR_RED = Color.rgb(239, 68, 68);      // Merah vibrant
    private static final Color COLOR_GREEN = Color.rgb(34, 197, 94);    // Hijau terang
    private static final Color COLOR_BLUE = Color.rgb(59, 130, 246);    // Biru cerah
    private static final Color COLOR_YELLOW = Color.rgb(250, 204, 21);  // Kuning cerah

    // Warna safe zone
    private static final Color SAFE_ZONE_BG = Color.rgb(148, 163, 184); // Abu-abu terang

    private final Map<Integer, int[]> trackGrid;

    public BoardRenderer() {
        this.trackGrid = GridPositionCalculator.getTrackGridMap();
    }

    /**
     * Main render method - PAPAN LUDO KLASIK
     */
    public void render(GraphicsContext gc, GameEngine engine) {
        // Background
        gc.setFill(BOARD_BG);
        gc.fillRect(0, 0, BOARD_SIZE, BOARD_SIZE);

        // Draw layers (order matters!)
        drawTrack(gc);           // Layer 1: Track putih
        drawStartPositions(gc);  // Layer 2: Start positions (warna pemain)
        drawSafeZones(gc);       // Layer 3: Safe zones (bintang)
        drawHomeLanes(gc);       // Layer 4: Home lanes
        drawBases(gc);           // Layer 5: Bases (LAYOUT KLASIK)
        drawCenter(gc);          // Layer 6: Center finish

        // Draw pieces (topmost layer)
        for (Player player : engine.getPlayers()) {
            for (GamePiece piece : player.getPieces()) {
                drawPiece(gc, piece);
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // TRACK RENDERING - Jalur Cross (tidak overlap dengan markas)
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Menggambar jalur track berbentuk cross.
     * Hanya 4 lengan cross, TIDAK menggambar area markas.
     *
     * Lengan Cross:
     * - Kiri:  col 0-5,  row 6-8
     * - Atas:  col 6-8,  row 0-5
     * - Kanan: col 9-14, row 6-8
     * - Bawah: col 6-8,  row 9-14
     */
    private void drawTrack(GraphicsContext gc) {
        // Lengan KIRI (Left arm) - col 0-5, row 6-8
        for (int col = 0; col <= 5; col++) {
            for (int row = 6; row <= 8; row++) {
                drawTrackCell(gc, col, row);
            }
        }

        // Lengan ATAS (Top arm) - col 6-8, row 0-5
        for (int col = 6; col <= 8; col++) {
            for (int row = 0; row <= 5; row++) {
                drawTrackCell(gc, col, row);
            }
        }

        // Lengan KANAN (Right arm) - col 9-14, row 6-8
        for (int col = 9; col <= 14; col++) {
            for (int row = 6; row <= 8; row++) {
                drawTrackCell(gc, col, row);
            }
        }

        // Lengan BAWAH (Bottom arm) - col 6-8, row 9-14
        for (int col = 6; col <= 8; col++) {
            for (int row = 9; row <= 14; row++) {
                drawTrackCell(gc, col, row);
            }
        }
    }

    /**
     * Menggambar satu cell track - VISUAL PROFESIONAL.
     */
    private void drawTrackCell(GraphicsContext gc, int col, int row) {
        double[] topLeft = GridPositionCalculator.gridToPixelTopLeft(col, row);

        // Fill putih untuk track
        gc.setFill(TRACK_COLOR);
        gc.fillRect(topLeft[0], topLeft[1], CELL, CELL);

        // Border abu-abu tipis
        gc.setStroke(Color.rgb(226, 232, 240));
        gc.setLineWidth(0.5);
        gc.strokeRect(topLeft[0], topLeft[1], CELL, CELL);
    }

    /**
     * Cek apakah posisi grid adalah safe zone - LAYOUT KLASIK.
     */
    private boolean isSafeZone(int col, int row) {
        // 4 START POSITIONS (warna pemain)
        if (col == 1 && row == 6) return true;   // RED start
        if (col == 8 && row == 1) return true;   // GREEN start
        if (col == 13 && row == 8) return true;  // BLUE start
        if (col == 6 && row == 13) return true;  // YELLOW start

        // 4 SAFE ZONES (bintang abu-abu)
        if (col == 2 && row == 8) return true;   // Safe zone 1 (kiri-bawah)
        if (col == 6 && row == 2) return true;   // Safe zone 2 (atas-tengah)
        if (col == 12 && row == 6) return true;  // Safe zone 3 (kanan-tengah)
        if (col == 8 && row == 12) return true;  // Safe zone 4 (bawah-tengah)

        return false;
    }

    private boolean isStartPosition(int col, int row) {
        return (col == 1 && row == 6) ||   // RED
                (col == 8 && row == 1) ||   // GREEN
                (col == 13 && row == 8) ||  // BLUE
                (col == 6 && row == 13);    // YELLOW
    }

    private Color getStartColor(int col, int row) {
        if (col == 1 && row == 6) return COLOR_RED;
        if (col == 8 && row == 1) return COLOR_GREEN;
        if (col == 13 && row == 8) return COLOR_BLUE;
        if (col == 6 && row == 13) return COLOR_YELLOW;
        return Color.GRAY;
    }

    /**
     * Render START POSITIONS - VISUAL PROFESIONAL dengan icon arrow & shadow.
     */
    private void drawStartPositions(GraphicsContext gc) {
        drawColoredStart(gc, 1, 6, COLOR_RED, "→");      // MERAH START (arrow kanan)
        drawColoredStart(gc, 8, 1, COLOR_GREEN, "↓");    // HIJAU START (arrow bawah)
        drawColoredStart(gc, 13, 8, COLOR_BLUE, "←");    // BIRU START (arrow kiri)
        drawColoredStart(gc, 6, 13, COLOR_YELLOW, "↑");  // KUNING START (arrow atas)
    }

    private void drawColoredStart(GraphicsContext gc, int col, int row, Color color, String arrow) {
        double[] topLeft = GridPositionCalculator.gridToPixelTopLeft(col, row);
        double[] center = GridPositionCalculator.gridToPixelCenter(col, row);

        // Shadow layer (depth effect)
        gc.setFill(Color.rgb(0, 0, 0, 0.15));
        gc.fillRect(topLeft[0] + 2, topLeft[1] + 2, CELL, CELL);

        // Gradient background (inner to outer)
        gc.setFill(color.deriveColor(0, 0.9, 1.15, 0.95));
        gc.fillRect(topLeft[0], topLeft[1], CELL, CELL);

        // Outer gradient layer
        gc.setFill(color.deriveColor(0, 1.1, 0.95, 0.85));
        gc.fillRect(topLeft[0] + 3, topLeft[1] + 3, CELL - 6, CELL - 6);

        // Border warna pemain yang lebih gelap (triple layer)
        gc.setStroke(color.darker().darker());
        gc.setLineWidth(3.5);
        gc.strokeRect(topLeft[0], topLeft[1], CELL, CELL);

        gc.setStroke(color);
        gc.setLineWidth(2);
        gc.strokeRect(topLeft[0] + 2, topLeft[1] + 2, CELL - 4, CELL - 4);

        // Arrow shadow (untuk depth)
        gc.setFill(Color.rgb(0, 0, 0, 0.3));
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(arrow, center[0] + 1, center[1] + 9);

        // Arrow di tengah (putih terang)
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        gc.fillText(arrow, center[0], center[1] + 8);
    }

    /**
     * Render SAFE ZONES - VISUAL PROFESIONAL dengan bintang emas 3D.
     */
    private void drawSafeZones(GraphicsContext gc) {
        drawSafeZone(gc, 2, 8);   // Safe kiri-bawah
        drawSafeZone(gc, 6, 2);   // Safe atas-tengah
        drawSafeZone(gc, 12, 6);  // Safe kanan-tengah
        drawSafeZone(gc, 8, 12);  // Safe bawah-tengah
    }

    private void drawSafeZone(GraphicsContext gc, int col, int row) {
        double[] topLeft = GridPositionCalculator.gridToPixelTopLeft(col, row);
        double[] center = GridPositionCalculator.gridToPixelCenter(col, row);

        // Shadow layer
        gc.setFill(Color.rgb(0, 0, 0, 0.08));
        gc.fillRect(topLeft[0] + 2, topLeft[1] + 2, CELL, CELL);

        // Fill abu-abu terang dengan gradient
        gc.setFill(SAFE_ZONE_BG.brighter());
        gc.fillRect(topLeft[0], topLeft[1], CELL, CELL);

        // Inner darker layer untuk depth
        gc.setFill(SAFE_ZONE_BG);
        gc.fillRect(topLeft[0] + 4, topLeft[1] + 4, CELL - 8, CELL - 8);

        // Border abu-abu gelap (double border)
        gc.setStroke(Color.rgb(100, 116, 139));
        gc.setLineWidth(2);
        gc.strokeRect(topLeft[0], topLeft[1], CELL, CELL);

        gc.setStroke(Color.rgb(148, 163, 184));
        gc.setLineWidth(1);
        gc.strokeRect(topLeft[0] + 2, topLeft[1] + 2, CELL - 4, CELL - 4);

        // Bintang shadow (3D effect)
        gc.setFill(Color.rgb(180, 120, 0, 0.4));
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("★", center[0] + 1.5, center[1] + 10.5);

        // Bintang emas (golden star)
        gc.setFill(Color.rgb(234, 179, 8)); // Emas gelap
        gc.fillText("★", center[0], center[1] + 9);

        // Highlight star (shine effect)
        gc.setFill(Color.rgb(253, 224, 71, 0.6)); // Kuning terang
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        gc.fillText("★", center[0] - 0.5, center[1] + 8.5);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // HOME LANES - Jalur warna menuju center
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Menggambar home lanes - VISUAL GRADIENT PROFESIONAL dengan efek pencahayaan.
     */
    private void drawHomeLanes(GraphicsContext gc) {
        // MERAH home lane: Y=7, X dari 1→5
        for (int x = 1; x <= 5; x++) {
            drawHomeLaneCell(gc, x, 7, COLOR_RED, 5 - (x - 1));
        }

        // HIJAU home lane: X=7, Y dari 1→5
        for (int y = 1; y <= 5; y++) {
            drawHomeLaneCell(gc, 7, y, COLOR_GREEN, 5 - (y - 1));
        }

        // BIRU home lane: Y=7, X dari 13→9
        for (int x = 13; x >= 9; x--) {
            drawHomeLaneCell(gc, x, 7, COLOR_BLUE, x - 8);
        }

        // KUNING home lane: X=7, Y dari 13→9
        for (int y = 13; y >= 9; y--) {
            drawHomeLaneCell(gc, 7, y, COLOR_YELLOW, y - 8);
        }
    }

    /**
     * Menggambar satu cell home lane dengan gradient intensity dan glow effect.
     */
    private void drawHomeLaneCell(GraphicsContext gc, int col, int row, Color color, int intensity) {
        double[] topLeft = GridPositionCalculator.gridToPixelTopLeft(col, row);

        // Opacity berdasarkan jarak dari center (gradient effect) - lebih intens
        double baseOpacity = 0.35 + (intensity * 0.13);

        // Background glow (outer layer)
        gc.setFill(color.deriveColor(0, 1.1, 1.2, baseOpacity * 0.5));
        gc.fillRect(topLeft[0] - 1, topLeft[1] - 1, CELL + 2, CELL + 2);

        // Fill dengan warna pemain (main layer)
        gc.setFill(color.deriveColor(0, 1, 1.05, baseOpacity));
        gc.fillRect(topLeft[0], topLeft[1], CELL, CELL);

        // Inner highlight (center glow)
        gc.setFill(color.deriveColor(0, 0.9, 1.3, baseOpacity * 0.6));
        gc.fillRect(topLeft[0] + 5, topLeft[1] + 5, CELL - 10, CELL - 10);

        // Border warna pemain (triple border untuk depth)
        gc.setStroke(color.deriveColor(0, 1.3, 0.85, 1));
        gc.setLineWidth(2.5);
        gc.strokeRect(topLeft[0], topLeft[1], CELL, CELL);

        gc.setStroke(color.deriveColor(0, 1.1, 1.1, 0.9));
        gc.setLineWidth(1.5);
        gc.strokeRect(topLeft[0] + 2, topLeft[1] + 2, CELL - 4, CELL - 4);

        gc.setStroke(color.brighter());
        gc.setLineWidth(0.8);
        gc.strokeRect(topLeft[0] + 4, topLeft[1] + 4, CELL - 8, CELL - 8);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // BASES - Markas 6x6 di sudut
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Menggambar 4 markas - LAYOUT KLASIK!
     * MERAH: Kiri-Atas, HIJAU: Kanan-Atas, BIRU: Kanan-Bawah, KUNING: Kiri-Bawah
     */
    private void drawBases(GraphicsContext gc) {
        drawBase(gc, 0, 0, COLOR_RED);      // MERAH: Kiri-Atas
        drawBase(gc, 9, 0, COLOR_GREEN);    // HIJAU: Kanan-Atas
        drawBase(gc, 9, 9, COLOR_BLUE);     // BIRU: Kanan-Bawah
        drawBase(gc, 0, 9, COLOR_YELLOW);   // KUNING: Kiri-Bawah
    }

    /**
     * Menggambar satu markas 6x6 - VISUAL SUPER PROFESIONAL dengan depth & shine.
     */
    private void drawBase(GraphicsContext gc, int gridX, int gridY, Color color) {
        double[] topLeft = GridPositionCalculator.gridToPixelTopLeft(gridX, gridY);
        double baseSize = 6 * CELL;

        // Outer shadow untuk depth (3 layers)
        gc.setFill(Color.rgb(0, 0, 0, 0.25));
        gc.fillRoundRect(topLeft[0] + 4, topLeft[1] + 4, baseSize, baseSize, 15, 15);

        gc.setFill(Color.rgb(0, 0, 0, 0.15));
        gc.fillRoundRect(topLeft[0] + 2, topLeft[1] + 2, baseSize, baseSize, 15, 15);

        // Background markas (warna lebih gelap untuk depth)
        gc.setFill(color.deriveColor(0, 1.4, 0.25, 1));
        gc.fillRoundRect(topLeft[0], topLeft[1], baseSize, baseSize, 15, 15);

        // Mid-tone layer (gradient simulation)
        gc.setFill(color.deriveColor(0, 1.2, 0.4, 0.9));
        gc.fillRoundRect(topLeft[0] + 8, topLeft[1] + 8, baseSize - 16, baseSize - 16, 12, 12);

        // Border markas luar (sangat tebal dan vibrant)
        gc.setStroke(color.darker().darker());
        gc.setLineWidth(5);
        gc.strokeRoundRect(topLeft[0], topLeft[1], baseSize, baseSize, 15, 15);

        gc.setStroke(color);
        gc.setLineWidth(3.5);
        gc.strokeRoundRect(topLeft[0] + 2, topLeft[1] + 2, baseSize - 4, baseSize - 4, 14, 14);

        // Inner border (highlight & shine effect)
        gc.setStroke(color.deriveColor(0, 0.9, 1.3, 0.6));
        gc.setLineWidth(2.5);
        gc.strokeRoundRect(topLeft[0] + 5, topLeft[1] + 5, baseSize - 10, baseSize - 10, 13, 13);

        gc.setStroke(color.deriveColor(0, 1, 1.4, 0.4));
        gc.setLineWidth(1.5);
        gc.strokeRoundRect(topLeft[0] + 8, topLeft[1] + 8, baseSize - 16, baseSize - 16, 12, 12);

        // 4 slots untuk pieces
        int[][] slotOffsets = {{1,1}, {4,1}, {1,4}, {4,4}};
        double slotRadius = CELL * 0.7;

        for (int[] offset : slotOffsets) {
            double[] center = GridPositionCalculator.gridToPixelCenter(
                    gridX + offset[0],
                    gridY + offset[1]
            );

            // Multi-layer shadow untuk slot (dramatic depth)
            gc.setFill(Color.rgb(0, 0, 0, 0.35));
            gc.fillOval(
                    center[0] - slotRadius - 2,
                    center[1] - slotRadius - 2,
                    slotRadius * 2 + 4,
                    slotRadius * 2 + 4
            );

            gc.setFill(Color.rgb(0, 0, 0, 0.25));
            gc.fillOval(
                    center[0] - slotRadius - 1,
                    center[1] - slotRadius - 1,
                    slotRadius * 2 + 2,
                    slotRadius * 2 + 2
            );

            // Slot circle (very dark background)
            gc.setFill(Color.rgb(10, 15, 30));
            gc.fillOval(
                    center[0] - slotRadius,
                    center[1] - slotRadius,
                    slotRadius * 2,
                    slotRadius * 2
            );

            // Inner gradient (subtle shine)
            gc.setFill(Color.rgb(20, 30, 50, 0.5));
            gc.fillOval(
                    center[0] - slotRadius * 0.6,
                    center[1] - slotRadius * 0.6,
                    slotRadius * 1.2,
                    slotRadius * 1.2
            );

            // Slot border (triple border - professional!)
            gc.setStroke(color.darker().darker());
            gc.setLineWidth(3);
            gc.strokeOval(
                    center[0] - slotRadius,
                    center[1] - slotRadius,
                    slotRadius * 2,
                    slotRadius * 2
            );

            gc.setStroke(color);
            gc.setLineWidth(2);
            gc.strokeOval(
                    center[0] - slotRadius + 1.5,
                    center[1] - slotRadius + 1.5,
                    slotRadius * 2 - 3,
                    slotRadius * 2 - 3
            );

            gc.setStroke(color.brighter());
            gc.setLineWidth(1);
            gc.strokeOval(
                    center[0] - slotRadius + 3,
                    center[1] - slotRadius + 3,
                    slotRadius * 2 - 6,
                    slotRadius * 2 - 6
            );
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // CENTER - Area finish [7,7] dengan 4 segitiga
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Menggambar center finish [7,7] - VISUAL SUPER PROFESIONAL dengan depth & shine.
     */
    private void drawCenter(GraphicsContext gc) {
        double[] topLeft = GridPositionCalculator.gridToPixelTopLeft(7, 7);
        double[] center = GridPositionCalculator.gridToPixelCenter(7, 7);
        double cx = topLeft[0];
        double cy = topLeft[1];
        double s = CELL;
        double midX = center[0];
        double midY = center[1];

        // Outer shadow (dramatic depth)
        gc.setFill(Color.rgb(0, 0, 0, 0.3));
        gc.fillRect(cx + 3, cy + 3, s, s);

        gc.setFill(Color.rgb(0, 0, 0, 0.2));
        gc.fillRect(cx + 2, cy + 2, s, s);

        // Background putih dengan shine
        gc.setFill(Color.WHITE);
        gc.fillRect(cx, cy, s, s);

        // Inner white glow
        gc.setFill(Color.rgb(255, 255, 255, 0.8));
        gc.fillRect(cx + 4, cy + 4, s - 8, s - 8);

        // Border hitam sangat tebal (outer)
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(4);
        gc.strokeRect(cx, cy, s, s);

        // Border abu gelap (mid)
        gc.setStroke(Color.rgb(60, 60, 60));
        gc.setLineWidth(2.5);
        gc.strokeRect(cx + 1.5, cy + 1.5, s - 3, s - 3);

        // 4 SEGITIGA WARNA - LAYOUT KLASIK dengan shadow & depth

        // Shadow layer untuk semua segitiga
        gc.setFill(Color.rgb(0, 0, 0, 0.2));
        gc.fillPolygon(
                new double[]{cx + 1, cx + 1, midX + 1},
                new double[]{cy + 1, cy + s + 1, midY + 1},
                3
        );
        gc.fillPolygon(
                new double[]{cx + 1, cx + s + 1, midX + 1},
                new double[]{cy + 1, cy + 1, midY + 1},
                3
        );
        gc.fillPolygon(
                new double[]{cx + s + 1, cx + s + 1, midX + 1},
                new double[]{cy + 1, cy + s + 1, midY + 1},
                3
        );
        gc.fillPolygon(
                new double[]{cx + 1, cx + s + 1, midX + 1},
                new double[]{cy + s + 1, cy + s + 1, midY + 1},
                3
        );

        // MERAH triangle (kiri) - dengan inner glow
        gc.setFill(COLOR_RED);
        gc.fillPolygon(
                new double[]{cx, cx, midX},
                new double[]{cy, cy + s, midY},
                3
        );
        gc.setFill(COLOR_RED.brighter());
        gc.fillPolygon(
                new double[]{cx + 4, cx + 4, midX - 2},
                new double[]{cy + 4, cy + s - 4, midY},
                3
        );

        // HIJAU triangle (atas) - dengan inner glow
        gc.setFill(COLOR_GREEN);
        gc.fillPolygon(
                new double[]{cx, cx + s, midX},
                new double[]{cy, cy, midY},
                3
        );
        gc.setFill(COLOR_GREEN.brighter());
        gc.fillPolygon(
                new double[]{cx + 4, cx + s - 4, midX},
                new double[]{cy + 4, cy + 4, midY - 2},
                3
        );

        // BIRU triangle (kanan) - dengan inner glow
        gc.setFill(COLOR_BLUE);
        gc.fillPolygon(
                new double[]{cx + s, cx + s, midX},
                new double[]{cy, cy + s, midY},
                3
        );
        gc.setFill(COLOR_BLUE.brighter());
        gc.fillPolygon(
                new double[]{cx + s - 4, cx + s - 4, midX + 2},
                new double[]{cy + 4, cy + s - 4, midY},
                3
        );

        // KUNING triangle (bawah) - dengan inner glow
        gc.setFill(COLOR_YELLOW);
        gc.fillPolygon(
                new double[]{cx, cx + s, midX},
                new double[]{cy + s, cy + s, midY},
                3
        );
        gc.setFill(COLOR_YELLOW.brighter());
        gc.fillPolygon(
                new double[]{cx + 4, cx + s - 4, midX},
                new double[]{cy + s - 4, cy + s - 4, midY + 2},
                3
        );

        // Border pemisah antar segitiga (sangat tebal & dramatic)
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2.5);
        gc.strokeLine(cx, cy, midX, midY);
        gc.strokeLine(cx + s, cy, midX, midY);
        gc.strokeLine(cx, cy + s, midX, midY);
        gc.strokeLine(cx + s, cy + s, midX, midY);

        // Inner border untuk segitiga (depth lines)
        gc.setStroke(Color.rgb(40, 40, 40));
        gc.setLineWidth(1.5);
        gc.strokeLine(cx + 3, cy + 3, midX, midY);
        gc.strokeLine(cx + s - 3, cy + 3, midX, midY);
        gc.strokeLine(cx + 3, cy + s - 3, midX, midY);
        gc.strokeLine(cx + s - 3, cy + s - 3, midX, midY);

        // Center circle putih dengan multi-layer shadow & shine
        double dotRadius = 8;

        // Outer shadow (strong)
        gc.setFill(Color.rgb(0, 0, 0, 0.5));
        gc.fillOval(midX - dotRadius + 2, midY - dotRadius + 2, dotRadius * 2, dotRadius * 2);

        // Mid shadow
        gc.setFill(Color.rgb(0, 0, 0, 0.35));
        gc.fillOval(midX - dotRadius + 1, midY - dotRadius + 1, dotRadius * 2, dotRadius * 2);

        // White circle (main)
        gc.setFill(Color.WHITE);
        gc.fillOval(midX - dotRadius, midY - dotRadius, dotRadius * 2, dotRadius * 2);

        // Inner shine (highlight)
        gc.setFill(Color.rgb(255, 255, 255, 0.6));
        gc.fillOval(midX - dotRadius * 0.5, midY - dotRadius * 0.5, dotRadius, dotRadius);

        // Circle border (triple border)
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeOval(midX - dotRadius, midY - dotRadius, dotRadius * 2, dotRadius * 2);

        gc.setStroke(Color.rgb(80, 80, 80));
        gc.setLineWidth(1.2);
        gc.strokeOval(midX - dotRadius + 1, midY - dotRadius + 1, dotRadius * 2 - 2, dotRadius * 2 - 2);

        gc.setStroke(Color.rgb(180, 180, 180));
        gc.setLineWidth(0.8);
        gc.strokeOval(midX - dotRadius + 2, midY - dotRadius + 2, dotRadius * 2 - 4, dotRadius * 2 - 4);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // PIECE RENDERING
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Menggambar piece menggunakan GridPositionCalculator.
     * Piece di-render tepat di tengah cell.
     */
    private void drawPiece(GraphicsContext gc, GamePiece piece) {
        double[] pixelPos = GridPositionCalculator.getPiecePixelPosition(piece);
        if (pixelPos != null) {
            // Koordinat top-left untuk rendering (piece size diperhitungkan)
            double x = pixelPos[0] - PIECE_SIZE / 2.0;
            double y = pixelPos[1] - PIECE_SIZE / 2.0;

            // Delegate rendering ke piece (polymorphism)
            piece.render(gc, x, y, PIECE_SIZE);
        }
    }

    public int getBoardSize() {
        return BOARD_SIZE;
    }
}