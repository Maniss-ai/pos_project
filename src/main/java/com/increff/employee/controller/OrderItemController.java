package com.increff.employee.controller;

import com.increff.employee.dto.OrderItemDto;
import com.increff.employee.model.data.OrderItemData;
import com.increff.employee.model.form.OrderItemForm;
import com.increff.employee.model.form.OrderItemUpdateForm;
import com.increff.employee.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api
@RestController
public class OrderItemController extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
    @Autowired
    private OrderItemDto dto;

    @ApiOperation(value = "Add order item")
    @RequestMapping(path = "/api/order/order-item", method = RequestMethod.POST)
    public OrderItemData add(@RequestBody OrderItemForm orderItemForm) throws ApiException {
        return dto.add(orderItemForm);
    }

    @ApiOperation(value = "Submit (place) order")
    @RequestMapping(path = "/api/order/submit-order", method = RequestMethod.POST)
    public void submit(@RequestBody List<OrderItemForm> orderFormList) throws ApiException {
        dto.submit(orderFormList);
    }

    @ApiOperation(value = "Get order item by Id")
    @RequestMapping(path = "/api/order/order-item/{id}", method = RequestMethod.GET)
    public OrderItemData get(@PathVariable Integer id) throws ApiException {
        return dto.get(id);
    }

    @ApiOperation(value = "Get list of all order items")
    @RequestMapping(path = "/api/order/order-item", method = RequestMethod.GET)
    public List<OrderItemData> getAll() {
        return dto.getAll();
    }

    @ApiOperation(value = "Update order items")
    @RequestMapping(path = "/api/order/order-item/{orderItemId}", method = RequestMethod.PUT)
    public OrderItemData update(@PathVariable Integer orderItemId, @RequestBody OrderItemUpdateForm form) throws ApiException {
        return dto.update(orderItemId, form);
    }
}
