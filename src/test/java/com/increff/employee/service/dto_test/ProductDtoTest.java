package com.increff.employee.service.dto_test;

import com.increff.employee.dto.BrandDto;
import com.increff.employee.dto.ProductDto;
import com.increff.employee.model.data.BrandData;
import com.increff.employee.model.data.ProductData;
import com.increff.employee.model.form.BrandForm;
import com.increff.employee.model.form.ProductForm;
import com.increff.employee.model.form.ProductUpdateForm;
import com.increff.employee.service.AbstractUnitTest;
import com.increff.employee.service.ApiException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ProductDtoTest extends AbstractUnitTest {
    @Autowired
    ProductDto productDto;
    @Autowired
    BrandDto brandDto;

    @Test(expected = ApiException.class)
    public void testAddDuplicate() throws ApiException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("PuMa    ");
        brandForm.setCategory("    SHOES    ");
        brandDto.add(brandForm);

        ProductForm productForm = new ProductForm();
        productForm.setBrand("    PUma");
        productForm.setCategory("    shOES   ");
        productForm.setBarcode("    PUmA111   ");
        productForm.setProduct("    SportS ShoES   ");
        productForm.setMrp(2999.362);

        productDto.add(productForm);
        productDto.add(productForm);
    }

    @Test
    public void testAddNormalize() throws ApiException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("PuMa    ");
        brandForm.setCategory("    SHOES    ");

        brandDto.add(brandForm);

        ProductForm productForm = new ProductForm();
        productForm.setBrand("    PUma");
        productForm.setCategory("    shOES   ");
        productForm.setBarcode("    PUmA111   ");
        productForm.setProduct("    SportS ShoES   ");
        productForm.setMrp(2999.362);

        ProductData productData = productDto.add(productForm);

        Assert.assertEquals("puma", productData.getBrand());
        Assert.assertEquals("shoes", productData.getCategory());
        Assert.assertEquals("puma111", productData.getBarcode());
        Assert.assertEquals("sports shoes", productData.getProduct());
        Assert.assertEquals(2999.36, productData.getMrp(), 0.01);
    }

    @Test(expected = ApiException.class)
    public void testAddBrandCategoryPairNotExists() throws ApiException {
        ProductForm productForm = new ProductForm();
        productForm.setBrand("    PUma");
        productForm.setCategory("    shOES   ");
        productForm.setBarcode("    PUmA111   ");
        productForm.setProduct("    SportS ShoES   ");
        productForm.setMrp(2999.362);

        productDto.add(productForm);
    }

    @Test
    public void testBulkAddSize() throws ApiException {
        List<BrandForm> brandFormList = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            BrandForm brandForm = new BrandForm();
            brandForm.setBrand("Brand" + i+1);
            brandForm.setCategory("Category" + i+1);
            brandFormList.add(brandForm);
        }
        brandDto.bulkAddBrand(brandFormList);

        List<ProductForm> productFormList = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            ProductForm productForm = new ProductForm();
            productForm.setBrand("Brand" + i+1);
            productForm.setCategory("Category" + i+1);
            productForm.setBarcode("Barcode" + i+1);
            productForm.setProduct("Product" + i+1);
            productForm.setMrp((i+1) * 100.23);
            productFormList.add(productForm);
        }
        List<ProductData> productDataList = productDto.bulkAddProduct(productFormList);

        Assert.assertEquals(5, productDataList.size());
    }

    @Test(expected = ApiException.class)
    public void testBulkAddRollback() throws ApiException {
        List<BrandForm> brandFormList = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            BrandForm brandForm = new BrandForm();
            brandForm.setBrand("Brand" + i+1);
            brandForm.setCategory("Category" + i+1);
            brandFormList.add(brandForm);
        }
        brandDto.bulkAddBrand(brandFormList);

        // Add Duplicate Product
        ProductForm productForm = new ProductForm();
        productForm.setBrand("Brand" + 5);
        productForm.setCategory("Category" + 5);
        productForm.setBarcode("Barcode" + 5);
        productForm.setProduct("Product" + 5);
        productForm.setMrp((5) * 100.23);
        productDto.add(productForm);

        List<ProductForm> productFormList = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            productForm.setBrand("Brand" + i+1);
            productForm.setCategory("Category" + i+1);
            productForm.setBarcode("Barcode" + i+1);
            productForm.setProduct("Product" + i+1);
            productForm.setMrp((i+1) * 100.23);
            productFormList.add(productForm);
        }
        List<ProductData> productDataList = productDto.bulkAddProduct(productFormList);

        Assert.assertEquals(0, productDataList.size());
    }

    @Test
    public void testGetWithIdWhenIdExists() throws ApiException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("PuMa    ");
        brandForm.setCategory("    SHOES    ");
        brandDto.add(brandForm);

        ProductForm productForm = new ProductForm();
        productForm.setBrand("    PUma");
        productForm.setCategory("    shOES   ");
        productForm.setBarcode("    PUmA111   ");
        productForm.setProduct("    SportS ShoES   ");
        productForm.setMrp(2999.362);

        ProductData productData = productDto.add(productForm);

        int id = productData.getId();
        productData = productDto.getWithId(id);

        Assert.assertEquals("puma", productData.getBrand());
        Assert.assertEquals("shoes", productData.getCategory());
        Assert.assertEquals("puma111", productData.getBarcode());
        Assert.assertEquals("sports shoes", productData.getProduct());
        Assert.assertEquals(2999.36, productData.getMrp(), 0.01);
    }

    @Test(expected = ApiException.class)
    public void testGetWithIdWhenIdNotExists() throws ApiException {
        int id = 0;
        productDto.getWithId(id);
    }

    @Test
    public void testGetWithBarcode() throws ApiException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("PuMa    ");
        brandForm.setCategory("    SHOES    ");
        brandDto.add(brandForm);

        ProductForm productForm = new ProductForm();
        productForm.setBrand("    PUma");
        productForm.setCategory("    shOES   ");
        productForm.setBarcode("    PUmA111   ");
        productForm.setProduct("    SportS ShoES   ");
        productForm.setMrp(2999.362);

        ProductData productData = productDto.add(productForm);

        String barcode = productData.getBarcode();
        productData = productDto.get(barcode);

        Assert.assertEquals("puma", productData.getBrand());
        Assert.assertEquals("shoes", productData.getCategory());
        Assert.assertEquals("puma111", productData.getBarcode());
        Assert.assertEquals("sports shoes", productData.getProduct());
        Assert.assertEquals(2999.36, productData.getMrp(), 0.01);
    }

    @Test
    public void testGetAll() throws ApiException {
        List<BrandForm> brandFormList = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            BrandForm brandForm = new BrandForm();
            brandForm.setBrand("Brand" + i+1);
            brandForm.setCategory("Category" + i+1);
            brandFormList.add(brandForm);
        }
        brandDto.bulkAddBrand(brandFormList);

        for(int i = 0; i < 5; i++) {
            ProductForm productForm = new ProductForm();
            productForm.setBrand("Brand" + i+1);
            productForm.setCategory("Category" + i+1);
            productForm.setBarcode("Barcode" + i+1);
            productForm.setProduct("Product" + i+1);
            productForm.setMrp((i+1) * 100.23);
            productDto.add(productForm);
        }
        List<ProductData> productDataList = productDto.getAll();

        Assert.assertEquals(5, productDataList.size());
    }

    @Test
    public void testUpdate() throws ApiException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("PuMa    ");
        brandForm.setCategory("    SHOES    ");
        BrandData brandData = brandDto.add(brandForm);

        ProductForm productForm = new ProductForm();
        productForm.setBrand(brandData.getBrand());
        productForm.setCategory(brandData.getCategory());
        productForm.setBarcode("    PUmA111   ");
        productForm.setProduct("    SportS ShoES   ");
        productForm.setMrp(2999.362);
        ProductData productData = productDto.add(productForm);

        int id = productData.getId();
        ProductUpdateForm productUpdateForm = new ProductUpdateForm();
        productUpdateForm.setBarcode("  PumA222 ");
        productUpdateForm.setProduct(" Neon ShoES    ");
        productUpdateForm.setMrp(7999.828);

        productData = productDto.update(id, productUpdateForm);

        Assert.assertEquals("puma222", productData.getBarcode());
        Assert.assertEquals("neon shoes", productData.getProduct());
        Assert.assertEquals(7999.82, productData.getMrp(), 0.02);
    }

    @Test(expected = ApiException.class)
    public void testUpdateDuplicate() throws ApiException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("PuMa    ");
        brandForm.setCategory("    SHOES    ");
        BrandData brandData = brandDto.add(brandForm);

        ProductForm productForm = new ProductForm();
        productForm.setBrand(brandData.getBrand());
        productForm.setCategory(brandData.getCategory());
        productForm.setBarcode("    PUmA111   ");
        productForm.setProduct("    SportS ShoES   ");
        productForm.setMrp(2999.362);
        productDto.add(productForm);

        productForm.setBrand(brandData.getBrand());
        productForm.setCategory(brandData.getCategory());
        productForm.setBarcode("    PUmA222   ");
        productForm.setProduct("    neOn ShoES   ");
        productForm.setMrp(2999.362);
        ProductData productData = productDto.add(productForm);

        int id = productData.getId();
        ProductUpdateForm productUpdateForm = new ProductUpdateForm();
        productUpdateForm.setBarcode("  PumA111 ");
        productUpdateForm.setProduct(" Neon ShoES    ");
        productUpdateForm.setMrp(7999.828);

        productDto.update(id, productUpdateForm);
    }
}
