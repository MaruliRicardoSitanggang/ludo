package com.ludoelite.controller;

import com.ludoelite.engine.GameEngine;
import com.ludoelite.model.*;
import com.ludoelite.util.AlertHelper;
import com.ludoelite.util.GridPositionCalculator;
import com.ludoelite.util.SessionManager;
import com.ludoelite.util.ViewNavigator;
import com.ludoelite.view.BoardRenderer;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for the Ludo Board game screen.
 *
 * Demonstrates:
 * - MVC Pattern: Controller manages interaction between Model (GameEngine) and View (FXML/Canvas)
 * - Encapsulation: Uses GameEngine API without knowing internal implementation
 * - Polymorphism: Works with GamePiece objects polymorphically
 */
public class LudoBoardController extends BaseController {

    @FXML private Canvas gameCanvas;
    @FXML private Button rollDiceButton;
    @FXML private Label diceResultLabel;
    @FXML private Label currentPlayerLabel;
    @FXML private Label gameStatusLabel;
    @FXML private HBox player1Info;
    @FXML private HBox player2Info;
    @FXML private HBox player3Info;
    @FXML private HBox player4Info;
    @FXML private Label player1Name;
    @FXML private Label player2Name;
    @FXML private Label player3Name;
    @FXML private Label player4Name;
    @FXML private Button backButton;
    @FXML private Button autoPlayButton;

    private GameEngine gameEngine;
    private BoardRenderer boardRenderer;
    private GraphicsContext gc;
    private GamePiece selectedPiece;

    // WebSocket integration (disabled for local mode)
    // private WebSocketService webSocketService;
    private String currentGameId;
    private boolean isMultiplayerMode = false;
    private String subscriptionId;

    // ══════════════════════════════════════════════════════════════════════════
    // Lifecycle
    // ══════════════════════════════════════════════════════════════════════════

    @FXML
    public void initialize() {
        gc = gameCanvas.getGraphicsContext2D();
        boardRenderer = new BoardRenderer();
        gameEngine = new GameEngine();
        // WebSocketService disabled for local mode
        // webSocketService = WebSocketService.getInstance();

        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println(" 🎮 Ludo Elite v3.7.0 - POSITION-13-FIX");
        System.out.println(" ✅ ALL positions (0-51) ACCESSIBLE");
        System.out.println(" ✅ Position 13 (start squares) FIXED");
        System.out.println(" 📊 Version: " + GameEngine.VERSION);
        System.out.println("═══════════════════════════════════════════════════════");

        setupGame();
        setupCanvas();
        updateUI();

        // Initial render
        boardRenderer.render(gc, gameEngine);

        // Connect to WebSocket if in multiplayer mode
        if (isMultiplayerMode && currentGameId != null) {
            connectToGame();
        }
    }

    /**
     * Sets up the game with players.
     */
    private void setupGame() {
        // For demo, create 4 AI/test players
        // In production, this would be populated from game session data
        Player player1 = new Player(PlayerColor.RED, "Player 1");
        Player player2 = new Player(PlayerColor.GREEN, "Player 2");
        Player player3 = new Player(PlayerColor.YELLOW, "Player 3");
        Player player4 = new Player(PlayerColor.BLUE, "Player 4");

        gameEngine.addPlayer(player1);
        gameEngine.addPlayer(player2);
        gameEngine.addPlayer(player3);
        gameEngine.addPlayer(player4);

        // Update player info labels
        player1Name.setText(player1.getPlayerName());
        player2Name.setText(player2.getPlayerName());
        player3Name.setText(player3.getPlayerName());
        player4Name.setText(player4.getPlayerName());

        // Start the game
        if (!gameEngine.startGame()) {
            AlertHelper.showError("Game Error", "Failed to start game",
                    "Need at least 2 players to start.");
        }
    }

    /**
     * Sets game context for multiplayer mode.
     * Call this before showing the view.
     *
     * @param gameId The game ID from backend
     */
    public void setGameContext(String gameId) {
        this.currentGameId = gameId;
        this.isMultiplayerMode = true;
    }

