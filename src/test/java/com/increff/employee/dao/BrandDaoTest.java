package com.increff.employee.dao;

import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.AbstractUnitTest;
import com.increff.employee.service.ApiException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BrandDaoTest extends AbstractUnitTest {
    @Autowired
    BrandDao brandDao;

    @Test
    public void testInsert() {
        BrandPojo brandPojo = brandDao.insert(addBrandPojo());

        Assert.assertEquals("brand", brandPojo.getBrand());
        Assert.assertEquals("category", brandPojo.getCategory());
    }

    @Test
    public void testSelect() {
        BrandPojo brandPojo = brandDao.insert(addBrandPojo());

        int id = brandPojo.getId();
        brandPojo = brandDao.select(id);

        Assert.assertEquals("brand", brandPojo.getBrand());
        Assert.assertEquals("category", brandPojo.getCategory());
    }

    @Test
    public void testSelectAll() {
        for(int i = 0; i < 5; i++) {
            BrandPojo brandPojo = new BrandPojo();
            brandPojo.setBrand("brand" + i+1);
            brandPojo.setCategory("category" + i+1);
            brandDao.insert(brandPojo);
        }

        List<BrandPojo> brandPojoList = brandDao.selectAll();

        Assert.assertEquals(5, brandPojoList.size());
    }

    @Test
    public void testGetBrand() {
        brandDao.getBrand("puma");
    }

    @Test
    public void testGetCategory() {
        brandDao.getCategory("shoes");
    }

    @Test
    public void testGetBrandCategory() {
        brandDao.insert(addBrandPojo());
        brandDao.getBrandCategory("brand", "category");
    }

}
