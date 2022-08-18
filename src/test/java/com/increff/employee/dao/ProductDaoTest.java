package com.increff.employee.dao;

import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.AbstractUnitTest;
import com.increff.employee.service.ApiException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProductDaoTest extends AbstractUnitTest {
    @Autowired
    ProductDao productDao;
    @Autowired
    BrandDao brandDao;

    @Test
    public void testInsert() {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("puma");
        brandPojo.setCategory("shoes");
        brandDao.insert(brandPojo);

        ProductPojo productPojo = new ProductPojo();
        productPojo.setBrand("puma");
        productPojo.setCategory("shoes");
        productPojo.setBarcode("puma111");
        productPojo.setProduct("sports shoes");
        productPojo.setMrp(2999.362);
        productPojo = productDao.insert(productPojo);

        Assert.assertEquals("puma", productPojo.getBrand());
        Assert.assertEquals("shoes", productPojo.getCategory());
        Assert.assertEquals("puma111", productPojo.getBarcode());
        Assert.assertEquals("sports shoes", productPojo.getProduct());
        Assert.assertEquals(2999.36, productPojo.getMrp(), 0.01);
    }

    @Test
    public void testSelect() throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("puma");
        brandPojo.setCategory("shoes");
        brandDao.insert(brandPojo);

        ProductPojo productPojo = new ProductPojo();
        productPojo.setBrand("puma");
        productPojo.setCategory("shoes");
        productPojo.setBarcode("puma111");
        productPojo.setProduct("sports shoes");
        productPojo.setMrp(2999.362);
        productPojo = productDao.insert(productPojo);

        productPojo = productDao.select(productPojo.getId());

        Assert.assertEquals("puma", productPojo.getBrand());
        Assert.assertEquals("shoes", productPojo.getCategory());
        Assert.assertEquals("puma111", productPojo.getBarcode());
        Assert.assertEquals("sports shoes", productPojo.getProduct());
        Assert.assertEquals(2999.36, productPojo.getMrp(), 0.01);
    }

    @Test
    public void testSelectAll() {
        for(int i = 0; i < 5; i++) {
            BrandPojo brandPojo = new BrandPojo();
            brandPojo.setBrand("brand" + i+1);
            brandPojo.setCategory("category" + i+1);
            brandDao.insert(brandPojo);
        }

        for(int i = 0; i < 5; i++) {
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBrand("brand" + i+1);
            productPojo.setCategory("category" + i+1);
            productPojo.setBarcode("barcode" + i+1);
            productPojo.setProduct("product" + i+1);
            productPojo.setMrp(100.2432 * (i+1));
            productDao.insert(productPojo);
        }

        List<ProductPojo> productPojoList = productDao.selectAll();

        Assert.assertEquals(5, productPojoList.size());
    }
}
