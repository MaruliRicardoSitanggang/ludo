package com.ludoelite.engine;

import com.ludoelite.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Main game engine that handles all Ludo game logic and rules.
 *
 * Demonstrates:
 * - Encapsulation: Game state is encapsulated and modified through methods
 * - Single Responsibility: Handles only game logic (not UI)
 * - Composition: Composed of Players, Dice, etc.
 *
 * @version 3.7.0 - POSITION-13-FIX
 * ALL POSITIONS (0-51) GUARANTEED ACCESSIBLE
 * Position 13 (start squares) explicitly verified
 */
public class GameEngine {

    public static final String VERSION = "3.7.0-POSITION-13-FIX";

    private final List<Player> players;
    private final Dice dice;
    private Player currentPlayer;
    private int currentPlayerIndex;
    private GameState gameState;
    private int consecutiveSixes;
    private boolean hasRolledDice;
    private int lastDiceValue;

    public GameEngine() {
        this.players = new ArrayList<>();
        this.dice = new Dice();
        this.currentPlayerIndex = 0;
        this.gameState = GameState.NOT_STARTED;
        this.consecutiveSixes = 0;
        this.hasRolledDice = false;
        this.lastDiceValue = 0;
    }

    // ── Game Setup ───────────────────────────────────────────────────────────

    /**
     * Adds a player to the game.
     */
    public void addPlayer(Player player) {
        if (players.size() < 4) {
            players.add(player);
        }
    }

    /**
     * Starts the game. Must have at least 2 players.
     */
    public boolean startGame() {
        if (players.size() < 2) {
            return false;
        }

        gameState = GameState.IN_PROGRESS;
        currentPlayerIndex = 0;
        currentPlayer = players.get(0);
        currentPlayer.setCurrentTurn(true);
        hasRolledDice = false;

        return true;
    }

    // ── Dice Rolling ─────────────────────────────────────────────────────────

    /**
     * Rolls the dice for the current player.
     */
    public int rollDice() {
        if (gameState != GameState.IN_PROGRESS || hasRolledDice) {
            return -1;
        }

        lastDiceValue = dice.roll();
        hasRolledDice = true;

        // Track consecutive sixes
        if (lastDiceValue == 6) {
            consecutiveSixes++;
        } else {
            consecutiveSixes = 0;
        }

        // Three consecutive sixes = skip turn
        if (consecutiveSixes >= 3) {
            consecutiveSixes = 0;
            nextTurn();
            return 0; // Indicate turn skipped
        }

        return lastDiceValue;
    }

    // ── Piece Movement ───────────────────────────────────────────────────────

