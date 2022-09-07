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
import java.time.ZonedDateTime;
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
        OrderItemPojo orderItemPojo = DtoHelper.convertFormToPojoOrderItem(form, 0);
        DtoHelper.normalizeOrderItem(form);
        orderItemPojo.setProductId(productService.getWithBarcode(form.getBarcode()).getId());
        Checks.checkLength(form);
        Checks.isMrpNegative(orderItemPojo.getSellingPrice());
        Checks.isInventoryNegative(orderItemPojo.getQuantity());

        // get inventoryId using barcodeId match ....
        Integer inventoryId = getInventoryIdMatchWithBarcode(form.getBarcode());
        Integer inventoryItems = inventoryService.get(inventoryId).getInventory();

        // check if there exists enough products in inventory to place order ....
        if(inventoryItems < form.getQuantity()) {
            throw new ApiException("There's only " + inventoryItems + " items available in the Inventory");
        }
        else if(!isSellingPriceLessThanMRP(orderItemPojo)) {
            throw new ApiException("Selling Price Can't be More Than MRP");
        }
        else {
            if(alreadyAdded(form)) {
                throw new ApiException("Order item already exists, Please Update");
            }
            else {
                OrderItemData orderItemData = DtoHelper.convertPojoToDataOrderItem(orderItemService.add(orderItemPojo));
                orderItemData.setBarcode(form.getBarcode());
                return orderItemData;
            }
        }
    }

    public OrderItemData get(Integer orderItemId) throws ApiException {
        OrderItemPojo pojo = orderItemService.get(orderItemId);
        OrderItemData orderItemData = DtoHelper.convertPojoToDataOrderItem(pojo);
        orderItemData.setBarcode(productService.getWithId(orderItemService.get(orderItemId).getProductId()).getBarcode());
        return orderItemData;
    }

    public List<OrderItemData> getAll() throws ApiException {
        List<OrderItemPojo> pojoList = orderItemService.getAll();
        List<OrderItemData> dataList = new ArrayList<>();
        for (OrderItemPojo pojo : pojoList) {
            // if order is not placed than display it ....
            if(pojo.getOrderId() == 0) {
                OrderItemData orderItemData = DtoHelper.convertPojoToDataOrderItem(pojo);
                orderItemData.setBarcode(productService.getWithId(pojo.getProductId()).getBarcode());
                dataList.add(orderItemData);
            }
        }
        return dataList;
    }

    @Transactional
    public void submit(List<OrderItemForm> orderFormList) throws ApiException {
        if(orderFormList.isEmpty()) {
            throw new ApiException("add order item to place an order");
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

        orderPojo.setTime(ZonedDateTime.now());

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
            orderItemPojo.setProductId(productService.getWithBarcode(form.getBarcode()).getId());

            orderItemService.updateOrderId(orderItemPojo.getProductId(), orderItemPojo);

            // update inventory ....
            Integer currentInventory = inventoryService.getCheck(orderItemPojo.getProductId()).getInventory();
            currentInventory -= orderItemPojo.getQuantity();

            InventoryPojo inventoryPojo = new InventoryPojo();
            inventoryPojo.setInventory(currentInventory);
            inventoryService.update(orderItemPojo.getProductId(), inventoryPojo);
        }
    }

    public OrderItemData update(Integer orderItemId, OrderItemUpdateForm form) throws ApiException {
        Checks.nullCheckForUpdateOrderItem(form);
        String barcode = get(orderItemId).getBarcode();
        OrderItemPojo orderItemPojo = DtoHelper.convertFormToPojoForUpdateOrderItem(form, 0);
        orderItemPojo.setProductId(orderItemService.get(orderItemId).getProductId());

        Checks.isMrpNegative(orderItemPojo.getSellingPrice());
        Checks.isInventoryNegative(orderItemPojo.getQuantity());

        // get inventoryId using barcodeId match ....
        Integer inventoryId = getInventoryIdMatchWithBarcode(barcode);
        Integer inventoryItems = inventoryService.get(inventoryId).getInventory();

        if(inventoryItems < form.getQuantity()) {
            throw new ApiException("There's only " + inventoryItems + " items available in the Inventory");
        }
        else if(!isSellingPriceLessThanMRP(orderItemPojo)) {
            throw new ApiException("Selling Price can't be more than MRP");
        }
        else {
            OrderItemData orderItemData = DtoHelper.convertPojoToDataOrderItem(orderItemService.update(orderItemId, orderItemPojo));
            orderItemData.setBarcode(productService.getWithId(orderItemService.get(orderItemId).getProductId()).getBarcode());
            return orderItemData;
        }
    }

    private boolean alreadyAdded(OrderItemForm form) throws ApiException {
        List<OrderItemPojo> list = orderItemService.getAll();
        for(OrderItemPojo orderItemPojo : list) {
            if(Objects.equals(productService.getWithId(orderItemPojo.getProductId()).getBarcode(), form.getBarcode()) && 0 == orderItemPojo.getOrderId()) {
                return true;
            }
        }
        return false;
    }

    private boolean isSellingPriceLessThanMRP(OrderItemPojo orderItemPojo) throws ApiException {
        ProductPojo productPojo = productService.getWithId(orderItemPojo.getProductId());

        if(orderItemPojo.getSellingPrice() > productPojo.getMrp()) {
            return false;
        }
        else {
//             TODO
//            orderItemPojo.setBarcode(productService.getWithBarcode(orderItemPojo.getBarcode()).getBarcode());
            return true;
        }
    }

    private Integer getInventoryIdMatchWithBarcode(String barcode) throws ApiException {
        Integer inventoryId = -1;
        List<InventoryPojo> list = inventoryService.getAll();
        for(InventoryPojo inventoryPojo : list) {
            if(Objects.equals(productService.getWithId(inventoryPojo.getId()).getBarcode(), barcode)) {
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
