package com.ludoelite.controller;

import com.ludoelite.model.Game;
import com.ludoelite.util.SessionManager;
import com.ludoelite.util.ViewNavigator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for DashboardView.fxml.
 * Simple dashboard for local game mode.
 */
public class DashboardController extends BaseController implements Initializable {

    // Sidebar
    @FXML private Label welcomeLabel;

    // Stats
    @FXML private Text statsTotal;
    @FXML private Text statsWaiting;
    @FXML private Text statsPlaying;

    // Table
    @FXML private TableView<Game> gamesTable;
    @FXML private TableColumn<Game, Long> colId;
    @FXML private TableColumn<Game, String> colName;
    @FXML private TableColumn<Game, String> colStatus;
    @FXML private TableColumn<Game, String> colHost;
    @FXML private TableColumn<Game, Integer> colPlayers;

    @FXML private ProgressIndicator spinner;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Greet the player
        String name = SessionManager.getInstance().getPersistedUsername();
        welcomeLabel.setText("Welcome back, " + (name.isBlank() ? "Player" : name) + "!");

        // Initialize table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("gameName"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colHost.setCellValueFactory(new PropertyValueFactory<>("hostUsername"));
        colPlayers.setCellValueFactory(new PropertyValueFactory<>("currentPlayers"));

        // Set local mode message
        gamesTable.setPlaceholder(new Label("🎮 Local Mode - Click 'Play Game' to start!"));
        
        // Set stats to 0 for local mode
        statsTotal.setText("0");
        statsWaiting.setText("0");
        statsPlaying.setText("0");
        
        // Hide spinner
        spinner.setVisible(false);
    }

    @FXML
    private void handleManageGames() {
        ViewNavigator.navigateToGameManagement();
    }

    @FXML
    private void handlePlayGame() {
        // Navigate directly to game board
        ViewNavigator.navigateToLudoBoard();
    }

    @FXML
    private void handleRefresh() {
        // In local mode, just reset the display
        gamesTable.setPlaceholder(new Label("🎮 Local Mode - Click 'Play Game' to start!"));
    }

    @FXML
    private void handleLogout() {
        if (confirmAction("Logout", "Are you sure you want to logout?")) {
            SessionManager.getInstance().clearSession();
            ViewNavigator.navigateToLogin();
        }
    }
}