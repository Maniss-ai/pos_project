package com.increff.employee.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public abstract class AbstractDao {
    @PersistenceContext
    private EntityManager entityManager;

    protected <T> TypedQuery<T> getQuery(String jpql, Class<T> givenClass) {
        return entityManager.createQuery(jpql, givenClass);
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }
}
