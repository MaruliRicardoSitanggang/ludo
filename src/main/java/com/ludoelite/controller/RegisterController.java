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

public class RegisterController extends BaseController implements Initializable {

    @FXML private TextField        usernameField;
    @FXML private TextField        emailField;
    @FXML private PasswordField    passwordField;
    @FXML private PasswordField    confirmPasswordField;
    @FXML private Button           registerButton;
    @FXML private Label            errorLabel;
    @FXML private ProgressIndicator spinner;

    private final AuthService authService = new AuthService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(spinner != null) spinner.setVisible(false);
        if(errorLabel != null) errorLabel.setVisible(false);
    }

    @FXML
    private void handleRegister() {
        clearErrors();

        String username = usernameField.getText().trim();
        String email    = emailField.getText().trim();
        String password = passwordField.getText();
        String confirm  = confirmPasswordField.getText();

        String usernameErr = Validator.validateUsername(username);
        String emailErr    = Validator.validateEmail(email);
        String passwordErr = Validator.validatePassword(password);
        String matchErr    = (passwordErr == null) ? Validator.validatePasswordMatch(password, confirm) : null;

        Validator.markField(usernameField,        usernameErr == null);
        Validator.markField(emailField,           emailErr    == null);
        Validator.markField(passwordField,        passwordErr == null);
        Validator.markField(confirmPasswordField, matchErr    == null);

        String firstError = usernameErr != null ? usernameErr
                : emailErr    != null ? emailErr
                : passwordErr != null ? passwordErr
                : matchErr;

        if (firstError != null) { showFieldError(firstError); return; }

        registerButton.setDisable(true);
        if(spinner != null) spinner.setVisible(true);

        runAsync(
                () -> {
                    authService.register(username, email, password);
                    return null;
                },
                ignored -> {
                    Platform.runLater(() -> {
                        registerButton.setDisable(false);
                        if(spinner != null) spinner.setVisible(false);
                        showInfo("Berhasil", "Akun berhasil dibuat! Silakan Login.");
                        ViewNavigator.navigateToLogin();
                    });
                },
                ex -> {
                    Platform.runLater(() -> {
                        registerButton.setDisable(false);
                        if(spinner != null) spinner.setVisible(false);
                        showFieldError("Gagal Mendaftar: Username atau Email mungkin sudah ada.");
                    });
                },
                spinner
        );
    }

    @FXML
    private void handleBackToLogin() {
        ViewNavigator.navigateToLogin();
    }

    private void showFieldError(String message) {
        if(errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
        }
    }

    private void clearErrors() {
        if(errorLabel != null) errorLabel.setVisible(false);
    }
}