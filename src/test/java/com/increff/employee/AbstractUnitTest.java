package com.increff.employee;

import javax.transaction.Transactional;

import com.increff.employee.model.form.BrandForm;
import com.increff.employee.model.form.InventoryForm;
import com.increff.employee.model.form.ProductForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public abstract class AbstractUnitTest {
    public static BrandPojo addBrandPojo() {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("brand");
        brandPojo.setCategory("category");
        return brandPojo;
    }

    public static BrandForm addBrandForm() {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("brand");
        brandForm.setCategory("category");
        return brandForm;
    }

    public static ProductPojo addProductPojo() {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setProduct("product");
        productPojo.setBarcode("barcode");
        productPojo.setMrp(100.00);
        productPojo.setBrandCategoryId(1);
        return productPojo;
    }

    public static ProductForm addProductForm() {
        ProductForm productForm = new ProductForm();
        productForm.setProduct("product");
        productForm.setBarcode("barcode");
        productForm.setMrp(100.00);
        productForm.setBrand("brand");
        productForm.setCategory("category");
        return productForm;
    }

    public static InventoryForm addInventoryForm() {
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode("barcode");
        inventoryForm.setInventory(100);
        return inventoryForm;
    }

}
