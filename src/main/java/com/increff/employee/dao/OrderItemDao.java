package com.increff.employee.dao;

import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.service.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class OrderItemDao extends AbstractDao {
    private static final String SELECT_ID = "select p from OrderItemPojo p where id=:id";
    private static final String SELECT_BARCODE = "select p from OrderItemPojo p where barcode=:barcode and orderId!=0";
    private static final String SELECT_ALL = "select p from OrderItemPojo p";
    private static final String SELECT_ORDER_ID = "select p from OrderItemPojo p where productId=:productId and orderId=0";
    private static final String SELECT_WITH_ORDER_ID = "select p from OrderItemPojo p where orderId=:orderId";
    private static final String SELECT_WITH_PRODUCT_ID = "select p from OrderItemPojo p where productId=:productId and orderId!=0";


    @PersistenceContext
    @Autowired
    private EntityManager entityManager;

    @Transactional
    public OrderItemPojo insert(OrderItemPojo p) {
        entityManager.persist(p);
        return p;
    }

    @Transactional
    public void insertOrder(OrderPojo orderPojo) {
        entityManager.persist(orderPojo);
    }

    public OrderItemPojo select(Integer id) throws ApiException {
        TypedQuery<OrderItemPojo> query = getQuery(SELECT_ID, OrderItemPojo.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public List<OrderItemPojo> selectSingleOrder(Integer orderId) {
        TypedQuery<OrderItemPojo> query = getQuery(SELECT_WITH_ORDER_ID, OrderItemPojo.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }

    public List<OrderItemPojo> select1(String barcode) {
        TypedQuery<OrderItemPojo> query = getQuery(SELECT_BARCODE, OrderItemPojo.class);
        query.setParameter("barcode", barcode);
        return query.getResultList();
    }

    public OrderItemPojo selectOrderId(Integer productId) {
        TypedQuery<OrderItemPojo> query = getQuery(SELECT_ORDER_ID, OrderItemPojo.class);
        query.setParameter("productId", productId);
        return query.getSingleResult();
    }

    public List<OrderItemPojo> selectAll() {
        TypedQuery<OrderItemPojo> query = getQuery(SELECT_ALL, OrderItemPojo.class);
        return query.getResultList();
    }

    public List<OrderItemPojo> selectWithProductId(Integer productId) {
        TypedQuery<OrderItemPojo> query = getQuery(SELECT_WITH_PRODUCT_ID, OrderItemPojo.class);
        query.setParameter("productId", productId);
        return query.getResultList();
    }
}
