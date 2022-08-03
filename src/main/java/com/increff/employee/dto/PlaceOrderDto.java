package com.increff.employee.dto;

import com.increff.employee.model.data.PlaceOrderData;
import com.increff.employee.model.form.PlaceOrderForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.PlaceOrderPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.PlaceOrderService;
import com.increff.employee.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PlaceOrderDto {
    @Autowired
    private PlaceOrderService placeOrderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryService inventoryService;

    public PlaceOrderDto() {

    }

    // CRUD ....
    public PlaceOrderData add(PlaceOrderForm form) throws ApiException {
        nullCheck(form);
        PlaceOrderPojo pojo = convertFormToPojo(form, 0);
        normalize(pojo);

        if(form.getQuantity() == 0 || form.getSelling_price() == 0) {
            throw new ApiException("Quantity or Selling price can't be zero/empty ");
        }

        // get inventory_id using barcode_id match ....
        int inventory_id = getInventoryIdMatchWithBarcode(pojo);
        int inventory_items = inventoryService.get(inventory_id).getInventory();

        // check if there exists enough products in inventory to place order ....
        if(inventory_items < form.getQuantity()) {
            throw new ApiException("There's only " + inventory_items + " items available in the Inventory ...");
        }
        else if(!isSellingPriceLessThanMRP(pojo)) {
            throw new ApiException("Selling Price Can't be More Than MRP ...");
        }
        else {
            if(alreadyAdded(form)) {
                throw new ApiException("Order already exists, Please Update ...");
            }
            else {
                return convertPojoToData(placeOrderService.add(pojo));
            }
        }
    }


    private boolean alreadyAdded(PlaceOrderForm form) {
        List<PlaceOrderPojo> list = placeOrderService.getAll();
        for(PlaceOrderPojo placeOrderPojo : list) {
            if(Objects.equals(placeOrderPojo.getBarcode(), form.getBarcode()) && 0 == placeOrderPojo.getOrder_id()) {
                return true;
            }
        }
        return false;
    }

    public void delete(int id) throws ApiException {
        placeOrderService.delete(id);
    }

    public PlaceOrderData get(int id) throws ApiException {
        PlaceOrderPojo pojo = placeOrderService.get(id);
        return convertPojoToData(pojo);
    }

    public List<PlaceOrderData> getSingleOrder(int order_id) {
        List<PlaceOrderPojo> pojoList = placeOrderService.getSingleOrder(order_id);
        List<PlaceOrderData> dataList = new ArrayList<>();
        for(PlaceOrderPojo pojo : pojoList) {
            dataList.add(convertPojoToData(pojo));
        }
        return dataList;
    }

    public List<PlaceOrderData> getAll() {
        List<PlaceOrderPojo> pojoList = placeOrderService.getAll();
        List<PlaceOrderData> dataList = new ArrayList<>();
        for (PlaceOrderPojo pojo : pojoList) {
            // if order is not placed than display it ....
            if(pojo.getOrder_id() == 0) {
                dataList.add(convertPojoToData(pojo));
            }
        }
        return dataList;
    }

    @Transactional
    public void submit(List<PlaceOrderForm> orderFormList) throws ApiException {
        if(orderFormList.isEmpty()) {
            throw new ApiException("Please add product to place an order");
        }
        OrderPojo orderPojo = new OrderPojo();

        orderPojo.setTime(LocalDate.now());

        int total_bill_amount = 0;
        for(PlaceOrderForm form : orderFormList) {
            total_bill_amount += (form.getQuantity() * form.getSelling_price());
        }

        orderPojo.setBill_amount(total_bill_amount);

        placeOrderService.submit(orderPojo);
        int order_id = orderPojo.getOrder_id();

        // add order_id and update items ....
        for (PlaceOrderForm form : orderFormList) {
//            form.setOrder_id(order_id);
            PlaceOrderPojo placeOrderPojo = convertFormToPojo(form, order_id);
            placeOrderService.updateOrderId(placeOrderPojo.getBarcode(), placeOrderPojo);

            // update inventory ....
            int current_inventory = inventoryService.getCheck(placeOrderPojo.getBarcode()).getInventory();
            current_inventory -= placeOrderPojo.getQuantity();

            InventoryPojo inventoryPojo = new InventoryPojo();
            inventoryPojo.setInventory(current_inventory);
            inventoryPojo.setBarcode(placeOrderPojo.getBarcode());
            inventoryService.update(placeOrderPojo.getBarcode(), inventoryPojo);
        }
    }

    public void update(int place_order_id, PlaceOrderForm form) throws ApiException {
        nullCheck(form);
        PlaceOrderPojo pojo = convertFormToPojo(form, 0);

        // get inventory_id using barcode_id match ....
        int inventory_id = getInventoryIdMatchWithBarcode(pojo);
        int inventory_items = inventoryService.get(inventory_id).getInventory();

        System.out.println("UPDATE WORKING");
        if(inventory_items < form.getQuantity()) {
            throw new ApiException("There's only " + inventory_items + " items available in the Inventory ...");
        }
        else if(!isSellingPriceLessThanMRP(pojo)) {
            throw new ApiException("Selling Price Can't be More Than MRP ...");
        }
        else {
            placeOrderService.update(place_order_id, pojo);
        }
    }

    // CHECKS ....

    private void nullCheck(PlaceOrderForm form) throws ApiException {
        if(form.getBarcode().isEmpty() || form.getBarcode() == null || form.getQuantity() == 0 || form.getSelling_price() == 0) {
            throw new ApiException("Order info can't be empty");
        }
    }

    boolean isUnique(PlaceOrderPojo pojo) {
        List<PlaceOrderData> dataList = getAll();
        for(PlaceOrderData placeOrderData : dataList) {
            if(Objects.equals(placeOrderData.getBarcode(), pojo.getBarcode())) {
                return false;
            }
        }
        return true;
    }

    boolean isSellingPriceLessThanMRP(PlaceOrderPojo pojo) throws ApiException {
        ProductPojo productPojo = productService.get(pojo.getBarcode());

        if(pojo.getSelling_price() > productPojo.getMrp()) {
            return false;
        }
        else {
            pojo.setBarcode(productService.get(pojo.getBarcode()).getBarcode());
            return true;
        }
    }
    // MODIFYING ....
    public static void normalize(PlaceOrderPojo p) {
        p.setBarcode(p.getBarcode().toLowerCase().trim());
    }


    // CONVERSION ....
    protected static PlaceOrderData convertPojoToData(PlaceOrderPojo pojo) {
        PlaceOrderData data = new PlaceOrderData();
        data.setOrder_id(pojo.getOrder_id());
        data.setBarcode(pojo.getBarcode());
        data.setQuantity(pojo.getQuantity());
        data.setSelling_price(pojo.getSelling_price());
        data.setId(pojo.getId());
        return data;
    }

    protected static PlaceOrderPojo convertFormToPojo(PlaceOrderForm form, int order_id) {
        PlaceOrderPojo pojo = new PlaceOrderPojo();
//        pojo.setOrder_id(form.getOrder_id());
        pojo.setOrder_id(order_id);
        pojo.setBarcode(form.getBarcode());
        pojo.setQuantity(form.getQuantity());
        pojo.setSelling_price(form.getSelling_price());
        return pojo;
    }

    private int getInventoryIdMatchWithBarcode(PlaceOrderPojo pojo) throws ApiException {
        int inventory_id = -1;
        List<InventoryPojo> list = inventoryService.getAll();
        for(InventoryPojo inventoryPojo : list) {
            if(Objects.equals(inventoryPojo.getBarcode(), pojo.getBarcode())) {
                inventory_id = inventoryPojo.getId();
                break;
            }
        }
        if(inventory_id == -1) {
            throw new ApiException("Inventory with barcode: " + pojo.getBarcode() + " doesn't exists ....");
        }
        else {
            return inventory_id;
        }
    }

    private int getProductIdMatchWithBarcode(PlaceOrderPojo pojo) throws ApiException {
        int product_id = -1;
        List<ProductPojo> list = productService.getAll();
        for(ProductPojo productPojo : list) {
            if(Objects.equals(productPojo.getBarcode(), pojo.getBarcode())) {
                product_id = productPojo.getId();
                break;
            }
        }
        if(product_id == -1) {
            throw new ApiException("Inventory with barcode: " + get(product_id).getBarcode() + " doesn't exists ....");
        }
        else {
            return product_id;
        }
    }
}