    /**
     * Attempts to move a piece. Returns true if move was valid and executed.
     * Fixed: Proper position calculation to prevent skipping.
     */
    public boolean movePiece(GamePiece piece, int steps) {
        if (!hasRolledDice || piece == null) {
            return false;
        }

        // Check if piece belongs to current player
        if (piece.getOwnerColor() != currentPlayer.getColor()) {
            return false;
        }

        // Handle piece in base
        if (piece.getState() == PieceState.IN_BASE) {
            if (steps == 6) {
                piece.setState(PieceState.ON_TRACK);
                piece.setTrackPosition(0);

                // Check for capture at starting position
                checkAndCaptureAt(piece, 0);

                finishMove(steps);
                return true;
            }
            return false;
        }

        // Handle piece on track
        if (piece.getState() == PieceState.ON_TRACK) {
            int currentPos = piece.getTrackPosition();
            int newPosition = currentPos + steps;

            // ═══════════════════════════════════════════════════════════════════════
            // HOME LANE ENTRY LOGIC - FIXED v3.3.0
            // ═══════════════════════════════════════════════════════════════════════
            //
            // SISTEM KOORDINAT RELATIF:
            // - Setiap warna punya 52 posisi relatif (0-51) di main track
            // - Posisi relatif 0 = start square (berbeda untuk tiap warna)
            // - Posisi relatif 51 = kotak terakhir sebelum home entry
            // - Posisi relatif 52+ = masuk home lane
            //
            // KOTAK CORNER "DI BELAKANG START":
            // Kotak absolut 51, 12, 25, 38 adalah kotak corner yang terletak
            // "di belakang" kotak start (abs 0, 13, 26, 39). Kotak-kotak ini adalah
            // KOTAK TRACK BIASA yang HARUS bisa diinjak oleh SEMUA warna:
            //
            // - Untuk warna PEMILIK: kotak relatif 51 adalah posisi terakhir
            //   sebelum masuk home lane (relatif 52+)
            // - Untuk warna LAIN: kotak ini hanya kotak biasa di jalur mereka
            //
            // Contoh untuk MERAH (start abs 0):
            //   - Posisi relatif 51 = absolut 51 = grid [0,7] (corner kiri atas)
            //   - Bidak merah di rel 48 + dadu 5 = rel 53 → masuk home lane step 1
            //
            // Contoh untuk KUNING (start abs 39) yang lewat kotak merah:
            //   - Posisi relatif 12 = absolut (39+12)%52 = 51 = grid [0,7] (sama!)
            //   - Bidak kuning di rel 12 + dadu 1 = rel 13 → ke abs 52%52=0 (start merah)
            //   - Bidak kuning TIDAK masuk home lane karena rel 13 < 52
            //
            // PENTING: Hanya saat newPosition >= 52 bidak masuk home lane.
            // Semua posisi 0-51 adalah track normal yang bisa diinjak semua warna.
            // ═══════════════════════════════════════════════════════════════════════

            if (newPosition >= 52) {
                // Bidak sudah melewati SELURUH 52 kotak track relatif → masuk home lane
                int stepsIntoHome = newPosition - 52;

                if (stepsIntoHome > BoardTrack.HOME_LANE_SIZE) {
                    // Overshoot melewati finish - tidak boleh (butuh angka pas)
                    return false;
                }

                if (stepsIntoHome == BoardTrack.HOME_LANE_SIZE) {
                    // Tepat sampai finish (angka pas)!
                    piece.setState(PieceState.FINISHED);
                    piece.setTrackPosition(BoardTrack.HOME_LANE_SIZE);

                    if (currentPlayer.hasWon()) {
                        gameState = GameState.FINISHED;
                    }

                    finishMove(steps);
                    return true;
                }

                // Masuk home lane normal (belum finish)
                piece.setState(PieceState.IN_HOME_LANE);
                piece.setTrackPosition(stepsIntoHome);

                finishMove(steps);
                return true;
            }

            // ═══════════════════════════════════════════════════════════════════════
            // NORMAL TRACK MOVEMENT (posisi relatif 0-51)
            // ═══════════════════════════════════════════════════════════════════════
            // newPosition < 52, masih di main track.
            // Simpan posisi relatif langsung tanpa wrap — rendering nanti yang wrap.
            //
            // SEMUA posisi 0-51 bisa diinjak oleh SEMUA warna, termasuk:
            // - Posisi 45-51 (kotak corner "di belakang start" untuk setiap warna)
            // - Kotak-kotak ini TIDAK di-skip, TIDAK di-block
            // - Rendering: GridPositionCalculator.getAbsolutePosition() akan convert
            //   posisi relatif → absolut dengan wrap % 52 otomatis
            // ═══════════════════════════════════════════════════════════════════════

            // Debug log untuk verify movement (EXTENSIVE)
            int absolutePos = BoardTrack.getAbsolutePosition(piece.getOwnerColor(), newPosition);
            System.out.println("═══════════════════════════════════════════════════════");
            System.out.printf("[TRACK MOVE] %s piece #%d MOVING%n",
                    piece.getOwnerColor(), piece.getPieceNumber());
            System.out.printf("  Current rel pos: %d%n", currentPos);
            System.out.printf("  Steps: %d%n", steps);
            System.out.printf("  New rel pos: %d%n", newPosition);
            System.out.printf("  Old abs pos: %d%n",
                    BoardTrack.getAbsolutePosition(piece.getOwnerColor(), currentPos));
            System.out.printf("  New abs pos: %d%n", absolutePos);

            // Check if this is a corner square
            if (absolutePos == 12 || absolutePos == 25 || absolutePos == 38 || absolutePos == 51) {
                System.out.printf("  ⭐ CORNER SQUARE DETECTED! Abs pos %d%n", absolutePos);
            }
            System.out.println("═══════════════════════════════════════════════════════");

            piece.setTrackPosition(newPosition);

            // Check for capture di posisi papan absolut
            checkAndCaptureAt(piece, newPosition);

            finishMove(steps);
            return true;
        }

        // Handle piece in home lane
        if (piece.getState() == PieceState.IN_HOME_LANE) {
            int newPosition = piece.getTrackPosition() + steps;

            // Check if finishing
            if (newPosition >= BoardTrack.HOME_LANE_SIZE) {
                if (newPosition == BoardTrack.HOME_LANE_SIZE) {
                    // Exact count - FINISH!
                    piece.setState(PieceState.FINISHED);
                    piece.setTrackPosition(BoardTrack.HOME_LANE_SIZE);

                    // Check win condition
                    if (currentPlayer.hasWon()) {
                        gameState = GameState.FINISHED;
                    }

                    finishMove(steps);
                    return true;
                } else {
                    // Overshoot - need exact count to finish
                    return false;
                }
            }

            // Normal home lane move
            piece.setTrackPosition(newPosition);
            finishMove(steps);
            return true;
        }

        return false;
    }

