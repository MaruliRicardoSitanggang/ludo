package com.ludoelite.controller;

import com.ludoelite.util.AlertHelper;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;

/**
 * Abstract base controller providing shared utilities:
 *   - background task execution (keeps UI thread free)
 *   - standardised error / success alert helpers
 *
 * All concrete controllers extend this class.
 *
 * Demonstrates: Inheritance, Abstraction.
 */
public abstract class BaseController {

    /**
     * Runs a blocking operation on a background thread, then dispatches
     * success / failure callbacks back on the JavaFX Application Thread.
     *
     * @param task      the blocking work to execute
     * @param onSuccess called with the result on the FX thread
     * @param onError   called with the exception on the FX thread
     * @param spinner   optional ProgressIndicator to show/hide (can be null)
     */
    protected <T> void runAsync(
            java.util.concurrent.Callable<T> task,
            java.util.function.Consumer<T>   onSuccess,
            java.util.function.Consumer<Exception> onError,
            ProgressIndicator spinner) {

        Task<T> bgTask = new Task<>() {
            @Override
            protected T call() throws Exception {
                return task.call();
            }
        };

        bgTask.setOnSucceeded(e -> {
            if (spinner != null) spinner.setVisible(false);
            onSuccess.accept(bgTask.getValue());
        });

        bgTask.setOnFailed(e -> {
            if (spinner != null) spinner.setVisible(false);
            Throwable cause = bgTask.getException();
            onError.accept(cause instanceof Exception
                    ? (Exception) cause
                    : new Exception(cause.getMessage()));
        });

        if (spinner != null) {
            Platform.runLater(() -> spinner.setVisible(true));
        }

        Thread thread = new Thread(bgTask);
        thread.setDaemon(true);
        thread.start();
    }

    // ── Alert shortcuts ───────────────────────────────────────────────────────

    protected void showError(String header, String content) {
        AlertHelper.showError("Error", header, content);
    }

    protected void showInfo(String header, String content) {
        AlertHelper.showInfo("Success", header, content);
    }

    protected void showWarning(String header, String content) {
        AlertHelper.showWarning("Warning", header, content);
    }

    protected boolean confirmAction(String header, String content) {
        return AlertHelper.showConfirmation("Confirm", header, content);
    }
}