    /**
     * Sets up canvas click handling for piece selection.
     */
    private void setupCanvas() {
        gameCanvas.setOnMouseClicked(event -> {
            handleCanvasClick(event.getX(), event.getY());
        });
    }

    // ══════════════════════════════════════════════════════════════════════════
    // WebSocket Connection
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Connects to WebSocket and subscribes to game updates.
     * DISABLED FOR LOCAL MODE - Online multiplayer not available.
     */
    private void connectToGame() {
        // WebSocket disabled for local mode
        gameStatusLabel.setText("Local Mode - Hot-seat Multiplayer");

        // Original code commented for local mode
        /*
        gameStatusLabel.setText("Connecting to server...");

        webSocketService.connect(
            // On connected
            (Void) -> {
                Platform.runLater(() -> {
                    gameStatusLabel.setText("Connected! Waiting for game to start...");
                    subscribeToGameUpdates();
                });
            },
            // On error
            (error) -> {
                Platform.runLater(() -> {
                    gameStatusLabel.setText("Connection failed: " + error);
                    AlertHelper.showError("Connection Error",
                                         "Failed to connect to game server",
                                         error);
                });
            }
        );
        */
    }

    /**
     * Subscribes to game state updates from server.
     * DISABLED FOR LOCAL MODE.
     */
    private void subscribeToGameUpdates() {
        // WebSocket disabled for local mode

        // Original code commented
        /*
        String topic = "/topic/game/" + currentGameId;

        subscriptionId = webSocketService.subscribe(
            topic,
            GameStateUpdate.class,
            this::handleGameStateUpdate
        );

        System.out.println("✅ Subscribed to game updates: " + topic);
        */
    }

    /**
     * Handles incoming game state updates from server.
     * DISABLED FOR LOCAL MODE.
     */
    private void handleGameStateUpdate(Object update) {
        // WebSocket disabled for local mode

        // Original code commented
        /*
        Platform.runLater(() -> {
            System.out.println("📥 Received game state update: " + update);

            // Update game state from server
            if (update.getDiceValue() > 0) {
                diceResultLabel.setText("🎲 " + update.getDiceValue());
            }

            if (update.getCurrentPlayerColor() != null) {
                currentPlayerLabel.setText("Current Turn: " + update.getCurrentPlayerColor());
            }

            if (update.getMessage() != null) {
                gameStatusLabel.setText(update.getMessage());
            }

            // If there's a move, apply it
            if (update.getMovedPiece() != null) {
                applyMoveFromServer(update.getMovedPiece());
            }

            // Re-render board
            boardRenderer.render(gc, gameEngine);
            updateUI();
        });
        */
    }

    /**
     * Applies a move received from the server to local game state.
     * DISABLED FOR LOCAL MODE.
     */
    private void applyMoveFromServer(Object moveData) {
        // WebSocket disabled for local mode

        // Original code commented
        /*
        // Find the player and piece
        for (Player player : gameEngine.getPlayers()) {
            if (player.getColor().name().equals(moveData.getPlayerColor())) {
                GamePiece piece = player.getPiece(moveData.getPieceNumber());
                if (piece != null) {
                    // Apply the move (server already validated it)
                    gameEngine.movePiece(piece, moveData.getDiceValue());
                }
                break;
            }
        }
        */
    }

    /**
     * Disconnects from WebSocket.
     * DISABLED FOR LOCAL MODE.
     */
    private void disconnectFromGame() {
        // WebSocket disabled for local mode

        // Original code commented
        /*
        if (subscriptionId != null && webSocketService != null) {
            webSocketService.unsubscribe(subscriptionId);
            subscriptionId = null;
        }
        */
    }

