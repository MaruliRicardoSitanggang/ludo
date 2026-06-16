package com.ludoelite.util;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Utility class for displaying standardised JavaFX Alert dialogs.
 * Ensures all alerts are shown on the JavaFX Application Thread.
 */
public final class AlertHelper {

    private AlertHelper() { /* utility class */ }

    public static void showError(String title, String header, String content) {
        runOnFxThread(() -> {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);
            styleAlert(alert);
            alert.showAndWait();
        });
    }

    public static void showInfo(String title, String header, String content) {
        runOnFxThread(() -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);
            styleAlert(alert);
            alert.showAndWait();
        });
    }

    public static void showWarning(String title, String header, String content) {
        runOnFxThread(() -> {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);
            styleAlert(alert);
            alert.showAndWait();
        });
    }

    /**
     * Confirmation dialog. Returns true if the user clicked OK.
     * Must be called from the FX thread.
     */
    public static boolean showConfirmation(String title, String header, String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        styleAlert(alert);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    // ── Internals ────────────────────────────────────────────────────────────

    private static void styleAlert(Alert alert) {
        try {
            alert.getDialogPane()
                 .getStylesheets()
                 .add(AlertHelper.class.getResource("/css/styles.css").toExternalForm());
        } catch (Exception ignored) { /* CSS is optional */ }
    }

    private static void runOnFxThread(Runnable action) {
        if (Platform.isFxApplicationThread()) {
            action.run();
        } else {
            Platform.runLater(action);
        }
    }
}
