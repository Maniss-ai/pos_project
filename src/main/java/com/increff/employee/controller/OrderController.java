package com.increff.employee.controller;

import com.increff.employee.dto.OrderDto;
import com.increff.employee.model.data.OrderData;
import com.increff.employee.model.data.OrderItemData;
import com.increff.employee.model.form.ViewOrderForm;
import com.increff.employee.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;

@Api
@RestController
public class OrderController {
    @Autowired
    private OrderDto dto;

    @ApiOperation(value = "Get Placed order")
    @RequestMapping(path = "/api/order/view-order", method = RequestMethod.POST)
    public List<OrderData> search(@RequestBody ViewOrderForm viewOrderForm) throws ApiException, ParseException {
        return dto.search(viewOrderForm);
    }

    @ApiOperation(value = "Generate Invoice for placed order")
    @RequestMapping(path = "/api/order/view-order/invoice/{orderId}", method = RequestMethod.GET)
    public void generateXmlString(HttpServletResponse response, @PathVariable Integer orderId) throws Exception {
        dto.generatePdfForOrder(response, orderId);
    }

    @ApiOperation(value = "Get order by Id")
    @RequestMapping(path = "/api/order/view-order/placed/{orderId}", method = RequestMethod.GET)
    public List<OrderItemData> getSingleOrder(@PathVariable Integer orderId) {
        return dto.getSingleOrder(orderId);
    }

}
