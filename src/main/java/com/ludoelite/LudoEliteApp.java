package com.ludoelite;

import com.ludoelite.util.SessionManager;
import com.ludoelite.util.ViewNavigator;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main entry point for the Ludo Elite Desktop Application.
 * Bootstraps the JavaFX runtime and delegates navigation to ViewNavigator.
 */
public class LudoEliteApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Ludo Elite");
        primaryStage.setMinWidth(1100);
        primaryStage.setMinHeight(700);
        primaryStage.setResizable(true);

        // Initialize the navigator with the primary stage
        ViewNavigator.init(primaryStage);

        // Navigate to dashboard (local mode)
        // For multiplayer mode with authentication, uncomment below:
        // if (SessionManager.getInstance().isLoggedIn()) {
        //     ViewNavigator.navigateToDashboard();
        // } else {
        //     ViewNavigator.navigateToLogin();
        // }
        ViewNavigator.navigateToDashboard();

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}