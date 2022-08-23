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
    public List<OrderPojo> getSelectedOrdersWithoutId(LocalDate startDate, LocalDate endDate) {
        return dao.selectAllWithoutId(startDate, endDate);
    }

    @Transactional
    public List<OrderPojo> getSelectedOrdersWithId(Integer orderId) throws ApiException {
        try {
            List<OrderPojo> orderPojoList = dao.selectAllWithId(orderId);
            if(orderPojoList.size() == 0) {
                throw new ApiException("Order Id doesn't exists");
            }
            else {
                return orderPojoList;
            }
        }
        catch (Exception e) {
            throw new ApiException("Order Id doesn't exists");
        }
    }

    @Transactional
    public OrderPojo getOrder(Integer orderId) throws ApiException {
        try {
            return dao.getOrder(orderId);
        }
        catch (Exception e) {
            throw new ApiException("Order Id doesn't exists");
        }
    }
}
