package com.increff.employee.dao;

import com.increff.employee.model.form.ReportForm;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class ProductDao extends AbstractDao {
    private static final String SELECT_ID = "select p from ProductPojo p where id=:id";
    private static final String SELECT_ALL = "select p from ProductPojo p";
    private static final String SELECT_ID_WITH_BRAND_CATEGORY = "select p from ProductPojo p where brandCategoryId=:brandCategory";

    private static final String SELECT_BARCODE = "select p from ProductPojo p where barcode=:barcode";

    @PersistenceContext
    @Autowired
    private final EntityManager entityManager = getEntityManager();

    @Transactional
    public ProductPojo insert(ProductPojo p) {
        entityManager.persist(p);
        return p;
    }

    public ProductPojo select(Integer id) throws ApiException {
        TypedQuery<ProductPojo> query = getQuery(SELECT_ID, ProductPojo.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public List<ProductPojo> selectAll() {
        TypedQuery<ProductPojo> query = getQuery(SELECT_ALL, ProductPojo.class);
        return query.getResultList();
    }

    public ProductPojo getInventoryBarcode(String barcode) {
        TypedQuery<ProductPojo> query = getQuery(SELECT_BARCODE, ProductPojo.class);
        query.setParameter("barcode", barcode);
        return query.getSingleResult();
    }

    public List<ProductPojo> getWithBrandCategory(Integer brandCategory) {
        TypedQuery<ProductPojo> query = getQuery(SELECT_ID_WITH_BRAND_CATEGORY, ProductPojo.class);
        query.setParameter("brandCategory", brandCategory);
        return query.getResultList();
    }

}