    // ══════════════════════════════════════════════════════════════════════════
    // Game Actions
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Handles dice roll button click.
     */
    @FXML
    private void handleRollDice() {
        if (gameEngine.getGameState() != GameState.IN_PROGRESS) {
            AlertHelper.showWarning("Game Not Active", "Peringatan", "The game is not currently in progress.");
            return;
        }

        // Roll the dice
        int result = gameEngine.rollDice();

        if (result == -1) {
            AlertHelper.showWarning("Invalid Roll", "Peringatan", "You have already rolled the dice this turn.");
            return;
        }

        // Send dice roll to server if in multiplayer mode
        if (isMultiplayerMode) {
            sendDiceRollToServer(result);
        }

        if (result == 0) {
            // Three consecutive sixes - turn skipped
            diceResultLabel.setText("❌ Turn Skipped!");
            gameStatusLabel.setText("Three 6s in a row! Turn skipped.");
            animateDiceRoll(0);

            // Wait then update for next player
            pauseThenExecute(1500, () -> {
                updateUI();
                boardRenderer.render(gc, gameEngine);
            });
            return;
        }

        // Show result with animation
        animateDiceRoll(result);
        diceResultLabel.setText("🎲 " + result);

        // Check if player has valid moves
        if (!gameEngine.hasValidMoves()) {
            gameStatusLabel.setText("No valid moves available!");
            rollDiceButton.setDisable(true);

            // Auto skip turn after delay
            pauseThenExecute(2000, () -> {
                gameEngine.skipTurn();
                updateUI();
                boardRenderer.render(gc, gameEngine);
            });
        } else {
            gameStatusLabel.setText("Select a piece to move");
            rollDiceButton.setDisable(true);
        }

        updateUI();
    }

    /**
     * Handles canvas click for piece selection and movement.
     */
    private void handleCanvasClick(double x, double y) {
        if (!gameEngine.hasRolledDice()) {
            gameStatusLabel.setText("Roll the dice first!");
            return;
        }

        if (!gameEngine.hasValidMoves()) {
            return;
        }

        // Find clicked piece
        Player currentPlayer = gameEngine.getCurrentPlayer();
        GamePiece clickedPiece = findPieceAt(x, y, currentPlayer);

        if (clickedPiece != null) {
            // Attempt to move the piece
            boolean moved = gameEngine.movePiece(clickedPiece, gameEngine.getLastDiceValue());

            if (moved) {
                gameStatusLabel.setText("Piece moved!");

                // Send move to server if in multiplayer mode
                if (isMultiplayerMode) {
                    sendMoveToServer(clickedPiece, gameEngine.getLastDiceValue());
                }

                // Re-render board
                boardRenderer.render(gc, gameEngine);

                // Check game state
                if (gameEngine.getGameState() == GameState.FINISHED) {
                    handleGameFinished();
                } else {
                    updateUI();
                }
            } else {
                gameStatusLabel.setText("Invalid move! Select another piece.");
            }
        }
    }

    /**
     * Finds a piece at the given canvas coordinates using GridPositionCalculator.
     */
    private GamePiece findPieceAt(double x, double y, Player player) {
        final double CLICK_RADIUS = 20.0;

        for (GamePiece piece : player.getPieces()) {
            if (GridPositionCalculator.isClickOnPiece(x, y, piece, CLICK_RADIUS)) {
                return piece;
            }
        }

        return null;
    }

    /**
     * Handles game finished state.
     */
    private void handleGameFinished() {
        Player winner = gameEngine.getCurrentPlayer();

        AlertHelper.showInfo("Game Finished!",
                winner.getPlayerName() + " (" + winner.getColor().getDisplayName() + ") wins!",
                "Congratulations!");

        rollDiceButton.setDisable(true);
        gameStatusLabel.setText("🏆 " + winner.getPlayerName() + " WINS!");
    }

    // ══════════════════════════════════════════════════════════════════════════
    // UI Updates
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Updates all UI elements based on game state.
     */
    private void updateUI() {
        Player current = gameEngine.getCurrentPlayer();

        if (current != null) {
            currentPlayerLabel.setText("Current Turn: " + current.getPlayerName() +
                    " (" + current.getColor().getDisplayName() + ")");
            currentPlayerLabel.setStyle("-fx-text-fill: " + current.getColor().getHexColor() + ";");
        }

        // Enable/disable roll button
        rollDiceButton.setDisable(gameEngine.hasRolledDice() ||
                gameEngine.getGameState() != GameState.IN_PROGRESS);

        // Highlight current player info
        highlightCurrentPlayerInfo();

        // Update dice display
        if (!gameEngine.hasRolledDice()) {
            diceResultLabel.setText("🎲 ?");
            gameStatusLabel.setText("Roll the dice!");
        }
    }

