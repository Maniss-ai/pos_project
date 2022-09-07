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
        try {
            return dao.select(id);
        }
        catch (Exception e) {
            throw new ApiException("Product id does not exit");
        }
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<OrderItemPojo> getSingleOrder(Integer orderId) throws ApiException {
        List<OrderItemPojo> orderItemPojoList = dao.selectSingleOrder(orderId);;
        if(orderItemPojoList.size() == 0) {
            throw new ApiException("Order Id doesn't not exists");
        }
        return orderItemPojoList;
    }

    @Transactional
    public List<OrderItemPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public OrderItemPojo update(Integer placeOrderId, OrderItemPojo p) throws ApiException {
        OrderItemPojo pojo = getCheck(placeOrderId);
        pojo.setOrderId(p.getOrderId());
//        pojo.setBarcode(p.getBarcode());
        pojo.setQuantity(p.getQuantity());
        pojo.setSellingPrice(p.getSellingPrice());

        return pojo;
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void updateOrderId(Integer productId, OrderItemPojo p) throws ApiException {
        OrderItemPojo pojo = getCheckOrderId(productId);
        pojo.setOrderId(p.getOrderId());
//        pojo.setBarcode(p.getBarcode());
        pojo.setQuantity(p.getQuantity());
        pojo.setSellingPrice(p.getSellingPrice());
    }

    @Transactional
    public List<OrderItemPojo> getWithProductId(Integer productId) {
        return dao.selectWithProductId(productId);
    }

    @Transactional
    public OrderItemPojo getCheck(Integer orderItemId) throws ApiException {
        OrderItemPojo p;
        try {
            p = dao.select(orderItemId);
        }
        catch (Exception e) {
            throw new ApiException("Product id does not exit");
        }
        return p;
    }

    @Transactional
    public OrderItemPojo getCheckOrderId(Integer productId) throws ApiException {
        OrderItemPojo p;
        try {
            p = dao.selectOrderId(productId);
        }
        catch (Exception e) {
            throw new ApiException("Product with does not exit");
        }

        return p;
    }

    @Transactional
    public void submit(OrderPojo orderPojo) {
        dao.insertOrder(orderPojo);
    }

}

