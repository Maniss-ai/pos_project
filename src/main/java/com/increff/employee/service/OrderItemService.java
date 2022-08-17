package com.increff.employee.service;

import com.increff.employee.dao.OrderItemDao;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.OrderItemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class OrderItemService {
    @Autowired
    private OrderItemDao dao;

    @Transactional
    public OrderItemPojo add(OrderItemPojo p) {
        return dao.insert(p);
    }

    @Transactional(rollbackOn = ApiException.class)
    public OrderItemPojo get(Integer id) throws ApiException {
        return dao.select(id);
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<OrderItemPojo> getSingleOrder(Integer orderId) {
        return dao.selectSingleOrder(orderId);
    }

    @Transactional
    public List<OrderItemPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(Integer placeOrderId, OrderItemPojo p) throws ApiException {
        OrderItemPojo pojo = getCheck(placeOrderId);
        pojo.setOrderId(p.getOrderId());
        pojo.setBarcode(p.getBarcode());
        pojo.setQuantity(p.getQuantity());
        pojo.setSellingPrice(p.getSellingPrice());
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void updateOrderId(String barcode, OrderItemPojo p) throws ApiException {
        OrderItemPojo pojo = getCheckOrderId(barcode);
        pojo.setOrderId(p.getOrderId());
        pojo.setBarcode(p.getBarcode());
        pojo.setQuantity(p.getQuantity());
        pojo.setSellingPrice(p.getSellingPrice());
    }

    public List<OrderItemPojo> getCheckWithBarcode(String barcode) throws ApiException {
        List<OrderItemPojo> orderItemPojoList = dao.select1(barcode);
        if (orderItemPojoList == null) {
            throw new ApiException("Product with barcode: " + barcode + " does not exit");
        }
        return orderItemPojoList;
    }

    @Transactional
    public OrderItemPojo getCheck(Integer placeOrderId) throws ApiException {
        OrderItemPojo p = dao.select(placeOrderId);
        if (p == null) {
            throw new ApiException("Product with id: " + placeOrderId + " does not exit");
        }
        return p;
    }

    @Transactional
    public OrderItemPojo getCheckOrderId(String barcode) throws ApiException {
        OrderItemPojo p = dao.selectOrderId(barcode);
        if (p == null) {
            throw new ApiException("Product with barcode: " + barcode + " does not exit");
        }
        return p;
    }

    @Transactional
    public void submit(OrderPojo orderPojo) {
        dao.insertOrder(orderPojo);
    }

}