    /**
     * Checks if there's an opponent piece at the position and captures it.
     *
     * ATURAN PROFESSIONAL:
     * - Capture terjadi jika landing di kotak yang sama dengan opponent
     * - TIDAK bisa capture di star safe zones (9, 22, 35, 48)
     * - TIDAK bisa capture di start square pemilik (contoh: RED di abs 0)
     * - BISA capture di start square warna lain (contoh: YELLOW di abs 0 = start RED)
     */
    private void checkAndCaptureAt(GamePiece movingPiece, int position) {
        int absolutePos = BoardTrack.getAbsolutePosition(
                movingPiece.getOwnerColor(), position
        );

        // Safe zones prevent capture
        // PENTING: isSafeZone sekarang cek apakah safe untuk moving piece color
        if (BoardTrack.isSafeZone(absolutePos, movingPiece.getOwnerColor())) {
            return;
        }

        // Check all opponent pieces
        for (Player player : players) {
            if (player.getColor() == movingPiece.getOwnerColor()) {
                continue;
            }

            for (GamePiece opponentPiece : player.getPieces()) {
                if (opponentPiece.getState() == PieceState.ON_TRACK) {
                    int opponentAbsPos = BoardTrack.getAbsolutePosition(
                            opponentPiece.getOwnerColor(),
                            opponentPiece.getTrackPosition()
                    );

                    if (opponentAbsPos == absolutePos) {
                        // Capture! Opponent kembali ke base
                        System.out.printf("[CAPTURE] %s captured %s at abs pos %d%n",
                                movingPiece.getOwnerColor(),
                                opponentPiece.getOwnerColor(),
                                absolutePos);
                        opponentPiece.sendToBase();
                    }
                }
            }
        }
    }

    /**
     * Completes the move and handles turn logic.
     */
    private void finishMove(int diceValue) {
        hasRolledDice = false;

        // If rolled 6, player gets another turn
        if (diceValue != 6) {
            nextTurn();
        }
    }

    // ── Turn Management ──────────────────────────────────────────────────────

    /**
     * Advances to the next player's turn.
     */
    private void nextTurn() {
        if (currentPlayer != null) {
            currentPlayer.setCurrentTurn(false);
        }

        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        currentPlayer = players.get(currentPlayerIndex);
        currentPlayer.setCurrentTurn(true);
        hasRolledDice = false;
        lastDiceValue = 0;
    }

    /**
     * Skips the current player's turn (e.g., no valid moves).
     */
    public void skipTurn() {
        nextTurn();
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public List<Player> getPlayers() {
        return new ArrayList<>(players); // Return copy
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Dice getDice() {
        return dice;
    }

    public int getLastDiceValue() {
        return lastDiceValue;
    }

    public boolean hasRolledDice() {
        return hasRolledDice;
    }

    /**
     * Gets moveable pieces for current player with last dice value.
     */
    public List<GamePiece> getMoveablePieces() {
        if (currentPlayer == null || !hasRolledDice) {
            return new ArrayList<>();
        }
        return currentPlayer.getMoveablePieces(lastDiceValue);
    }

    /**
     * Checks if current player has any valid moves.
     */
    public boolean hasValidMoves() {
        return !getMoveablePieces().isEmpty();
    }
}