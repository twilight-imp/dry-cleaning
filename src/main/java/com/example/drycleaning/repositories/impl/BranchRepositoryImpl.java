package com.example.drycleaning.repositories.impl;


import com.example.drycleaning.models.Branch;
import com.example.drycleaning.repositories.BranchRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BranchRepositoryImpl extends BaseRepositoryImpl<Branch> implements BranchRepository {
    @PersistenceContext
    private EntityManager entityManager;

   protected BranchRepositoryImpl() {
        super(Branch.class);
    }

    @Override
    public List<Branch> findByUserCity(String userCityId) {
        TypedQuery<Branch> query = entityManager.createQuery(
                "select b from Branch b " +
                        "join b.city c " +
                        "where c.id = :userCityId and b.isDeleted = false", Branch.class);
        return query.setParameter("userCityId", userCityId)
                .getResultList();
    }



    @Override
    public double findRatingByBranchId(String branchId) {
        TypedQuery<Double> query = entityManager.createQuery(
                "select coalesce(avg(r.score), 0) from Review r " +
                        "join r.order o " +
                        "join o.branch b " +
                        "where b.id = :branchId and b.isDeleted = false", Double.class);
        Double result = query.setParameter("branchId", branchId)
                .getSingleResult();

        String formattedResult = String.format("%.2f", result);

        formattedResult = formattedResult.replace(',', '.');

        return Double.parseDouble(formattedResult);
    }

    @Override
    public Branch findDefaultBranch(String userId) {
        TypedQuery<Branch> query = entityManager.createQuery(
                "select b from Branch b " +
                        "join b.city c " +
                        "join c.users u " +
                        "where u.id = :userId and b.isDeleted = false", Branch.class);

        return query.setParameter("userId", userId)
                .setMaxResults(1)
                .getSingleResult();
    }

}
