package com.example.drycleaning.repositories.impl;

import com.example.drycleaning.models.Service;
import com.example.drycleaning.repositories.ServiceRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class ServiceRepositoryImpl extends BaseRepositoryImpl<Service> implements ServiceRepository {
    @PersistenceContext
    private EntityManager entityManager;

    protected ServiceRepositoryImpl() {
        super(Service.class);
    }

    @Override
    public List<Service> findPopularFromDate(LocalDate localDate) {
        TypedQuery<Service> query = entityManager.createQuery(
                "select s from Service s " +
                        "join OrderPosition op on s.id = op.service.id " +
                        "join Order o on op.order.id = o.id " +
                        "where o.date >= :localDate and s.isDeleted = false " +
                        "group by s.id " +
                        "order by count(op.id) desc", Service.class);
        return query.setParameter("localDate", localDate)
                .getResultList();
    }

}
