package com.increff.employee.service;

import com.increff.employee.dao.PlaceOrderDao;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.PlaceOrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PlaceOrderService {
    @Autowired
    private PlaceOrderDao dao;

    @Transactional
    public PlaceOrderPojo add(PlaceOrderPojo p) {
        return dao.insert(p);
    }

    @Transactional
    public void delete(int id) {
        dao.delete(id);
    }

    @Transactional(rollbackOn = ApiException.class)
    public PlaceOrderPojo get(int id) throws ApiException {
        return dao.select(id);
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<PlaceOrderPojo> getSingleOrder(int order_id) {
        return dao.selectSingleOrder(order_id);
    }

    @Transactional
    public List<PlaceOrderPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(int place_order_id, PlaceOrderPojo p) throws ApiException {
        PlaceOrderPojo pojo = getCheck(place_order_id);
        pojo.setOrder_id(p.getOrder_id());
        pojo.setBarcode(p.getBarcode());
        pojo.setQuantity(p.getQuantity());
        pojo.setSelling_price(p.getSelling_price());
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void updateOrderId(String barcode, PlaceOrderPojo p) throws ApiException {
        System.out.println(barcode + " UPDATE ORDER ID SERVICE");
        PlaceOrderPojo pojo = getCheckOrderId(barcode);
        pojo.setOrder_id(p.getOrder_id());
        pojo.setBarcode(p.getBarcode());
        pojo.setQuantity(p.getQuantity());
        pojo.setSelling_price(p.getSelling_price());
    }

    public List<PlaceOrderPojo> getCheckWithBarcode(String barcode) throws ApiException {
        List<PlaceOrderPojo> placeOrderPojoList = dao.select1(barcode);
        if (placeOrderPojoList == null) {
            throw new ApiException("Product with barcode: " + barcode + " does not exit");
        }
        return placeOrderPojoList;
    }

    @Transactional
    public PlaceOrderPojo getCheck(int place_order_id) throws ApiException {
        PlaceOrderPojo p = dao.select(place_order_id);
        if (p == null) {
            throw new ApiException("Product with id: " + place_order_id + " does not exit");
        }
        return p;
    }

    @Transactional
    public PlaceOrderPojo getCheckOrderId(String barcode) throws ApiException {
        PlaceOrderPojo p = dao.selectOrderId(barcode);
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

