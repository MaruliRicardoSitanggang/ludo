package com.ludoelite.controller;

import com.ludoelite.model.Game;
import com.ludoelite.model.User;
import com.ludoelite.service.GameService;
import com.ludoelite.util.AlertHelper;
import com.ludoelite.util.SessionManager;
import com.ludoelite.util.ViewNavigator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class GameManagementController extends BaseController {

    @FXML private Label welcomeLabel;
    @FXML private TableView<Game> gameTable;
    @FXML private TableColumn<Game, Long> idColumn;
    @FXML private TableColumn<Game, String> statusColumn;
    @FXML private TableColumn<Game, Integer> playersColumn;
    @FXML private TextField gameIdField;

    private GameService gameService;
    private ObservableList<Game> gameList;

    @FXML
    public void initialize() {
        this.gameService = new GameService();
        this.gameList = FXCollections.observableArrayList();

        // FIX: Pengaman agar tidak Crash jika ID welcomeLabel dihapus dari layar FXML
        if (welcomeLabel != null) {
            User currentUser = SessionManager.getInstance().getCurrentUser();
            if (currentUser != null) {
                welcomeLabel.setText("Welcome, " + currentUser.getUsername() + "!");
            }
        }

        setupTable();
        loadGames();
    }

    private void setupTable() {
        if (gameTable != null) {
            // FIX: Pengaman untuk setiap kolom tabel
            if (idColumn != null) idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            if (statusColumn != null) statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
            if (playersColumn != null) playersColumn.setCellValueFactory(new PropertyValueFactory<>("currentPlayers"));

            gameTable.setItems(gameList);

            gameTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null && gameIdField != null) {
                    gameIdField.setText(String.valueOf(newSelection.getId()));
                }
            });
        }
    }

    @FXML
    private void handleCreateGame() {
        gameService.createGame().thenAccept(game -> {
            Platform.runLater(() -> {
                AlertHelper.showSuccess("Berhasil", "Ruang Game Dibuat! Room ID: " + game.getId());
                SessionManager.getInstance().setCurrentGameId(game.getId());
                ViewNavigator.getInstance().navigateTo("LudoBoardView");
            });
        }).exceptionally(ex -> {
            Platform.runLater(() -> AlertHelper.showError("Error", "Gagal membuat game: " + ex.getMessage()));
            return null;
        });
    }

    @FXML
    private void handleJoinGame() {
        String gameIdStr = gameIdField != null ? gameIdField.getText() : "";
        if (gameIdStr == null || gameIdStr.trim().isEmpty()) {
            AlertHelper.showError("Error", "Masukkan Game ID atau pilih dari tabel terlebih dahulu.");
            return;
        }

        try {
            Long gameId = Long.parseLong(gameIdStr.trim());
            gameService.joinGame(gameId).thenAccept(game -> {
                Platform.runLater(() -> {
                    AlertHelper.showSuccess("Berhasil", "Berhasil Masuk ke Room " + game.getId());
                    SessionManager.getInstance().setCurrentGameId(game.getId());
                    ViewNavigator.getInstance().navigateTo("LudoBoardView");
                });
            }).exceptionally(ex -> {
                Platform.runLater(() -> AlertHelper.showError("Error", "Gagal masuk game: " + ex.getMessage()));
                return null;
            });
        } catch (NumberFormatException e) {
            AlertHelper.showError("Error", "Format Game ID tidak valid. Harus berupa angka.");
        }
    }

    @FXML
    private void handleRefresh() {
        loadGames();
    }

    @FXML
    private void handleLogout() {
        SessionManager.getInstance().clearSession();
        ViewNavigator.getInstance().navigateTo("LoginView");
    }

    private void loadGames() {
        gameService.getAvailableGames().thenAccept(games -> {
            Platform.runLater(() -> {
                gameList.clear();
                if (games != null) {
                    gameList.addAll(games);
                }
            });
        }).exceptionally(ex -> {
            Platform.runLater(() -> AlertHelper.showError("Peringatan", "Gagal mengambil data dari server. Pastikan Backend menyala."));
            return null;
        });
    }
}