    /**
     * Highlights the current player's info panel.
     */
    private void highlightCurrentPlayerInfo() {
        // Reset all
        player1Info.setStyle("");
        player2Info.setStyle("");
        player3Info.setStyle("");
        player4Info.setStyle("");

        Player current = gameEngine.getCurrentPlayer();
        if (current == null) return;

        String highlightStyle = "-fx-border-color: " + current.getColor().getHexColor() +
                "; -fx-border-width: 3; -fx-border-radius: 8; " +
                "-fx-background-color: rgba(245,158,11,0.1); -fx-background-radius: 8;";

        switch (current.getColor()) {
            case RED:    player1Info.setStyle(highlightStyle); break;
            case GREEN:  player2Info.setStyle(highlightStyle); break;
            case YELLOW: player3Info.setStyle(highlightStyle); break;
            case BLUE:   player4Info.setStyle(highlightStyle); break;
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // Animations
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Animates dice roll with a counting effect.
     */
    private void animateDiceRoll(int finalValue) {
        Timeline timeline = new Timeline();

        for (int i = 0; i < 10; i++) {
            int randomValue = (int)(Math.random() * 6) + 1;
            KeyFrame kf = new KeyFrame(
                    Duration.millis(i * 100),
                    e -> diceResultLabel.setText("🎲 " + randomValue)
            );
            timeline.getKeyFrames().add(kf);
        }

        // Final value
        KeyFrame finalKf = new KeyFrame(
                Duration.millis(1000),
                e -> diceResultLabel.setText("🎲 " + (finalValue == 0 ? "❌" : finalValue))
        );
        timeline.getKeyFrames().add(finalKf);

        timeline.play();
    }

    /**
     * Helper to execute code after a delay.
     */
    private void pauseThenExecute(long millis, Runnable action) {
        new Thread(() -> {
            try {
                Thread.sleep(millis);
                Platform.runLater(action);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // ══════════════════════════════════════════════════════════════════════════
    // Navigation
    // ══════════════════════════════════════════════════════════════════════════

    @FXML
    private void handleBack() {
        boolean confirm = AlertHelper.showConfirmation(
                "Leave Game",
                "Are you sure you want to leave?",
                "Current game progress will be lost."
        );

        if (confirm) {
            // Disconnect from WebSocket if connected
            if (isMultiplayerMode) {
                disconnectFromGame();
            }
            ViewNavigator.navigateToDashboard();
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // WebSocket Integration Hooks (for future implementation)
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Hook for when an opponent's move is received via WebSocket.
     *
     * Integration Engineers: Call this method when receiving move data from server.
     *
     * @param playerColor The color of the player who moved
     * @param pieceNumber The piece number (0-3)
     * @param diceValue   The dice value used
     */
    public void onOpponentMoveReceived(PlayerColor playerColor, int pieceNumber, int diceValue) {
        Platform.runLater(() -> {
            // Find the player and piece
            for (Player player : gameEngine.getPlayers()) {
                if (player.getColor() == playerColor) {
                    GamePiece piece = player.getPiece(pieceNumber);
                    if (piece != null) {
                        // Apply the move
                        gameEngine.movePiece(piece, diceValue);

                        // Update UI
                        boardRenderer.render(gc, gameEngine);
                        updateUI();

                        gameStatusLabel.setText(
                                player.getPlayerName() + " moved a piece"
                        );
                    }
                    break;
                }
            }
        });
    }

    /**
     * Sends local player's move to server via WebSocket.
     * DISABLED FOR LOCAL MODE.
     *
     * @param piece     The piece that was moved
     * @param diceValue The dice value used for the move
     */
    private void sendMoveToServer(GamePiece piece, int diceValue) {
        // WebSocket disabled for local mode

        // Original code commented
        /*
        if (!webSocketService.isConnected()) {
            System.err.println("Cannot send move - WebSocket not connected");
            return;
        }

        // Create move payload
        Map<String, Object> movePayload = new HashMap<>();
        movePayload.put("gameId", currentGameId);
        movePayload.put("action", "MOVE");
        movePayload.put("playerColor", piece.getOwnerColor().name());
        movePayload.put("pieceNumber", piece.getPieceNumber());
        movePayload.put("diceValue", diceValue);
        movePayload.put("timestamp", System.currentTimeMillis());

        // Send to server
        String destination = "/app/game/" + currentGameId + "/move";
        webSocketService.send(destination, movePayload);

        System.out.println("📤 Sent move to server: " + piece.getOwnerColor() +
                         " piece " + piece.getPieceNumber());
        */
    }

    /**
     * Sends dice roll to server via WebSocket.
     * DISABLED FOR LOCAL MODE.
     *
     * @param diceValue The dice result
     */
    private void sendDiceRollToServer(int diceValue) {
        // WebSocket disabled for local mode

        // Original code commented
        /*
        if (!webSocketService.isConnected()) {
            System.err.println("Cannot send dice roll - WebSocket not connected");
            return;
        }

        // Create dice roll payload
        Map<String, Object> rollPayload = new HashMap<>();
        rollPayload.put("gameId", currentGameId);
        rollPayload.put("action", "ROLL");
        rollPayload.put("diceValue", diceValue);
        rollPayload.put("playerColor", gameEngine.getCurrentPlayer().getColor().name());
        rollPayload.put("timestamp", System.currentTimeMillis());

        // Send to server
        String destination = "/app/game/" + currentGameId + "/roll";
        webSocketService.send(destination, rollPayload);

        System.out.println("📤 Sent dice roll to server: " + diceValue);
        */
    }

    // ══════════════════════════════════════════════════════════════════════════
    // WebSocket Message Classes
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Represents a game state update from server.
     */
    public static class GameStateUpdate {
        private String gameId;
        private String currentPlayerColor;
        private int diceValue;
        private String message;
        private String status;
        private PieceMove movedPiece;

        // Getters and setters
        public String getGameId() { return gameId; }
        public void setGameId(String gameId) { this.gameId = gameId; }

        public String getCurrentPlayerColor() { return currentPlayerColor; }
        public void setCurrentPlayerColor(String currentPlayerColor) {
            this.currentPlayerColor = currentPlayerColor;
        }

        public int getDiceValue() { return diceValue; }
        public void setDiceValue(int diceValue) { this.diceValue = diceValue; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public PieceMove getMovedPiece() { return movedPiece; }
        public void setMovedPiece(PieceMove movedPiece) { this.movedPiece = movedPiece; }

        @Override
        public String toString() {
            return "GameStateUpdate{gameId=" + gameId +
                    ", player=" + currentPlayerColor +
                    ", dice=" + diceValue +
                    ", message=" + message + "}";
        }
    }

    /**
     * Represents a piece move in game state update.
     */
    public static class PieceMove {
        private String playerColor;
        private int pieceNumber;
        private int diceValue;
        private int fromPosition;
        private int toPosition;

        // Getters and setters
        public String getPlayerColor() { return playerColor; }
        public void setPlayerColor(String playerColor) { this.playerColor = playerColor; }

        public int getPieceNumber() { return pieceNumber; }
        public void setPieceNumber(int pieceNumber) { this.pieceNumber = pieceNumber; }

        public int getDiceValue() { return diceValue; }
        public void setDiceValue(int diceValue) { this.diceValue = diceValue; }

        public int getFromPosition() { return fromPosition; }
        public void setFromPosition(int fromPosition) { this.fromPosition = fromPosition; }

        public int getToPosition() { return toPosition; }
        public void setToPosition(int toPosition) { this.toPosition = toPosition; }
    }
}