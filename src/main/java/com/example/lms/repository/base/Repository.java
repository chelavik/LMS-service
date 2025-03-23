package com.example.lms.repository.base;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    List<T> getAll();

    Optional<T> get(long id);

    T create(T entity);

    T update(long id, T entity);

    void delete(long id);
}
