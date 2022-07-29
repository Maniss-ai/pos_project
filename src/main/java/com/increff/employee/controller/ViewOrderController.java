package com.increff.employee.controller;

import com.increff.employee.dto.ViewOrderDto;
import com.increff.employee.model.OrderData;
import com.increff.employee.model.ViewOrderForm;
import com.increff.employee.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
public class ViewOrderController {
    @Autowired
    private ViewOrderDto dto;

    @ApiOperation(value = "Adds Place order")
    @RequestMapping(path = "/api/order/view_order", method = RequestMethod.POST)
    public List<OrderData> search(@RequestBody ViewOrderForm form) throws ApiException {
        return dto.search(form);
    }
}
