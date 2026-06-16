package com.ludoelite.service;

import com.ludoelite.model.AuthRequest;
import com.ludoelite.model.AuthResponse;
import com.ludoelite.model.RegisterRequest;
import com.ludoelite.util.SessionManager;

import java.net.http.HttpResponse;

/**
 * Handles authentication: login and registration.
 * Extends BaseHttpService to reuse the shared HTTP infrastructure.
 *
 * Demonstrates: Inheritance (extends BaseHttpService).
 */
public class AuthService extends BaseHttpService {

    // BARU: Telah ditambahkan awalan /api
    private static final String LOGIN_ENDPOINT    = "/api/auth/login";
    private static final String REGISTER_ENDPOINT = "/api/auth/register";

    /**
     * Calls POST /api/auth/login, persists the JWT on success.
     *
     * @return AuthResponse with token and user details
     * @throws Exception with a user-friendly message on failure
     */
    public AuthResponse login(String username, String password) throws Exception {
        AuthRequest payload = new AuthRequest(username, password);
        HttpResponse<String> response = postPublic(LOGIN_ENDPOINT, payload);

        if (response.statusCode() == 200) {
            AuthResponse authResponse = parseBody(response.body(), AuthResponse.class);
            // Persist session
            SessionManager.getInstance().saveSession(authResponse.getToken(), authResponse.getUser());
            return authResponse;
        }

        throw new Exception(extractErrorMessage(response));
    }

    /**
     * Calls POST /api/auth/register.
     *
     * @throws Exception with a user-friendly message on failure
     */
    public void register(String username, String email, String password) throws Exception {
        RegisterRequest payload = new RegisterRequest(username, email, password);
        HttpResponse<String> response = postPublic(REGISTER_ENDPOINT, payload);

        if (response.statusCode() == 200 || response.statusCode() == 201) {
            return; // success
        }

        throw new Exception(extractErrorMessage(response));
    }
}