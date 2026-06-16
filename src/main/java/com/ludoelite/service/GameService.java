package com.ludoelite.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ludoelite.model.Game;

import java.net.http.HttpResponse;
import java.util.List;

/**
 * CRUD service for Game entities.
 * Extends BaseHttpService and implements ApiService<Game, Long>.
 *
 * Demonstrates: Inheritance (extends BaseHttpService),
 *               Polymorphism (implements ApiService interface).
 */
public class GameService extends BaseHttpService implements ApiService<Game, Long> {

    private static final String GAMES_ENDPOINT = "/games";

    @Override
    public List<Game> findAll() throws Exception {
        HttpResponse<String> response = get(GAMES_ENDPOINT);
        if (response.statusCode() == 200) {
            return parseBody(response.body(), new TypeReference<List<Game>>() {});
        }
        throw new Exception(extractErrorMessage(response));
    }

    @Override
    public Game findById(Long id) throws Exception {
        HttpResponse<String> response = get(GAMES_ENDPOINT + "/" + id);
        if (response.statusCode() == 200) {
            return parseBody(response.body(), Game.class);
        }
        throw new Exception(extractErrorMessage(response));
    }

    @Override
    public Game create(Game game) throws Exception {
        HttpResponse<String> response = post(GAMES_ENDPOINT, game);
        if (response.statusCode() == 200 || response.statusCode() == 201) {
            return parseBody(response.body(), Game.class);
        }
        throw new Exception(extractErrorMessage(response));
    }

    @Override
    public Game update(Long id, Game game) throws Exception {
        HttpResponse<String> response = put(GAMES_ENDPOINT + "/" + id, game);
        if (response.statusCode() == 200) {
            return parseBody(response.body(), Game.class);
        }
        throw new Exception(extractErrorMessage(response));
    }

    @Override
    public void delete(Long id) throws Exception {
        HttpResponse<String> response = delete(GAMES_ENDPOINT + "/" + id);
        if (response.statusCode() != 200 && response.statusCode() != 204) {
            throw new Exception(extractErrorMessage(response));
        }
    }

    // ── Extra domain methods ──────────────────────────────────────────────────

    /**
     * Fetches only games that are currently waiting for players.
     */
    public List<Game> findWaitingGames() throws Exception {
        HttpResponse<String> response = get(GAMES_ENDPOINT + "?status=WAITING");
        if (response.statusCode() == 200) {
            return parseBody(response.body(), new TypeReference<List<Game>>() {});
        }
        throw new Exception(extractErrorMessage(response));
    }
}
