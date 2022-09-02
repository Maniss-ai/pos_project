package com.increff.employee.dto;

import com.increff.employee.model.data.OrderData;
import com.increff.employee.model.data.OrderItemData;
import com.increff.employee.model.form.ViewOrderForm;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.OrderItemService;
import com.increff.employee.service.OrderService;
import com.increff.employee.service.ProductService;
import com.increff.employee.util.ObjectToXml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.ZonedDateTime;
import java.time.zone.ZoneRulesException;
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

    public List<OrderData> search(ViewOrderForm form) throws ApiException {
        List<OrderPojo> pojoList;
        List<OrderData> dataList = new ArrayList<>();

        if(form.getOrderId() != null && form.getOrderId() != 0) {
            checkOrderIdExists(form.getOrderId());
            pojoList = orderService.getSelectedOrdersWithId(form.getOrderId());
        }
        else if(form.getStartDate() != null && form.getEndDate() != null && !form.getStartDate().isEmpty() && !form.getEndDate().isEmpty()) {
            ZonedDateTime startDate, endDate;
            try {
                startDate = ZonedDateTime.parse(form.getStartDate());
                endDate = ZonedDateTime.parse(form.getEndDate());
            }
            catch (Exception e) {
                throw new ApiException("Invalid Date time format must be zone date time");
            }

            if(startDate.isAfter(endDate)) {
                throw new ApiException("Start Date cannot be greater then End Date");
            }
            if(startDate.isAfter(ZonedDateTime.now())) {
                throw new ApiException("Start Date cannot be greater then Today");
            }

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
