package com.increff.employee.dto;

import com.increff.employee.model.data.BrandData;
import com.increff.employee.model.form.BrandForm;
import com.increff.employee.AbstractUnitTest;
import com.increff.employee.service.ApiException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class BrandDtoTest extends AbstractUnitTest {
    @Autowired
    BrandDto brandDto;

    @Test
    public void testAddNormalize() throws ApiException {
        BrandData brandData = brandDto.add(addBrandForm());

        Assert.assertEquals("brand", brandData.getBrand());
        Assert.assertEquals("category", brandData.getCategory());
    }

    @Test(expected = ApiException.class)
    public void testAddDuplicate() throws ApiException {
        BrandForm brandForm = addBrandForm();

        brandDto.add(brandForm);
        brandDto.add(brandForm);
    }

    @Test
    public void testBulkAddSize() throws ApiException {
        List<BrandForm> formList = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            BrandForm brandForm = new BrandForm();
            brandForm.setBrand("Brand" + i+1);
            brandForm.setCategory("Category" + i+1);
            formList.add(brandForm);
        }

        List<BrandData> brandDataList = brandDto.bulkAddBrand(formList);

        Assert.assertEquals(brandDataList.size(), 5);
    }

    @Test(expected = ApiException.class)
    public void testBulkAddRollback() throws ApiException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("Brand" + 5);
        brandForm.setCategory("Category" + 5);
        brandDto.add(brandForm);

        List<BrandForm> formList = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            brandForm.setBrand("Brand" + i+1);
            brandForm.setCategory("Category" + i+1);
            formList.add(brandForm);
        }

        List<BrandData> brandDataList = brandDto.bulkAddBrand(formList);

        Assert.assertEquals(0, brandDataList.size());
    }

    @Test(expected = ApiException.class)
    public void testGetWhenIdNotExists() throws ApiException {
        brandDto.get(0);
    }

    @Test
    public void testGetWhenIdExists() throws ApiException {
        BrandData brandData = brandDto.add(addBrandForm());
        int id = brandData.getId();
        brandData = brandDto.get(id);

        Assert.assertEquals("brand", brandData.getBrand());
        Assert.assertEquals("category", brandData.getCategory());
    }

    @Test
    public void testGetAll() throws ApiException {
        testBulkAddSize();
        List<BrandData> brandDataList = brandDto.getAll();
        Assert.assertEquals(brandDataList.size(), 5);
    }

    @Test
    public void testUpdate() throws ApiException {
        BrandForm brandForm = new BrandForm();
        BrandData brandData = brandDto.add(addBrandForm());

        int id = brandData.getId();
        brandForm.setBrand("   Adidas ");
        brandForm.setCategory("   ShiRtS   ");

        brandData = brandDto.update(id, brandForm);

        Assert.assertEquals("adidas", brandData.getBrand());
        Assert.assertEquals("shirts", brandData.getCategory());
    }

    @Test(expected = ApiException.class)
    public void testUpdateDuplicate() throws ApiException {
        testAddNormalize();

        BrandForm brandForm = new BrandForm();
        BrandData brandData = brandDto.add(addBrandForm());

        int id = brandData.getId();
        brandForm.setBrand("   PUMA ");
        brandForm.setCategory("   ShoeS   ");

        brandDto.update(id, brandForm);
    }

    @Test(expected = ApiException.class)
    public void testUpdateIdNotExist() throws ApiException {
        BrandForm brandForm = new BrandForm();

        brandForm.setBrand("   Adidas ");
        brandForm.setCategory("   ShiRtS   ");

        brandDto.update(0, brandForm);
    }

}
