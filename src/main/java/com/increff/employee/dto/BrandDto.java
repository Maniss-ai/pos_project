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
        BrandPojo pojo = convertFormToPojo(form);
        normalize(pojo);
        if(isUnique(pojo) && isNotEmpty(pojo)) {
            return convertPojoToData(brandService.add(pojo));
        }
        else {
            if(!isUnique(pojo))
                throw new ApiException("Brand Category already exists...");
            else
                throw new ApiException("Brand or Category can't be empty ...");
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
                System.out.println("Working Catch");
                error.append(row).append(": ").append(e.getMessage()).append("\n");
            }

            row++;
        }

        if(!error.toString().isEmpty()) {
            System.out.println("Working IF");
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
        return convertPojoToData(pojo);
    }

    public List<BrandData> getAll() {
        List<BrandPojo> pojoList = brandService.getAll();
        List<BrandData> dataList = new ArrayList<>();
        for (BrandPojo pojo : pojoList) {
            dataList.add(convertPojoToData(pojo));
        }
        return dataList;
    }

    public BrandData update(int id, BrandForm form) throws ApiException {
        nullCheck(form);
        BrandPojo pojo = convertFormToPojo(form);
        normalize(pojo);
        try {
            brandService.getCheck(id);
        }
        catch (Exception e) {
            throw new ApiException("Unable to update, Id doesn't exists");
        }

        if(isUnique(id, pojo)) {
            return convertPojoToData(brandService.update(id, pojo));
        }
        else {
            throw new ApiException("Unable to update, Brand already exists");
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

    public boolean isNotEmpty(BrandPojo pojo) {
        return !pojo.getBrand().isEmpty() && !pojo.getCategory().isEmpty();
    }

    // MODIFYING ....
    public static void normalize(BrandPojo p) {
        p.setBrand(p.getBrand().toLowerCase().trim());
        p.setCategory(p.getCategory().toLowerCase().trim());
    }

    private void nullCheck(BrandForm form) throws ApiException {
        if(form.getBrand() == null || form.getCategory() == null) {
            throw new ApiException("Brand and Category can't be null");
        }
    }

    // CONVERSION ....
    protected static BrandData convertPojoToData(BrandPojo pojo) {
        BrandData data = new BrandData();
        data.setCategory(pojo.getCategory());
        data.setBrand(pojo.getBrand());
        data.setId(pojo.getId());
        return data;
    }

    protected static BrandPojo convertFormToPojo(BrandForm form) {
        BrandPojo pojo = new BrandPojo();
        pojo.setCategory(form.getCategory());
        pojo.setBrand(form.getBrand());
        return pojo;
    }

}
