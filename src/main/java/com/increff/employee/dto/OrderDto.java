package com.increff.employee.dto;

import com.increff.employee.model.data.OrderData;
import com.increff.employee.model.form.OrderItemForm;
import com.increff.employee.model.form.ViewOrderForm;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.OrderService;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderDto {
    @Autowired
    private OrderService orderService;
    @Autowired
    private InventoryService inventoryService;

    public List<OrderData> search(ViewOrderForm form) throws ApiException, ParseException {
        List<OrderPojo> pojoList;
        List<OrderData> dataList = new ArrayList<>();

        if(form.getOrder_id() != 0) {
            checkOrderIdExists(form.getOrder_id());
            pojoList = orderService.getSelectedOrdersWithId(form.getOrder_id());
        }
        else if(form.getStart_date() != null && form.getEnd_date() != null && !form.getStart_date().isEmpty() && !form.getEnd_date().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(sdf.parse(form.getStart_date()).compareTo(sdf.parse(form.getEnd_date())) > 0) {
                throw new ApiException("Start Date should come before End Date");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate start_date = LocalDate.parse(form.getStart_date(), formatter);
            LocalDate end_date = LocalDate.parse(form.getEnd_date(), formatter);

            pojoList = orderService.getSelectedOrdersWithoutId(start_date, end_date);
        }
        else {
            throw new ApiException("Please provide either Order_Id or Date range");
        }

        for (OrderPojo pojo : pojoList) {
            dataList.add(DtoHelper.convertPojoToDataOrder(pojo));
        }
        return dataList;
    }

    private void checkOrderIdExists(int order_id) throws ApiException {
        List<OrderPojo> orderPojoList = orderService.getSelectedOrdersWithId(order_id);
        for(OrderPojo orderPojo : orderPojoList) {
            if(orderPojo.getOrder_id() == order_id) {
               return;
            }
        }

        throw new ApiException("Order Id doesn't exists");
    }

    public OrderPojo getOrder(int order_id) {
        return orderService.getOrder(order_id);
    }
}
