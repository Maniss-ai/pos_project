package com.increff.employee.service;

import com.increff.employee.dao.ViewOrderDao;
import com.increff.employee.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class ViewOrderService {
    @Autowired
    private ViewOrderDao dao;

    @Transactional
    public List<OrderPojo> getSelectedOrdersWithoutId(LocalDate start_date, LocalDate end_date) {
        return dao.selectAllWithoutId(start_date, end_date);
    }

    @Transactional
    public List<OrderPojo> getSelectedOrdersWithId(LocalDate start_date, LocalDate end_date, int order_id) {
        return dao.selectAllWithId(start_date, end_date, order_id);
    }

    @Transactional
    public OrderPojo getOrder(int order_id) {
        return dao.getOrder(order_id);
    }
}
