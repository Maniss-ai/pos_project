package com.increff.employee.dto;

import com.increff.employee.model.data.InventoryData;
import com.increff.employee.model.form.InventoryForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class InventoryDto {
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ProductService productService;

    // CRUD ....
    public InventoryData add(InventoryForm form) throws ApiException {
        nullCheck(form);
        InventoryPojo pojo = convertFormToPojo(form);
        normalize(pojo);
        boolean barcode_exists = isBarcodeExists(pojo);
        if (doesNotExist(form) && barcode_exists) {
            return convertPojoToData(inventoryService.add(pojo));
        } else {
            if (!barcode_exists) {
                throw new ApiException("Barcode doesn't exists ....");
            } else {
                throw new ApiException("Inventory already exists, Please Update ....");
            }
        }
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<InventoryData> bulkAddInventory(List<InventoryForm> formList) throws ApiException {
        List<InventoryData> dataList = new ArrayList<>();
        StringBuilder error = new StringBuilder();
        int row = 1;

        for(InventoryForm inventoryForm : formList) {
            try {
                dataList.add(add(inventoryForm));
            }
            catch (Exception e) {
                error.append(row).append(": ").append(e.getMessage()).append("\n");
            }

            row++;
        }

        if(!error.toString().isEmpty()) {
            throw new ApiException(error.toString());
        }

        System.out.println("WORKING FINE JAVA CODE::::");

        return dataList;
    }

    private boolean isBarcodeExists(InventoryPojo pojo) throws ApiException {
        return productService.getInventoryBarcode(pojo) != null;
    }

    private boolean doesNotExist(InventoryForm form) {
        List<InventoryPojo> list = inventoryService.getAll();
        for(InventoryPojo inventoryPojo : list) {
            if(Objects.equals(inventoryPojo.getBarcode(), form.getBarcode())) {
                return false;
            }
        }
        return true;
    }

    public void delete(int id) {
        inventoryService.delete(id);
    }

    public InventoryData get(int id) throws ApiException {
        InventoryPojo pojo = inventoryService.get(id);
        return convertPojoToData(pojo);
    }

    public List<InventoryData> getAll() {
        List<InventoryPojo> pojoList = inventoryService.getAll();
        List<InventoryData> dataList = new ArrayList<>();
        for (InventoryPojo pojo : pojoList) {
            dataList.add(convertPojoToData(pojo));
        }
        return dataList;
    }

    public void update(String barcode, InventoryForm form) throws ApiException {
        nullCheck(form);
        InventoryPojo pojo = convertFormToPojo(form);
        inventoryService.update(barcode, pojo);
    }

    // CHECKS ....

    private void nullCheck(InventoryForm form) throws ApiException {
        if(form.getInventory() == 0 || form.getBarcode().isEmpty() || form.getBarcode() == null) {
            throw new ApiException("Inventory Info can't be empty or null");
        }
    }

    // MODIFYING ....
    public static void normalize(InventoryPojo p) {
        p.setBarcode(p.getBarcode().toLowerCase().trim());
    }

    // CONVERSION ....
    protected static InventoryData convertPojoToData(InventoryPojo pojo) {
        InventoryData data = new InventoryData();
        data.setBarcode(pojo.getBarcode());
        data.setInventory(pojo.getInventory());
        data.setId(pojo.getId());
        return data;
    }

    protected static InventoryPojo convertFormToPojo(InventoryForm form) {
        InventoryPojo pojo = new InventoryPojo();
        pojo.setBarcode(form.getBarcode());
        pojo.setInventory(form.getInventory());
        return pojo;
    }
}
