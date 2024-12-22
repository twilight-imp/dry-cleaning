package com.example.drycleaning.repositories.impl;


import com.example.drycleaning.models.User;
import com.example.drycleaning.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public class UserRepositoryImpl extends BaseRepositoryImpl<User> implements UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    protected UserRepositoryImpl() {
        super(User.class);
    }

    @Override
    public String findNameByReview(String reviewId) {
        TypedQuery<String> query = entityManager.createQuery(
                "select u.name from User u " +
                        "join u.order o " +
                        "join o.review r " +
                        "where r.id = :reviewId",String.class);
        query.setParameter("reviewId", reviewId);
        return query.getSingleResult();
    }

    @Override
    public Optional<User> findByPhone(String phone) {
        try {
            TypedQuery<User> query = entityManager.createQuery(
                    "select u from User u " +
                            "where u.phone = :phone and u.isDeleted = false", User.class);
        query.setParameter("phone", phone);

        return Optional.ofNullable(query.getSingleResult());
        }catch (NoResultException e) {
            return Optional.empty();
        }

    }

    @Override
    public boolean existsByPhone(String phone) {
        TypedQuery<Long> query = entityManager.createQuery(
                "select count(u) from User u where u.phone = :phone and u.isDeleted = false", Long.class);
        query.setParameter("phone", phone);
        return query.getSingleResult() > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        TypedQuery<Long> query = entityManager.createQuery(
                "select count(u) from User u where u.email = :email and u.isDeleted = false", Long.class);
        query.setParameter("email", email);
        return query.getSingleResult() > 0;
    }



    @Override
    public Optional<User> findByEmail(String email) {
        try {
            TypedQuery<User> query = entityManager.createQuery(
                    "select u from User u " +
                            "where u.email = :email and u.isDeleted = false", User.class);
            query.setParameter("email", email);
            return Optional.ofNullable(query.getSingleResult());
        }catch (NoResultException e) {
            return Optional.empty();
        }
    }


}
