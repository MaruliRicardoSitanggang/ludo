package com.ludoelite.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ludoelite.model.ApiError;
import com.ludoelite.util.SessionManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Base HTTP service providing shared infrastructure:
 *   - configured HttpClient (timeouts, redirect policy)
 *   - Jackson ObjectMapper
 *   - Authenticated GET / POST / PUT / DELETE helpers
 *   - Unified error response parsing
 *
 * Demonstrates: Inheritance (subclasses extend this), Encapsulation.
 */
public abstract class BaseHttpService {

    protected static final String BASE_URL = "http://localhost:8080/api";

    protected final HttpClient   httpClient;
    protected final ObjectMapper objectMapper;

    protected BaseHttpService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    // ── HTTP helpers ─────────────────────────────────────────────────────────

    /**
     * Authenticated GET request.
     */
    protected HttpResponse<String> get(String endpoint) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .header("Authorization", SessionManager.getInstance().getBearerHeader())
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Unauthenticated POST (used for login / register).
     */
    protected HttpResponse<String> postPublic(String endpoint, Object body)
            throws IOException, InterruptedException {
        String json = objectMapper.writeValueAsString(body);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(15))
                .POST(BodyPublishers.ofString(json))
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Authenticated POST.
     */
    protected HttpResponse<String> post(String endpoint, Object body)
            throws IOException, InterruptedException {
        String json = objectMapper.writeValueAsString(body);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .header("Authorization", SessionManager.getInstance().getBearerHeader())
                .timeout(Duration.ofSeconds(15))
                .POST(BodyPublishers.ofString(json))
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Authenticated PUT.
     */
    protected HttpResponse<String> put(String endpoint, Object body)
            throws IOException, InterruptedException {
        String json = objectMapper.writeValueAsString(body);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .header("Authorization", SessionManager.getInstance().getBearerHeader())
                .timeout(Duration.ofSeconds(15))
                .PUT(BodyPublishers.ofString(json))
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Authenticated DELETE.
     */
    protected HttpResponse<String> delete(String endpoint)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .header("Authorization", SessionManager.getInstance().getBearerHeader())
                .timeout(Duration.ofSeconds(15))
                .DELETE()
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    // ── Error parsing ────────────────────────────────────────────────────────

    /**
     * Extracts a human-readable error message from a non-2xx response body.
     */
    protected String extractErrorMessage(HttpResponse<String> response) {
        try {
            ApiError error = objectMapper.readValue(response.body(), ApiError.class);
            if (error.getMessage() != null && !error.getMessage().isBlank()) {
                return error.getMessage();
            }
        } catch (Exception ignored) { /* fall through to raw body */ }
        return "Server error " + response.statusCode() + ": " + response.body();
    }

    /**
     * Deserialise JSON body into the given class.
     */
    protected <T> T parseBody(String json, Class<T> clazz) throws IOException {
        return objectMapper.readValue(json, clazz);
    }

    /**
     * Deserialise JSON body into a generic/parameterised type (e.g. List<Game>).
     */
    protected <T> T parseBody(String json, TypeReference<T> typeRef) throws IOException {
        return objectMapper.readValue(json, typeRef);
    }
}
