package com.increff.employee.dao;

import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.service.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class InventoryDao extends AbstractDao {
    private static final String SELECT_ID = "select p from InventoryPojo p where id=:id";
    private static final String SELECT_ALL = "select p from InventoryPojo p";
//    private static final String SELECT_BARCODE = "select p from InventoryPojo p where barcode=:barcode";

    @PersistenceContext
    @Autowired
    private final EntityManager entityManager = getEntityManager();

    @Transactional
    public InventoryPojo insert(InventoryPojo p) {
        entityManager.persist(p);
        return p;
    }

    public InventoryPojo select(Integer id) {
        TypedQuery<InventoryPojo> query = getQuery(SELECT_ID, InventoryPojo.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public List<InventoryPojo> selectAll() {
        TypedQuery<InventoryPojo> query = getQuery(SELECT_ALL, InventoryPojo.class);
        return query.getResultList();
    }

    public InventoryPojo selectBarcode(Integer productId) {
        TypedQuery<InventoryPojo> query = getQuery(SELECT_ID, InventoryPojo.class);
        query.setParameter("id", productId);
        return query.getSingleResult();
    }

}
