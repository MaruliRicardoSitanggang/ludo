package com.ludoelite.util;

import java.util.regex.Pattern;

/**
 * Client-side validation utilities.
 * All methods return a String error message or null if valid.
 *
 * Demonstrates: Abstraction (static utility contract), Encapsulation.
 */
public final class Validator {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^[A-Za-z0-9_]{3,20}$");

    private static final Pattern NUMERIC_PATTERN =
            Pattern.compile("^\\d+$");

    private Validator() { /* utility class */ }

    // ── Field validators ─────────────────────────────────────────────────────

    /**
     * Validates that a field is not null or blank.
     * @return error message or null
     */
    public static String requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            return fieldName + " cannot be empty.";
        }
        return null;
    }

    /**
     * Validates email format using RFC-compatible regex.
     */
    public static String validateEmail(String email) {
        String blank = requireNonBlank(email, "Email");
        if (blank != null) return blank;
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            return "Please enter a valid email address.";
        }
        return null;
    }

    /**
     * Validates a username (3–20 alphanumeric/underscore characters).
     */
    public static String validateUsername(String username) {
        String blank = requireNonBlank(username, "Username");
        if (blank != null) return blank;
        if (!USERNAME_PATTERN.matcher(username.trim()).matches()) {
            return "Username must be 3–20 characters (letters, numbers, underscores).";
        }
        return null;
    }

    /**
     * Validates password length (minimum 6 characters).
     */
    public static String validatePassword(String password) {
        String blank = requireNonBlank(password, "Password");
        if (blank != null) return blank;
        if (password.length() < 6) {
            return "Password must be at least 6 characters.";
        }
        return null;
    }

    /**
     * Validates that two password strings match.
     */
    public static String validatePasswordMatch(String password, String confirm) {
        if (!password.equals(confirm)) {
            return "Passwords do not match.";
        }
        return null;
    }

    /**
     * Validates that a string contains only digits.
     */
    public static String validateNumeric(String value, String fieldName) {
        String blank = requireNonBlank(value, fieldName);
        if (blank != null) return blank;
        if (!NUMERIC_PATTERN.matcher(value.trim()).matches()) {
            return fieldName + " must contain numbers only.";
        }
        return null;
    }

    /**
     * Validates a positive integer within a range.
     */
    public static String validateIntRange(String value, String fieldName, int min, int max) {
        String numeric = validateNumeric(value, fieldName);
        if (numeric != null) return numeric;
        int parsed = Integer.parseInt(value.trim());
        if (parsed < min || parsed > max) {
            return fieldName + " must be between " + min + " and " + max + ".";
        }
        return null;
    }

    /**
     * Applies a red-border style to signal an invalid field,
     * or removes it when valid.
     */
    public static void markField(javafx.scene.control.Control field, boolean valid) {
        if (valid) {
            field.getStyleClass().remove("field-error");
        } else {
            if (!field.getStyleClass().contains("field-error")) {
                field.getStyleClass().add("field-error");
            }
        }
    }
}
