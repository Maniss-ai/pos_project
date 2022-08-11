package com.increff.employee.service.dto_test;

import com.increff.employee.model.data.BrandData;
import com.increff.employee.model.form.BrandForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.service.AbstractUnitTest;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class BrandServiceTest extends AbstractUnitTest {
    @Autowired
    BrandService brandService;

    @Test
    public void testAdd() {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("puma");
        brandPojo.setCategory("shoes");

        brandPojo = brandService.add(brandPojo);

        Assert.assertEquals("puma", brandPojo.getBrand());
        Assert.assertEquals("shoes", brandPojo.getCategory());
    }

    @Test
    public void testGetWhenIdExists() throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("puma");
        brandPojo.setCategory("shoes");

        brandPojo = brandService.add(brandPojo);

        brandPojo = brandService.get(brandPojo.getId());

        Assert.assertEquals("puma", brandPojo.getBrand());
        Assert.assertEquals("shoes", brandPojo.getCategory());
    }

    @Test(expected = ApiException.class)
    public void testGetWhenIdNotExists() throws ApiException {
        int id = 0;
        brandService.get(id);
    }

    @Test
    public void testGetAll() {
        for(int i = 0; i < 5; i++) {
            BrandPojo brandPojo = new BrandPojo();
            brandPojo.setBrand("Brand" + i+1);
            brandPojo.setCategory("Category" + i+1);

            brandService.add(brandPojo);
        }

        Assert.assertEquals(5, brandService.getAll().size());
    }

    @Test
    public void testUpdate() throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("puma");
        brandPojo.setCategory("shoes");
        brandPojo = brandService.add(brandPojo);

        int id = brandPojo.getId();
        brandPojo.setBrand("adidas");
        brandPojo.setCategory("shirts");

        brandPojo = brandService.update(id, brandPojo);

        Assert.assertEquals("adidas", brandPojo.getBrand());
        Assert.assertEquals("shirts", brandPojo.getCategory());
    }

}
