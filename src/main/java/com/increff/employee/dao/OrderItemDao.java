package com.increff.employee.dao;

import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.OrderItemPojo;
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
public class OrderItemDao extends AbstractDao {
    private static final String delete_id = "delete from OrderItemPojo p where id=:id";
    private static final String select_id = "select p from OrderItemPojo p where id=:id";
    private static final String select_barcode = "select p from OrderItemPojo p where barcode=:barcode and order_id!=0";
    private static final String select_all = "select p from OrderItemPojo p";
    private static final String select_order_id = "select p from OrderItemPojo p where barcode=:barcode and order_id=0";
    private static final String select_with_order_id = "select p from OrderItemPojo p where order_id=:order_id";


    @PersistenceContext
    @Autowired
    private final EntityManager em = getEntityManager();

    @Transactional
    public OrderItemPojo insert(OrderItemPojo p) {
        em.persist(p);
        return p;
    }

    @Transactional
    public void insertOrder(OrderPojo orderPojo) {
        em.persist(orderPojo);
    }

    public OrderItemPojo select(Integer id) throws ApiException {
        try {
            TypedQuery<OrderItemPojo> query = getQuery(select_id, OrderItemPojo.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        }
        catch (Exception e) {
            throw new ApiException("Product with id: " + id + " does not exit");
        }
    }

    public List<OrderItemPojo> selectSingleOrder(Integer order_id) {
        TypedQuery<OrderItemPojo> query = getQuery(select_with_order_id, OrderItemPojo.class);
        query.setParameter("order_id", order_id);
        return query.getResultList();
    }

    public List<OrderItemPojo> select1(String barcode) {
        TypedQuery<OrderItemPojo> query = getQuery(select_barcode, OrderItemPojo.class);
        query.setParameter("barcode", barcode);
        return query.getResultList();
    }

    public OrderItemPojo selectOrderId(String barcode) {
        TypedQuery<OrderItemPojo> query = getQuery(select_order_id, OrderItemPojo.class);
        query.setParameter("barcode", barcode);
        return query.getSingleResult();
    }

    public List<OrderItemPojo> selectAll() {
        TypedQuery<OrderItemPojo> query = getQuery(select_all, OrderItemPojo.class);
        return query.getResultList();
    }

    public void update(OrderItemPojo p) {

    }
}
