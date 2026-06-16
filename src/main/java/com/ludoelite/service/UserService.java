package com.ludoelite.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ludoelite.model.User;

import java.net.http.HttpResponse;
import java.util.List;

/**
 * CRUD service for User entities (admin operations).
 *
 * Demonstrates: Inheritance (extends BaseHttpService),
 *               Polymorphism (implements ApiService interface).
 */
public class UserService extends BaseHttpService implements ApiService<User, Long> {

    private static final String USERS_ENDPOINT = "/users";

    @Override
    public List<User> findAll() throws Exception {
        HttpResponse<String> response = get(USERS_ENDPOINT);
        if (response.statusCode() == 200) {
            return parseBody(response.body(), new TypeReference<List<User>>() {});
        }
        throw new Exception(extractErrorMessage(response));
    }

    @Override
    public User findById(Long id) throws Exception {
        HttpResponse<String> response = get(USERS_ENDPOINT + "/" + id);
        if (response.statusCode() == 200) {
            return parseBody(response.body(), User.class);
        }
        throw new Exception(extractErrorMessage(response));
    }

    @Override
    public User create(User user) throws Exception {
        HttpResponse<String> response = post(USERS_ENDPOINT, user);
        if (response.statusCode() == 200 || response.statusCode() == 201) {
            return parseBody(response.body(), User.class);
        }
        throw new Exception(extractErrorMessage(response));
    }

    @Override
    public User update(Long id, User user) throws Exception {
        HttpResponse<String> response = put(USERS_ENDPOINT + "/" + id, user);
        if (response.statusCode() == 200) {
            return parseBody(response.body(), User.class);
        }
        throw new Exception(extractErrorMessage(response));
    }

    @Override
    public void delete(Long id) throws Exception {
        HttpResponse<String> response = delete(USERS_ENDPOINT + "/" + id);
        if (response.statusCode() != 200 && response.statusCode() != 204) {
            throw new Exception(extractErrorMessage(response));
        }
    }
}
