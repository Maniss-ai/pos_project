package com.increff.employee.dto;

import com.increff.employee.service.*;
import com.increff.employee.util.ObjectToXml;
import com.increff.employee.model.data.OrderData;
import com.increff.employee.model.data.OrderItemData;
import com.increff.employee.model.form.ViewOrderForm;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class OrderDto {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderItemService orderItemService;

    public List<OrderData> search(ViewOrderForm form) throws ApiException, ParseException {
        List<OrderPojo> pojoList;
        List<OrderData> dataList = new ArrayList<>();

        if(form.getOrderId() != null && form.getOrderId() != 0) {
            checkOrderIdExists(form.getOrderId());
            pojoList = orderService.getSelectedOrdersWithId(form.getOrderId());
        }
        else if(form.getStartDate() != null && form.getEndDate() != null && !form.getStartDate().isEmpty() && !form.getEndDate().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(sdf.parse(form.getStartDate()).compareTo(sdf.parse(form.getEndDate())) > 0) {
                throw new ApiException("Start Date should come before End Date");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(form.getStartDate(), formatter);
            LocalDate endDate = LocalDate.parse(form.getEndDate(), formatter);

            pojoList = orderService.getSelectedOrdersWithoutId(startDate, endDate);
        }
        else {
            throw new ApiException("Please provide either OrderId or Date range");
        }

        for (OrderPojo pojo : pojoList) {
            dataList.add(DtoHelper.convertPojoToDataOrder(pojo));
        }
        return dataList;
    }

    public List<OrderItemData> getSingleOrder(Integer orderId) throws ApiException {
        List<OrderItemPojo> pojoList = orderItemService.getSingleOrder(orderId);
        List<OrderItemData> dataList = new ArrayList<>();
        for(OrderItemPojo pojo : pojoList) {
            OrderItemData orderItemData = DtoHelper.convertPojoToDataOrderItem(pojo);
            orderItemData.setBarcode(productService.getWithId(pojo.getProductId()).getBarcode());

            dataList.add(orderItemData);
        }
        return dataList;
    }

    public void generatePdfForOrder(HttpServletResponse response, Integer orderId) throws Exception {
        List<OrderItemData> placeOrderDataList = getSingleOrder(orderId);
        OrderPojo orderPojo = getOrder(orderId);
        ObjectToXml.generateXmlString(placeOrderDataList, orderPojo);

        File file = new File("src/main/resources/pdf/invoice.pdf");

        if (file.exists()) {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));
            response.setContentLength((int) file.length());
            InputStream inputStream = new BufferedInputStream(Files.newInputStream(file.toPath()));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        }
    }
    public OrderPojo getOrder(Integer orderId) throws ApiException {
        return orderService.getOrder(orderId);
    }

    private void checkOrderIdExists(Integer orderId) throws ApiException {
        List<OrderPojo> orderPojoList = orderService.getSelectedOrdersWithId(orderId);
        for(OrderPojo orderPojo : orderPojoList) {
            if(Objects.equals(orderPojo.getOrderId(), orderId)) {
               return;
            }
        }

        throw new ApiException("Order Id doesn't exists");
    }
}
