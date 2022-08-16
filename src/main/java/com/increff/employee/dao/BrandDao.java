package com.increff.employee.dao;

import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class BrandDao extends AbstractDao {
    private static final String delete_id = "delete from BrandPojo p where id=:id";
    private static final String select_id = "select p from BrandPojo p where id=:id";
    private static final String select_all = "select p from BrandPojo p";
    private static final String select_brand_category = "select p from BrandPojo p where brand=:brand and category=:category";

    @PersistenceContext
    private final EntityManager em = getEntityManager();

    @Transactional
    public BrandPojo insert(BrandPojo p) {
        em.persist(p);
        return p;
    }

    public BrandPojo select(Integer id) throws ApiException {
        try {
            TypedQuery<BrandPojo> query = getQuery(select_id, BrandPojo.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        }
        catch (Exception e) {
            throw new ApiException("Id doesn't exists");
        }
    }

    public List<BrandPojo> selectAll() {
        TypedQuery<BrandPojo> query = getQuery(select_all, BrandPojo.class);
        return query.getResultList();
    }

    public BrandPojo getBrandCategory(ProductPojo pojo) throws ApiException {
        try {
            TypedQuery<BrandPojo> query = getQuery(select_brand_category, BrandPojo.class);
            query.setParameter("brand", pojo.getBrand());
            query.setParameter("category", pojo.getCategory());
            return query.getSingleResult();
        }
        catch (Exception e) {
            throw new ApiException("Brand Category doesn't exists");
        }
    }

}
