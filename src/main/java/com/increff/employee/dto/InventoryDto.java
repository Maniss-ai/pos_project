package com.increff.employee.dto;

import com.increff.employee.model.data.InventoryData;
import com.increff.employee.model.form.InventoryForm;
import com.increff.employee.model.form.InventoryUpdateForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.ProductService;
import com.increff.employee.util.Checks;
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
        Checks.nullCheckInventory(form);
        InventoryPojo pojo = DtoHelper.convertFormToPojoInventory(form);
        DtoHelper.normalizeInventory(pojo);
        productService.getInventoryBarcode(pojo);

        if (Checks.doesNotExistInventory(form, getAll())) {
            return DtoHelper.convertPojoToDataInventory(inventoryService.add(pojo));
        } else {
                InventoryUpdateForm inventoryUpdateForm = new InventoryUpdateForm();
                inventoryUpdateForm.setInventory(form.getInventory());
                return update(pojo.getBarcode(), inventoryUpdateForm);
        }
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<InventoryData> bulkAddInventory(List<InventoryForm> formList) throws ApiException {
        List<InventoryData> dataList = new ArrayList<>();
        StringBuilder error = new StringBuilder();
        Integer row = 1;

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

    public InventoryData get(Integer id) throws ApiException {
        InventoryPojo pojo = inventoryService.get(id);
        return DtoHelper.convertPojoToDataInventory(pojo);
    }

    public List<InventoryData> getAll() {
        List<InventoryPojo> pojoList = inventoryService.getAll();
        List<InventoryData> dataList = new ArrayList<>();
        for (InventoryPojo pojo : pojoList) {
            dataList.add(DtoHelper.convertPojoToDataInventory(pojo));
        }
        return dataList;
    }

    public InventoryData update(String barcode, InventoryUpdateForm form) throws ApiException {
        Checks.nullCheckForUpdateInventory(form);
        InventoryPojo pojo = DtoHelper.convertFormToPojoForUpdateInventory(form);
        return DtoHelper.convertPojoToDataInventory(inventoryService.update(barcode, pojo));
    }

}
