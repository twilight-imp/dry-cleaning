package com.example.drycleaning.repositories.impl;

import com.example.drycleaning.models.BaseEntity;
import com.example.drycleaning.repositories.BaseRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public abstract class BaseRepositoryImpl<T extends BaseEntity> implements BaseRepository<T> {

    @PersistenceContext
    private EntityManager entityManager;

    private final Class<T> entityClass;

    protected BaseRepositoryImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Transactional
    public T create(T entity){
        if (entity.getId() == null) {
            entityManager.persist(entity);
        } else {
            entityManager.merge(entity);
        }
        return entity;
    };

    @Transactional
    public Optional<T> findById(String id) {
        return Optional.ofNullable(entityManager.find(entityClass,id));
    }


    @Transactional
    public T update(T entity){
        return entityManager.merge(entity);
    };

    @Transactional
    public void delete(T entity) {
        entity.setIsDeleted(true);
        entityManager.merge(entity);
    }

    @Transactional
    public List<T> getAll() {
        TypedQuery<T> query = entityManager.createQuery(
                "from " + entityClass.getName() + " e " +
                        "where e.isDeleted = false", entityClass);
        return query.getResultList();
    }

}
