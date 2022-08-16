package com.increff.employee.dao;

import com.increff.employee.model.form.ReportForm;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class ProductDao extends AbstractDao {
    private static final String delete_id = "delete from ProductPojo p where id=:id";
    private static final String select_id = "select p from ProductPojo p where id=:id";
    private static final String select_all = "select p from ProductPojo p";

    private static final String select_barcode = "select p from ProductPojo p where barcode=:barcode";
    private static final String select_with_brand_category = "select p from ProductPojo p where brand=:brand and category=:category";
    private static final String select_with_brand = "select p from ProductPojo p where brand=:brand";
    private static final String select_with_category = "select p from ProductPojo p where category=:category";

    @PersistenceContext
    @Autowired
    private final EntityManager em = getEntityManager();

    @Transactional
    public ProductPojo insert(ProductPojo p) {
        em.persist(p);
        return p;
    }

    public ProductPojo select(Integer id) throws ApiException {
        try {
            TypedQuery<ProductPojo> query = getQuery(select_id, ProductPojo.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        }
        catch (Exception e) {
            throw new ApiException("Barcode doesn't exists");
        }
    }

    public List<ProductPojo> selectAll() {
        TypedQuery<ProductPojo> query = getQuery(select_all, ProductPojo.class);
        return query.getResultList();
    }

    public void update(ProductPojo p) {

    }

    public List<ProductPojo> getProductWithBrandCategory(ReportForm form) {
        TypedQuery<ProductPojo> query = getQuery(select_with_brand_category, ProductPojo.class);
        query.setParameter("brand", form.getBrand());
        query.setParameter("category", form.getCategory());
        return query.getResultList();
    }

    public List<ProductPojo> getProductWithBrand(String brand) {
        TypedQuery<ProductPojo> query = getQuery(select_with_brand, ProductPojo.class);
        query.setParameter("brand", brand);
        return query.getResultList();
    }

    public List<ProductPojo> getProductWithCategory(ReportForm form) {
        TypedQuery<ProductPojo> query = getQuery(select_with_category, ProductPojo.class);
        query.setParameter("category", form.getCategory());
        return query.getResultList();
    }

    public List<ProductPojo> getProductWithDate(ReportForm form) {
        TypedQuery<ProductPojo> query = getQuery(select_with_brand_category, ProductPojo.class);
        query.setParameter("brand", form.getBrand());
        query.setParameter("category", form.getCategory());
        return query.getResultList();
    }

    public ProductPojo getInventoryBarcode(String barcode) throws ApiException {
        try {
            TypedQuery<ProductPojo> query = getQuery(select_barcode, ProductPojo.class);
            query.setParameter("barcode", barcode);
            return query.getSingleResult();
        }
        catch (Exception e) {
            throw new ApiException("Barcode doesn't exists");
        }
    }

}
