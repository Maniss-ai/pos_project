package com.increff.employee.dto;

import com.increff.employee.model.data.OrderItemData;
import com.increff.employee.model.form.OrderItemForm;
import com.increff.employee.model.form.OrderItemUpdateForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.OrderItemService;
import com.increff.employee.service.ProductService;
import com.increff.employee.util.Checks;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class OrderItemDto {
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private OrderDto orderDto;

    public OrderItemDto() {

    }

    public OrderItemData add(OrderItemForm form) throws ApiException {
        Checks.nullCheckOrderItem(form);
        OrderItemPojo pojo = DtoHelper.convertFormToPojoOrderItem(form, 0);
        DtoHelper.normalizeOrderItem(pojo);

        // get inventoryId using barcodeId match ....
        Integer inventoryId = getInventoryIdMatchWithBarcode(form.getBarcode());
        Integer inventoryItems = inventoryService.get(inventoryId).getInventory();

        // check if there exists enough products in inventory to place order ....
        if(inventoryItems < form.getQuantity()) {
            throw new ApiException("There's only " + inventoryItems + " items available in the Inventory");
        }
        else if(!isSellingPriceLessThanMRP(pojo)) {
            throw new ApiException("Selling Price Can't be More Than MRP");
        }
        else {
            if(alreadyAdded(form)) {
                throw new ApiException("Order already exists, Please Update");
            }
            else {
                return DtoHelper.convertPojoToDataOrderItem(orderItemService.add(pojo));
            }
        }
    }

    public OrderItemData get(Integer id) throws ApiException {
        OrderItemPojo pojo = orderItemService.get(id);
        return DtoHelper.convertPojoToDataOrderItem(pojo);
    }

    public List<OrderItemData> getAll() {
        List<OrderItemPojo> pojoList = orderItemService.getAll();
        List<OrderItemData> dataList = new ArrayList<>();
        for (OrderItemPojo pojo : pojoList) {
            // if order is not placed than display it ....
            if(pojo.getOrderId() == 0) {
                dataList.add(DtoHelper.convertPojoToDataOrderItem(pojo));
            }
        }
        return dataList;
    }

    @Transactional
    public void submit(List<OrderItemForm> orderFormList) throws ApiException {
        if(orderFormList.isEmpty()) {
            throw new ApiException("Please add product to place an order");
        }

        for(OrderItemForm orderItemForm : orderFormList) {
            // get inventoryId using barcodeId match ....
            Integer inventoryId = getInventoryIdMatchWithBarcode(orderItemForm.getBarcode());
            Integer inventoryItems = inventoryService.get(inventoryId).getInventory();

            // check if there exists enough products in inventory to place order ....
            if(inventoryItems < orderItemForm.getQuantity()) {
                throw new ApiException("For Barcode " + orderItemForm.getBarcode() +"\nThere are only " + inventoryItems + " items available in the Inventory");
            }
        }

        OrderPojo orderPojo = new OrderPojo();

        orderPojo.setTime(LocalDate.now());

        double totalBillAmount = 0.0;
        for(OrderItemForm form : orderFormList) {
            totalBillAmount += (form.getQuantity() * form.getSellingPrice());
        }

        orderPojo.setBillAmount(Precision.round(totalBillAmount, 2));

        orderItemService.submit(orderPojo);
        Integer orderId = orderPojo.getOrderId();

        // add orderId and update items ....
        for (OrderItemForm form : orderFormList) {
//            form.setOrderId(orderId);
            OrderItemPojo orderItemPojo = DtoHelper.convertFormToPojoOrderItem(form, orderId);
            orderItemService.updateOrderId(orderItemPojo.getBarcode(), orderItemPojo);

            // update inventory ....
            Integer currentInventory = inventoryService.getCheck(orderItemPojo.getBarcode()).getInventory();
            currentInventory -= orderItemPojo.getQuantity();

            InventoryPojo inventoryPojo = new InventoryPojo();
            inventoryPojo.setInventory(currentInventory);
            inventoryPojo.setBarcode(orderItemPojo.getBarcode());
            inventoryService.update(orderItemPojo.getBarcode(), inventoryPojo);
        }
    }

    public OrderItemData update(Integer orderItemId, OrderItemUpdateForm form) throws ApiException {
        Checks.nullCheckForUpdateOrderItem(form);
        String barcode = get(orderItemId).getBarcode();
        OrderItemPojo pojo = DtoHelper.convertFormToPojoForUpdateOrderItem(form, 0, barcode);

        // get inventoryId using barcodeId match ....
        Integer inventoryId = getInventoryIdMatchWithBarcode(barcode);
        Integer inventoryItems = inventoryService.get(inventoryId).getInventory();

        if(inventoryItems < form.getQuantity()) {
            throw new ApiException("There's only " + inventoryItems + " items available in the Inventory");
        }
        else if(!isSellingPriceLessThanMRP(pojo)) {
            throw new ApiException("Selling Price can't be more than MRP");
        }
        else {
            return DtoHelper.convertPojoToDataOrderItem(orderItemService.update(orderItemId, pojo));
        }
    }

    private boolean alreadyAdded(OrderItemForm form) {
        List<OrderItemPojo> list = orderItemService.getAll();
        for(OrderItemPojo orderItemPojo : list) {
            if(Objects.equals(orderItemPojo.getBarcode(), form.getBarcode()) && 0 == orderItemPojo.getOrderId()) {
                return true;
            }
        }
        return false;
    }

    private boolean isSellingPriceLessThanMRP(OrderItemPojo pojo) throws ApiException {
        ProductPojo productPojo = productService.getWithBarcode(pojo.getBarcode());

        if(pojo.getSellingPrice() > productPojo.getMrp()) {
            return false;
        }
        else {
            pojo.setBarcode(productService.getWithBarcode(pojo.getBarcode()).getBarcode());
            return true;
        }
    }

    private Integer getInventoryIdMatchWithBarcode(String barcode) throws ApiException {
        Integer inventoryId = -1;
        List<InventoryPojo> list = inventoryService.getAll();
        for(InventoryPojo inventoryPojo : list) {
            if(Objects.equals(inventoryPojo.getBarcode(), barcode)) {
                inventoryId = inventoryPojo.getId();
                break;
            }
        }

        if(inventoryId == -1) {
            throw new ApiException("Inventory with barcode: " + barcode + " doesn't exists");
        }
        else {
            return inventoryId;
        }
    }
}
