package com.ludoelite.service;

import java.util.List;

/**
 * Generic CRUD contract for all API services.
 *
 * Demonstrates: Abstraction (interface) + Polymorphism (different concrete implementations).
 *
 * @param <T>  the entity type
 * @param <ID> the identifier type
 */
public interface ApiService<T, ID> {

    List<T> findAll() throws Exception;

    T findById(ID id) throws Exception;

    T create(T entity) throws Exception;

    T update(ID id, T entity) throws Exception;

    void delete(ID id) throws Exception;
}
