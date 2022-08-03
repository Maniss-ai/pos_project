package com.increff.employee.dao;

import com.increff.employee.pojo.InventoryPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class InventoryDao extends AbstractDao {
    private static final String delete_id = "delete from InventoryPojo p where id=:id";
    private static final String select_id = "select p from InventoryPojo p where id=:id";
    private static final String select_id1 = "select p from InventoryPojo p where barcode_id=:barcode_id";
    private static final String select_all = "select p from InventoryPojo p";
    private static final String select_barcode = "select p from InventoryPojo p where barcode=:barcode";

    @PersistenceContext
    @Autowired
    private final EntityManager em = getEntityManager();

    @Transactional
    public InventoryPojo insert(InventoryPojo p) {
        em.persist(p);
        return p;
    }

    public void delete(int id) {
        Query query = em.createQuery(delete_id);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    public InventoryPojo select(int id) {
        TypedQuery<InventoryPojo> query = getQuery(select_id, InventoryPojo.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public InventoryPojo select1(int barcode_id) {
        TypedQuery<InventoryPojo> query = getQuery(select_id1, InventoryPojo.class);
        query.setParameter("barcode_id", barcode_id);
        return query.getSingleResult();
    }

    public List<InventoryPojo> selectAll() {
        TypedQuery<InventoryPojo> query = getQuery(select_all, InventoryPojo.class);
        return query.getResultList();
    }

    public void update(InventoryPojo p) {

    }
    public InventoryPojo selectBarcode(String barcode) {
        TypedQuery<InventoryPojo> query = getQuery(select_barcode, InventoryPojo.class);
        query.setParameter("barcode", barcode);
        return query.getSingleResult();
    }

}
