package com.increff.employee.dto;

import com.increff.employee.model.data.OrderData;
import com.increff.employee.model.form.PlaceOrderForm;
import com.increff.employee.model.form.ViewOrderForm;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.PlaceOrderPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.ViewOrderService;
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
public class ViewOrderDto {
    @Autowired
    private ViewOrderService viewOrderService;
    @Autowired
    private InventoryService inventoryService;

    public List<OrderData> search(ViewOrderForm form) throws ApiException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(sdf.parse(form.getStart_date()).compareTo(sdf.parse(form.getEnd_date())) > 0) {
            throw new ApiException("Start Date should come before End Date");
        }

        if(form.getStart_date() != null && form.getEnd_date() != null && !form.getStart_date().isEmpty() && !form.getEnd_date().isEmpty()) {
            List<OrderPojo> pojoList;
            List<OrderData> dataList = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate start_date = LocalDate.parse(form.getStart_date(), formatter);
            LocalDate end_date = LocalDate.parse(form.getEnd_date(), formatter);

            if(form.getOrder_id() == 0) {
                pojoList = viewOrderService.getSelectedOrdersWithoutId(start_date, end_date);
            }
            else {
                pojoList = viewOrderService.getSelectedOrdersWithId(start_date, end_date, form.getOrder_id());
            }

            for (OrderPojo pojo : pojoList) {
                dataList.add(convertPojoToData(pojo));
            }

            return dataList;
        }
        else {
            throw new ApiException("Date can't be empty");
        }
    }

    protected static OrderData convertPojoToData(OrderPojo pojo) {
        OrderData data = new OrderData();
        data.setId(pojo.getOrder_id());
        data.setTime(pojo.getTime().toString());
        data.setBill_amount(Precision.round(pojo.getBill_amount(), 2));
        return data;
    }

    protected static PlaceOrderPojo convertFormToPojo(PlaceOrderForm form) {
        PlaceOrderPojo pojo = new PlaceOrderPojo();
//        pojo.setOrder_id(form.getOrder_id());
        pojo.setBarcode(form.getBarcode());
        pojo.setQuantity(form.getQuantity());
        pojo.setSelling_price(Precision.round(form.getSelling_price(), 2));
        return pojo;
    }

    public OrderPojo getOrder(int order_id) {
        return viewOrderService.getOrder(order_id);
    }
}
