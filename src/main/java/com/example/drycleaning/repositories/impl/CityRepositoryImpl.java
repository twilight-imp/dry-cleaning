package com.example.drycleaning.repositories.impl;


import com.example.drycleaning.models.City;
import com.example.drycleaning.repositories.CityRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class CityRepositoryImpl extends BaseRepositoryImpl<City> implements CityRepository {

    @PersistenceContext
    private EntityManager entityManager;

    protected CityRepositoryImpl() {
        super(City.class);
    }

    @Override
    public String findIdByName(String cityName) {
        TypedQuery<String> query = entityManager.createQuery(
                "select c.name from City c " +
                        "where c.id = :cityName and c.isDeleted = false", String.class);
        return query.setParameter("cityName", cityName)
                .getSingleResult();
    }

    @Override
    public String findIdByBranchId(String branchId) {
        TypedQuery<String> query = entityManager.createQuery(
                "select b.city.id from Branch b " +
                        "where b.id = :branchId", String.class);
        return query.setParameter("branchId", branchId)
                .getSingleResult();
    }

}
