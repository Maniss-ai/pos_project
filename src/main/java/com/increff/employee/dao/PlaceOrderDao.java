package com.increff.employee.dao;

import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.PlaceOrderPojo;
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
public class PlaceOrderDao extends AbstractDao {
    private static final String delete_id = "delete from PlaceOrderPojo p where id=:id";
    private static final String select_id = "select p from PlaceOrderPojo p where id=:id";
    private static final String select_barcode = "select p from PlaceOrderPojo p where barcode=:barcode and order_id!=0";
    private static final String select_all = "select p from PlaceOrderPojo p";
    private static final String select_order_id = "select p from PlaceOrderPojo p where barcode=:barcode and order_id=0";
    private static final String select_with_order_id = "select p from PlaceOrderPojo p where order_id=:order_id";


    @PersistenceContext
    @Autowired
    private final EntityManager em = getEntityManager();

    @Transactional
    public PlaceOrderPojo insert(PlaceOrderPojo p) {
        em.persist(p);
        return p;
    }

    @Transactional
    public void insertOrder(OrderPojo orderPojo) {
        em.persist(orderPojo);
    }

    public void delete(int id) {
        Query query = em.createQuery(delete_id);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    public PlaceOrderPojo select(int id) throws ApiException {
        try {
            TypedQuery<PlaceOrderPojo> query = getQuery(select_id, PlaceOrderPojo.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        }
        catch (Exception e) {
            throw new ApiException("Product with id: " + id + " does not exit");
        }
    }

    public List<PlaceOrderPojo> selectSingleOrder(int order_id) {
        TypedQuery<PlaceOrderPojo> query = getQuery(select_with_order_id, PlaceOrderPojo.class);
        query.setParameter("order_id", order_id);
        return query.getResultList();
    }

    public List<PlaceOrderPojo> select1(String barcode) {
        TypedQuery<PlaceOrderPojo> query = getQuery(select_barcode, PlaceOrderPojo.class);
        query.setParameter("barcode", barcode);
        return query.getResultList();
    }

    public PlaceOrderPojo selectOrderId(String barcode) {
        TypedQuery<PlaceOrderPojo> query = getQuery(select_order_id, PlaceOrderPojo.class);
        query.setParameter("barcode", barcode);
        return query.getSingleResult();
    }

    public List<PlaceOrderPojo> selectAll() {
        TypedQuery<PlaceOrderPojo> query = getQuery(select_all, PlaceOrderPojo.class);
        return query.getResultList();
    }

    public void update(PlaceOrderPojo p) {

    }
}
