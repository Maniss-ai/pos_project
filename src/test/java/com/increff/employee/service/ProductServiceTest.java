package com.increff.employee.service;

import com.increff.employee.model.form.ReportForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.AbstractUnitTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProductServiceTest extends AbstractUnitTest {
    @Autowired
    ProductService productService;
    @Autowired
    BrandService brandService;

    @Test
    public void testAdd() {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("puma");
        brandPojo.setCategory("shoes");
        brandService.add(brandPojo);

        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("puma111");
        productPojo.setProduct("sports shoes");
        productPojo.setMrp(2999.362);

        productService.add(productPojo);
    }

    @Test
    public void testGetWithId() throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("puma");
        brandPojo.setCategory("shoes");
        brandService.add(brandPojo);

        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("puma111");
        productPojo.setProduct("sports shoes");
        productPojo.setMrp(2999.362);
        productPojo = productService.add(productPojo);

        int id = productPojo.getId();
        productPojo = productService.getWithId(id);

        Assert.assertEquals("puma111", productPojo.getBarcode());
        Assert.assertEquals("sports shoes", productPojo.getProduct());
        Assert.assertEquals(2999.36, productPojo.getMrp(), 0.01);
    }

    @Test
    public void testGetWithBarcode() throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("puma");
        brandPojo.setCategory("shoes");
        brandService.add(brandPojo);

        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("puma111");
        productPojo.setProduct("sports shoes");
        productPojo.setMrp(2999.362);
        productPojo = productService.add(productPojo);

        String barcode = productPojo.getBarcode();
        productPojo = productService.getWithBarcode(barcode);

        Assert.assertEquals("puma111", productPojo.getBarcode());
        Assert.assertEquals("sports shoes", productPojo.getProduct());
        Assert.assertEquals(2999.36, productPojo.getMrp(), 0.01);
    }

    @Test
    public void testGetAll() {
        for(int i = 0; i < 5; i++) {
            BrandPojo brandPojo = new BrandPojo();
            brandPojo.setBrand("brand" + i+1);
            brandPojo.setCategory("category" + i+1);
            brandService.add(brandPojo);
        }

        for(int i = 0; i < 5; i++) {
            ProductPojo productPojo = new ProductPojo();
            productPojo.setBarcode("barcode" + i+1);
            productPojo.setProduct("products" + i+1);
            productPojo.setMrp(100.234 * (i+1));
            productService.add(productPojo);
        }

        List<ProductPojo> productPojoList = productService.getAll();

        Assert.assertEquals(5, productPojoList.size());
    }

    @Test
    public void testUpdate() throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("puma");
        brandPojo.setCategory("shoes");
        brandService.add(brandPojo);

        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("puma111");
        productPojo.setProduct("sports shoes");
        productPojo.setMrp(2999.362);
        productService.add(productPojo);

        productPojo.setBarcode("puma222");
        productPojo.setProduct("neon shoes");
        productPojo.setMrp(8999.362);
        productPojo = productService.add(productPojo);

        productPojo.setBarcode("puma333");

        int id = productPojo.getId();
        productPojo = productService.update(id, productPojo);

        Assert.assertEquals("puma333", productPojo.getBarcode());
    }

    @Test
    public void testGetProductWithBrandCategory() {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("puma");
        brandPojo.setCategory("shoes");
        Integer id = brandService.add(brandPojo).getId();

        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("puma111");
        productPojo.setProduct("sports shoes");
        productPojo.setMrp(2999.362);
        productService.add(productPojo);

        productService.getWithBrandCategory(id);
    }

    @Test
    public void testGetProductWithCategory() {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("puma");
        brandPojo.setCategory("shoes");
        brandService.add(brandPojo);

        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("puma111");
        productPojo.setProduct("sports shoes");
        productPojo.setMrp(2999.362);
        productService.add(productPojo);

        ReportForm reportForm = new ReportForm();
        reportForm.setCategory("shoes");

    }

    @Test(expected = ApiException.class)
    public void getCheckWhenIdNotExist() throws ApiException {
        int id = 0;
        productService.getCheck(id);
    }

    @Test
    public void getCheckWhenIdExist() throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("puma");
        brandPojo.setCategory("shoes");
        brandService.add(brandPojo);

        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode("puma111");
        productPojo.setProduct("sports shoes");
        productPojo.setMrp(2999.362);
        productPojo = productService.add(productPojo);

        int id = productPojo.getId();
        productPojo = productService.getCheck(id);

        Assert.assertEquals("puma111", productPojo.getBarcode());
    }

    @Test
    public void testGetCheckInventoryBarcode() throws ApiException {
        productService.add(addProductPojo());
        productService.getInventoryBarcode("barcode");
    }

    @Test(expected = ApiException.class)
    public void testGetCheckInventoryBarcodeException() throws ApiException {
        productService.getInventoryBarcode("barcode");
    }

    @Test(expected = ApiException.class)
    public void testGetWhenBarcodeNotExist() throws ApiException {
        productService.getWithBarcode("barcode");
    }

}
