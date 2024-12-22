package com.example.drycleaning.repositories;

import com.example.drycleaning.models.BaseEntity;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T extends BaseEntity> {

    T create(T entity);

    Optional<T> findById(String id);

    T update(T entity);

    void delete(T entity);

    List<T> getAll();
}
