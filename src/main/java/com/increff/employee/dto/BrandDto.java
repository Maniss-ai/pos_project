package com.increff.employee.dto;

import com.increff.employee.model.data.BrandData;
import com.increff.employee.model.form.BrandForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.service.ProductService;
import com.increff.employee.util.Checks;
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
        Checks.nullCheckBrand(form);
        BrandPojo pojo = DtoHelper.convertFormToPojoBrand(form);
        DtoHelper.normalizeBrand(pojo);
        if(Checks.isUniqueBrand(pojo, getAll())) {
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
        Integer row = 1;

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

    // TODO
    private boolean idExistsInProductPojo(Integer id) {
        return false;
    }

    public BrandData get(Integer id) throws ApiException {
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

    public BrandData update(Integer id, BrandForm form) throws ApiException {
        Checks.nullCheckBrand(form);
        BrandPojo pojo = DtoHelper.convertFormToPojoBrand(form);
        DtoHelper.normalizeBrand(pojo);
        try {
            brandService.getCheck(id);
        }
        catch (Exception e) {
            throw new ApiException("Unable to update, Id doesn't exists");
        }

        if(Checks.isUniqueBrand(id, pojo, getAll())) {
            return DtoHelper.convertPojoToDataBrand(brandService.update(id, pojo));
        }
        else {
            throw new ApiException("Unable to update, Brand Category pair already exists");
        }
    }

}
