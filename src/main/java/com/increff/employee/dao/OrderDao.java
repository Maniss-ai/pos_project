package com.increff.employee.dao;

import com.increff.employee.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public class OrderDao extends AbstractDao {
    private static final String SELECT_ALL_WITHOUT_ID = "select p from OrderPojo p where time between :startDate and :endDate";
    private static final String SELECT_ALL_WITH_ID = "select p from OrderPojo p where orderId=:orderId";
    private static final String SELECT_ORDER_WITH_ORDER_ID = "select o from OrderPojo o where orderId=:orderId";

    @PersistenceContext
    @Autowired
    private final EntityManager entityManager = getEntityManager();

    public List<OrderPojo> selectAllWithoutId(ZonedDateTime startDate, ZonedDateTime endDate) {
        TypedQuery<OrderPojo> query = getQuery(SELECT_ALL_WITHOUT_ID, OrderPojo.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }

    public List<OrderPojo> selectAllWithId(Integer orderId) {
        TypedQuery<OrderPojo> query = getQuery(SELECT_ALL_WITH_ID, OrderPojo.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }

    public OrderPojo getOrder(Integer orderId) {
        TypedQuery<OrderPojo> query = getQuery(SELECT_ORDER_WITH_ORDER_ID, OrderPojo.class);
        query.setParameter("orderId", orderId);
        return query.getSingleResult();
    }
}
