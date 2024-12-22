package com.example.drycleaning.repositories.impl;

import com.example.drycleaning.models.Role;
import com.example.drycleaning.models.UserRoles;
import com.example.drycleaning.repositories.BaseRepository;
import com.example.drycleaning.repositories.UserRoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRoleRepositoryImpl extends BaseRepositoryImpl<Role> implements UserRoleRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public UserRoleRepositoryImpl() {
        super(Role.class);
    }


    @Override
    public Optional<Role> findRoleByName(UserRoles role) {
        TypedQuery<Role> query = entityManager.createQuery(
                "select r from Role r where r.name = :roleName", Role.class);
        query.setParameter("roleName", role);
        return Optional.ofNullable(query.getSingleResult());
    }
}
