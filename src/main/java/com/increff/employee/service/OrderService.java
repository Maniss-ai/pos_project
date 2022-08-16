package com.increff.employee.service;

import com.increff.employee.dao.OrderDao;
import com.increff.employee.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderDao dao;

    @Transactional
    public List<OrderPojo> getSelectedOrdersWithoutId(LocalDate start_date, LocalDate end_date) {
        return dao.selectAllWithoutId(start_date, end_date);
    }

    @Transactional
    public List<OrderPojo> getSelectedOrdersWithIdAndDate(LocalDate start_date, LocalDate end_date, Integer order_id) {
        return dao.selectAllWithIdAndDate(start_date, end_date, order_id);
    }
    @Transactional
    public List<OrderPojo> getSelectedOrdersWithId(Integer order_id) {
        return dao.selectAllWithId(order_id);
    }

    @Transactional
    public OrderPojo getOrder(Integer order_id) {
        return dao.getOrder(order_id);
    }
}
