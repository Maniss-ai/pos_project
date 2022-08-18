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
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("adidas");
        brandPojo.setCategory("pants");

        brandPojo = brandDao.insert(brandPojo);

        Assert.assertEquals("adidas", brandPojo.getBrand());
        Assert.assertEquals("pants", brandPojo.getCategory());
    }

    @Test
    public void testSelectWhenIdExists() throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("adidas");
        brandPojo.setCategory("pants");

        brandPojo = brandDao.insert(brandPojo);

        int id = brandPojo.getId();
        brandPojo = brandDao.select(id);

        Assert.assertEquals("adidas", brandPojo.getBrand());
        Assert.assertEquals("pants", brandPojo.getCategory());
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

}
