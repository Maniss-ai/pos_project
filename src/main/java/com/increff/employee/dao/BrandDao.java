package com.increff.employee.dao;

import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import jdk.nashorn.internal.ir.BreakableNode;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class BrandDao {
    private static final String delete_id = "delete from BrandPojo p where id=:id";
    private static final String select_id = "select p from BrandPojo p where id=:id";
    private static final String select_all = "select p from BrandPojo p";
    private static final String select_brand_category = "select p from BrandPojo p where brand=:brand and category=:category";

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void insert(BrandPojo p) {
        em.persist(p);
    }

    public void delete(int id) {
        Query query = em.createQuery(delete_id);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    public BrandPojo select(int id) throws ApiException {
        TypedQuery<BrandPojo> query = getQuery(select_id);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public List<BrandPojo> selectAll() {
        TypedQuery<BrandPojo> query = getQuery(select_all);
        return query.getResultList();
    }

    public void update(BrandPojo p) {

    }

    public BrandPojo getBrandCategory(ProductPojo pojo) throws ApiException {
        try {
            TypedQuery<BrandPojo> query = getQuery(select_brand_category);
            query.setParameter("brand", pojo.getBrand());
            query.setParameter("category", pojo.getCategory());
            System.out.println("DAO: " + query.getSingleResult());
            return query.getSingleResult();
        }
        catch (Exception e) {
            throw new ApiException("Brand Category doesn't exists ....");
        }
    }

    TypedQuery<BrandPojo> getQuery(String jpql) {
        return em.createQuery(jpql, BrandPojo.class);
    }
}
