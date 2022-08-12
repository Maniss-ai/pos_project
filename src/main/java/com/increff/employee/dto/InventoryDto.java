package com.increff.employee.dto;

import com.increff.employee.model.data.InventoryData;
import com.increff.employee.model.form.InventoryForm;
import com.increff.employee.model.form.InventoryUpdateForm;
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
        InventoryPojo pojo = DtoHelper.convertFormToPojoInventory(form);
        normalize(pojo);
        boolean barcode_exists = isBarcodeExists(pojo);

        if (doesNotExist(form) && barcode_exists) {
            return DtoHelper.convertPojoToDataInventory(inventoryService.add(pojo));
        } else {
            if (!barcode_exists) {
                throw new ApiException("Barcode doesn't exists  ");
            } else {
                InventoryUpdateForm inventoryUpdateForm = new InventoryUpdateForm();
                inventoryUpdateForm.setInventory(form.getInventory());
                return update(pojo.getBarcode(), inventoryUpdateForm);
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
        return DtoHelper.convertPojoToDataInventory(pojo);
    }

    public List<InventoryData> getAll() {
        List<InventoryPojo> pojoList = inventoryService.getAll();
        List<InventoryData> dataList = new ArrayList<>();
        for (InventoryPojo pojo : pojoList) {
            dataList.add(DtoHelper.convertPojoToDataInventory(pojo));
            System.out.println(pojo.getBarcode());
        }
        return dataList;
    }

    public InventoryData update(String barcode, InventoryUpdateForm form) throws ApiException {
        nullCheckForUpdate(form);
        InventoryPojo pojo = DtoHelper.convertFormToPojoForUpdateInventory(form);
        return DtoHelper.convertPojoToDataInventory(inventoryService.update(barcode, pojo));
    }

    // CHECKS ....

    private void nullCheck(InventoryForm form) throws ApiException {
        if(form.getBarcode().isEmpty() || form.getBarcode() == null) {
            throw new ApiException("Barcode can't be empty");
        }
        else if(form.getInventory() == 0) {
            throw new ApiException("Inventory can't be empty");
        }
    }

    private void nullCheckForUpdate(InventoryUpdateForm form) throws ApiException {
        if(form.getInventory() == 0) {
            throw new ApiException("Inventory can't be empty");
        }
    }


    // MODIFYING ....
    public static void normalize(InventoryPojo p) {
        p.setBarcode(p.getBarcode().toLowerCase().trim());
    }

}
