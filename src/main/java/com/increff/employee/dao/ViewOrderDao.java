package com.increff.employee.dao;

import com.increff.employee.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

@Repository
public class ViewOrderDao extends AbstractDao {
    private static final String select_all_without_id = "select p from OrderPojo p where time between :start_date and :end_date";
    private static final String select_all_with_id_date = "select p from OrderPojo p where (time between :start_date and :end_date) and order_id=:order_id";
    private static final String select_all_with_id = "select p from OrderPojo p where order_id=:order_id";
    private static final String select_order_with_order_id = "select o from OrderPojo o where order_id=:order_id";

    @PersistenceContext
    @Autowired
    private final EntityManager em = getEntityManager();

    public List<OrderPojo> selectAllWithoutId(LocalDate start_date, LocalDate end_date) {
        TypedQuery<OrderPojo> query = getQuery(select_all_without_id, OrderPojo.class);
        query.setParameter("start_date", start_date);
        query.setParameter("end_date", end_date);
        return query.getResultList();
    }

    public List<OrderPojo> selectAllWithIdAndDate(LocalDate start_date, LocalDate end_date, int order_id) {
        TypedQuery<OrderPojo> query = getQuery(select_all_with_id_date, OrderPojo.class);
        query.setParameter("start_date", start_date);
        query.setParameter("end_date", end_date);
        query.setParameter("order_id", order_id);
        return query.getResultList();
    }

    public List<OrderPojo> selectAllWithId(int order_id) {
        TypedQuery<OrderPojo> query = getQuery(select_all_with_id, OrderPojo.class);
        query.setParameter("order_id", order_id);
        return query.getResultList();
    }

    public OrderPojo getOrder(int order_id) {
        TypedQuery<OrderPojo> query = getQuery(select_order_with_order_id, OrderPojo.class);
        query.setParameter("order_id", order_id);
        return query.getSingleResult();
    }
}
