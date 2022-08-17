package com.increff.employee.dao;

import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class BrandDao extends AbstractDao {
    private static final String SELECT_ID = "select p from BrandPojo p where id=:id";
    private static final String SELECT_ALL = "select p from BrandPojo p";
    private static final String SELECT_BRAND_CATEGORY = "select p from BrandPojo p where brand=:brand and category=:category";

    @PersistenceContext
    private final EntityManager em = getEntityManager();

    @Transactional
    public BrandPojo insert(BrandPojo p) {
        em.persist(p);
        return p;
    }

    public BrandPojo select(Integer id) {
            TypedQuery<BrandPojo> query = getQuery(SELECT_ID, BrandPojo.class);
            query.setParameter("id", id);
            return query.getSingleResult();
    }

    public List<BrandPojo> selectAll() {
        TypedQuery<BrandPojo> query = getQuery(SELECT_ALL, BrandPojo.class);
        return query.getResultList();
    }

    public BrandPojo getBrandCategory(ProductPojo pojo) {
            TypedQuery<BrandPojo> query = getQuery(SELECT_BRAND_CATEGORY, BrandPojo.class);
            query.setParameter("brand", pojo.getBrand());
            query.setParameter("category", pojo.getCategory());
            return query.getSingleResult();

    }

}
