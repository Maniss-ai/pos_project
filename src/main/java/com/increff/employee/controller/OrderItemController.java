package com.increff.employee.controller;

import com.increff.employee.dto.OrderItemDto;
import com.increff.employee.dto.OrderDto;
import com.increff.employee.model.data.OrderItemData;
import com.increff.employee.model.form.OrderItemForm;
import com.increff.employee.model.form.OrderItemUpdateForm;
import com.increff.employee.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api
@RestController
public class OrderItemController extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
    @Autowired
    private OrderItemDto dto;
    @Autowired
    private OrderDto orderDto;

    @ApiOperation(value = "Add order item")
    @RequestMapping(path = "/api/order/place-order", method = RequestMethod.POST)
    public OrderItemData add(@RequestBody OrderItemForm orderItemForm) throws ApiException {
        return dto.add(orderItemForm);
    }

    @ApiOperation(value = "Submit (place) order")
    @RequestMapping(path = "/api/order/submit-order", method = RequestMethod.POST)
    public void submit(@RequestBody List<OrderItemForm> orderFormList) throws ApiException {
        dto.submit(orderFormList);
    }


    @ApiOperation(value = "Delete order item")
    @RequestMapping(path = "/api/order/place-order/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) throws ApiException {
        dto.delete(id);
    }

    @ApiOperation(value = "Get order item by Id")
    @RequestMapping(path = "/api/order/place-order/{id}", method = RequestMethod.GET)
    public OrderItemData get(@PathVariable int id) throws ApiException {
        return dto.get(id);
    }

    @ApiOperation(value = "Get order by Id")
    @RequestMapping(path = "/api/order/place-order/placed/{orderId}", method = RequestMethod.GET)
    public List<OrderItemData> getSingleOrder(@PathVariable int orderId) {
        return dto.getSingleOrder(orderId);
    }

    @ApiOperation(value = "Get list of all order items")
    @RequestMapping(path = "/api/order/place-order", method = RequestMethod.GET)
    public List<OrderItemData> getAll() {
        return dto.getAll();
    }

    @ApiOperation(value = "Updates order items")
    @RequestMapping(path = "/api/order/place-order/{placeOrderId}", method = RequestMethod.PUT)
    public void update(@PathVariable int placeOrderId, @RequestBody OrderItemUpdateForm form) throws ApiException {
        dto.update(placeOrderId, form);
    }

    @ApiOperation(value = "Generate Invoice for order")
    @RequestMapping(path = "/api/order/place-order/invoice/{orderId}", method = RequestMethod.GET)
    public void generateXmlString(HttpServletResponse response, @PathVariable int orderId) throws Exception {
        dto.generatePdfForOrder(response, orderId);
     }
}
