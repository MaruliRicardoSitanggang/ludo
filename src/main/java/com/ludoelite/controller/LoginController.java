package com.ludoelite.controller;

import com.ludoelite.service.AuthService;
import com.ludoelite.util.Validator;
import com.ludoelite.util.ViewNavigator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends BaseController implements Initializable {

    @FXML private TextField        usernameField;
    @FXML private PasswordField    passwordField;
    @FXML private Button           loginButton;
    @FXML private Button           registerLink;
    @FXML private Label            errorLabel;
    @FXML private ProgressIndicator spinner;

    private final AuthService authService = new AuthService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(spinner != null) spinner.setVisible(false);
        if(errorLabel != null) errorLabel.setVisible(false);

        passwordField.setOnAction(e -> handleLogin());

        usernameField.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused) validateUsername();
        });
        passwordField.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused) validatePassword();
        });
    }

    @FXML
    private void handleLogin() {
        clearErrors();

        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        String usernameErr = Validator.validateUsername(username);
        String passwordErr = Validator.validatePassword(password);

        Validator.markField(usernameField, usernameErr == null);
        Validator.markField(passwordField, passwordErr == null);

        if (usernameErr != null) { showFieldError(usernameErr); return; }
        if (passwordErr != null) { showFieldError(passwordErr); return; }

        loginButton.setDisable(true);
        if(spinner != null) spinner.setVisible(true);

        runAsync(
                () -> authService.login(username, password),
                authResponse -> {
                    Platform.runLater(() -> {
                        loginButton.setDisable(false);
                        if(spinner != null) spinner.setVisible(false);
                        ViewNavigator.navigateToDashboard();
                    });
                },
                ex -> {
                    Platform.runLater(() -> {
                        loginButton.setDisable(false);
                        if(spinner != null) spinner.setVisible(false);
                        showFieldError("Gagal Login: Periksa kembali username & password.");
                    });
                },
                spinner
        );
    }

    @FXML
    private void handleNavigateToRegister() {
        ViewNavigator.navigateToRegister();
    }

    private boolean validateUsername() {
        String err = Validator.validateUsername(usernameField.getText().trim());
        Validator.markField(usernameField, err == null);
        return err == null;
    }

    private boolean validatePassword() {
        String err = Validator.validatePassword(passwordField.getText());
        Validator.markField(passwordField, err == null);
        return err == null;
    }

    private void showFieldError(String message) {
        if(errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
        }
    }

    private void clearErrors() {
        if(errorLabel != null) errorLabel.setVisible(false);
        usernameField.getStyleClass().remove("field-error");
        passwordField.getStyleClass().remove("field-error");
    }
}