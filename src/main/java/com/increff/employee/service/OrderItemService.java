package com.increff.employee.service;

import com.increff.employee.dao.PlaceOrderDao;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.OrderItemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class OrderItemService {
    @Autowired
    private PlaceOrderDao dao;

    @Transactional
    public OrderItemPojo add(OrderItemPojo p) {
        return dao.insert(p);
    }

    @Transactional
    public void delete(int id) {
        dao.delete(id);
    }

    @Transactional(rollbackOn = ApiException.class)
    public OrderItemPojo get(int id) throws ApiException {
        return dao.select(id);
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<OrderItemPojo> getSingleOrder(int order_id) {
        return dao.selectSingleOrder(order_id);
    }

    @Transactional
    public List<OrderItemPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(int place_order_id, OrderItemPojo p) throws ApiException {
        OrderItemPojo pojo = getCheck(place_order_id);
        pojo.setOrder_id(p.getOrder_id());
        pojo.setBarcode(p.getBarcode());
        pojo.setQuantity(p.getQuantity());
        pojo.setSelling_price(p.getSelling_price());
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void updateOrderId(String barcode, OrderItemPojo p) throws ApiException {
        System.out.println(barcode + " UPDATE ORDER ID SERVICE");
        OrderItemPojo pojo = getCheckOrderId(barcode);
        pojo.setOrder_id(p.getOrder_id());
        pojo.setBarcode(p.getBarcode());
        pojo.setQuantity(p.getQuantity());
        pojo.setSelling_price(p.getSelling_price());
    }

    public List<OrderItemPojo> getCheckWithBarcode(String barcode) throws ApiException {
        List<OrderItemPojo> orderItemPojoList = dao.select1(barcode);
        if (orderItemPojoList == null) {
            throw new ApiException("Product with barcode: " + barcode + " does not exit");
        }
        return orderItemPojoList;
    }

    @Transactional
    public OrderItemPojo getCheck(int place_order_id) throws ApiException {
        OrderItemPojo p = dao.select(place_order_id);
        if (p == null) {
            throw new ApiException("Product with id: " + place_order_id + " does not exit");
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

