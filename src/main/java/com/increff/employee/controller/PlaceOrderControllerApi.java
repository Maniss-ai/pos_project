package com.increff.employee.controller;

import com.increff.employee.dto.PlaceOrderDto;
import com.increff.employee.dto.ViewOrderDto;
import com.increff.employee.generatepdf.ObjectToXml;
import com.increff.employee.model.data.PlaceOrderData;
import com.increff.employee.model.form.PlaceOrderForm;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.service.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.util.List;

@Api
@RestController
public class PlaceOrderControllerApi extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
    @Autowired
    private PlaceOrderDto dto;
    @Autowired
    private ViewOrderDto viewOrderDto;

    @ApiOperation(value = "Adds Place order")
    @RequestMapping(path = "/api/order/place_order", method = RequestMethod.POST)
    public PlaceOrderData add(@RequestBody PlaceOrderForm form) throws ApiException {
        return dto.add(form);
    }

    @ApiOperation(value = "Submit Place order")
    @RequestMapping(path = "/api/order/submit_order", method = RequestMethod.POST)
    public void submit(@RequestBody List<PlaceOrderForm> orderFormList) throws ApiException {
        dto.submit(orderFormList);
    }


    @ApiOperation(value = "Deletes Place order")
    @RequestMapping(path = "/api/order/place_order/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id) throws ApiException {
        dto.delete(id);
    }

    @ApiOperation(value = "Gets Place order by ID")
    @RequestMapping(path = "/api/order/place_order/{id}", method = RequestMethod.GET)
    public PlaceOrderData get(@PathVariable int id) throws ApiException {
        return dto.get(id);
    }

    @ApiOperation(value = "Gets Place order by ID")
    @RequestMapping(path = "/api/order/place_order/placed/{order_id}", method = RequestMethod.GET)
    public List<PlaceOrderData> getSingleOrder(@PathVariable int order_id) throws ApiException {
        return dto.getSingleOrder(order_id);
    }

    @ApiOperation(value = "Gets list of all Place order")
    @RequestMapping(path = "/api/order/place_order", method = RequestMethod.GET)
    public List<PlaceOrderData> getAllFromPage() {
        return dto.getAll();
    }

    @ApiOperation(value = "Updates Place order")
    @RequestMapping(path = "/api/order/place_order/{place_order_id}", method = RequestMethod.PUT)
    public void update(@PathVariable int place_order_id, @RequestBody PlaceOrderForm form) throws ApiException {
        dto.update(place_order_id, form);
    }

    @ApiOperation(value = "Generate XML String")
    @RequestMapping(path = "/api/order/place_order/invoice/{order_id}", method = RequestMethod.GET)
    public void generateXmlString(HttpServletResponse response, @PathVariable int order_id) throws Exception {
        List<PlaceOrderData> placeOrderDataList = getSingleOrder(order_id);
        OrderPojo orderPojo = viewOrderDto.getOrder(order_id);
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
}
