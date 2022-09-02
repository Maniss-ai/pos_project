package com.increff.employee.service;

import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.AbstractUnitTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BrandServiceTest extends AbstractUnitTest {
    @Autowired
    BrandService brandService;

    @Test
    public void testAdd() {
        BrandPojo brandPojo = brandService.add(addBrandPojo());

        Assert.assertEquals("brand", brandPojo.getBrand());
        Assert.assertEquals("category", brandPojo.getCategory());
    }

    @Test
    public void testGetWhenIdExists() throws ApiException {
        BrandPojo brandPojo = brandService.add(addBrandPojo());
        brandPojo = brandService.get(brandPojo.getId());

        Assert.assertEquals("brand", brandPojo.getBrand());
        Assert.assertEquals("category", brandPojo.getCategory());
    }

    @Test(expected = ApiException.class)
    public void testGetWhenIdNotExists() throws ApiException {
        brandService.get(0);
    }

    @Test
    public void testGetAll() {
        for(int i = 0; i < 5; i++) {
            brandService.add(addBrandPojo());
        }

        Assert.assertEquals(5, brandService.getAll().size());
    }

    @Test
    public void testUpdate() throws ApiException {
        BrandPojo brandPojo = brandService.add(addBrandPojo());

        int id = brandPojo.getId();
        brandPojo.setBrand("puma");
        brandPojo.setCategory("shoes");
        brandPojo = brandService.update(id, brandPojo);

        Assert.assertEquals("puma", brandPojo.getBrand());
        Assert.assertEquals("shoes", brandPojo.getCategory());
    }

    @Test
    public void testGetBrand() throws ApiException {
        brandService.add(addBrandPojo());

        Assert.assertEquals("brand", brandService.getBrand("brand").get(0).getBrand());
        Assert.assertEquals("category", brandService.getBrand("brand").get(0).getCategory());
    }

    @Test
    public void testGetCategory() throws ApiException {
        brandService.add(addBrandPojo());

        Assert.assertEquals("brand", brandService.getCategory("category").get(0).getBrand());
        Assert.assertEquals("category", brandService.getCategory("category").get(0).getCategory());
    }
    @Test
    public void testGetBrandCategory() throws ApiException {
        brandService.add(addBrandPojo());

        Assert.assertEquals("brand", brandService.getBrandCategory("brand", "category").getBrand());
        Assert.assertEquals("category", brandService.getBrandCategory("brand", "category").getCategory());
    }

    @Test(expected = Exception.class)
    public void testGetBrandException() throws ApiException {
        brandService.getBrand("value");
    }

    @Test(expected = Exception.class)
    public void testGetCategoryException() throws ApiException {
        brandService.getCategory("value");
    }
    @Test(expected = Exception.class)
    public void testGetBrandCategoryException() throws ApiException {
        brandService.getBrandCategory("brand", "category");
    }


}
