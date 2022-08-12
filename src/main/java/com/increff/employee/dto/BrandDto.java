package com.increff.employee.dto;

import com.increff.employee.model.data.BrandData;
import com.increff.employee.model.form.BrandForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BrandDto {

    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductService productService;


    @Transactional
    // CRUD....
    public BrandData add(BrandForm form) throws ApiException {
        nullCheck(form);
        BrandPojo pojo = DtoHelper.convertFormToPojoBrand(form);
        normalize(pojo);
        if(isUnique(pojo)) {
            return DtoHelper.convertPojoToDataBrand(brandService.add(pojo));
        }
        else {
            throw new ApiException("Brand Category pair already exists");
        }
    }


    @Transactional(rollbackOn = ApiException.class)
    public List<BrandData> bulkAddBrand(List<BrandForm> formList) throws ApiException {
        List<BrandData> dataList = new ArrayList<>();
        StringBuilder error = new StringBuilder();
        int row = 1;

        for(BrandForm brandForm : formList) {
            try {
                dataList.add(add(brandForm));
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

    public void delete(int id) throws ApiException {
        try {
            brandService.getCheck(id);
        }
        catch (Exception e) {
            throw new ApiException("Unable to delete, ID doesn't exists");
        }

        if(!idExistsInProductPojo(id)) {
            brandService.delete(id);
        }
        else {
            throw new ApiException("Unable to delete, Id exists in product table");
        }
    }

    // TODO
    private boolean idExistsInProductPojo(int id) {
        return false;
    }

    public BrandData get(int id) throws ApiException {
        BrandPojo pojo = brandService.get(id);
        return DtoHelper.convertPojoToDataBrand(pojo);
    }

    public List<BrandData> getAll() {
        List<BrandPojo> pojoList = brandService.getAll();
        List<BrandData> dataList = new ArrayList<>();
        for (BrandPojo pojo : pojoList) {
            dataList.add(DtoHelper.convertPojoToDataBrand(pojo));
        }
        return dataList;
    }

    public BrandData update(int id, BrandForm form) throws ApiException {
        nullCheck(form);
        BrandPojo pojo = DtoHelper.convertFormToPojoBrand(form);
        normalize(pojo);
        try {
            brandService.getCheck(id);
        }
        catch (Exception e) {
            throw new ApiException("Unable to update, Id doesn't exists");
        }

        if(isUnique(id, pojo)) {
            return DtoHelper.convertPojoToDataBrand(brandService.update(id, pojo));
        }
        else {
            throw new ApiException("Unable to update, Brand Category pair already exists");
        }
    }

    // CHECKS ....
    public boolean isUnique(BrandPojo pojo) {
        List<BrandData> dataList = getAll();

        for(BrandData pojoItem : dataList) {
            if(
                    Objects.equals(pojoItem.getBrand(), pojo.getBrand()) &&
                    Objects.equals(pojoItem.getCategory(), pojo.getCategory())
            ) {
                return false;
            }
        }

        return true;
    }

    public boolean isUnique(int current_id, BrandPojo pojo) {
        List<BrandData> dataList = getAll();

        for(BrandData pojoItem : dataList) {
            if(
                    Objects.equals(pojoItem.getBrand(), pojo.getBrand()) &&
                            Objects.equals(pojoItem.getCategory(), pojo.getCategory()) &&
                            pojoItem.getId() != current_id
            ) {
                return false;
            }
        }

        return true;
    }

    // MODIFYING ....
    public static void normalize(BrandPojo p) {
        p.setBrand(p.getBrand().toLowerCase().trim());
        p.setCategory(p.getCategory().toLowerCase().trim());
    }

    private void nullCheck(BrandForm form) throws ApiException {
        if(form.getBrand() == null || form.getBrand().isEmpty()) {
            throw new ApiException("Brand can't be null");
        }
        else if(form.getCategory() == null || form.getCategory().isEmpty()) {
            throw new ApiException("Category can't be null");
        }
    }
}
