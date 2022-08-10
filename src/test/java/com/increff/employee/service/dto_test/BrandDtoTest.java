package com.increff.employee.service.dto_test;


import com.increff.employee.dto.BrandDto;
import com.increff.employee.model.data.BrandData;
import com.increff.employee.model.form.BrandForm;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.QaConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QaConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public class BrandDtoTest {
    @Autowired
    BrandDto brandDto;

    @Test
    public void testAddNormalize() throws ApiException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("PuMa    ");
        brandForm.setCategory("    SHOES    ");

        BrandData brandData = brandDto.add(brandForm);

        Assert.assertEquals("puma", brandData.getBrand());
        Assert.assertEquals("shoes", brandData.getCategory());
    }

    @Test(expected = ApiException.class)
    public void testAddDuplicate() throws ApiException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("PuMa    ");
        brandForm.setCategory("    SHOES    ");

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
    public void testGetWhenIdNotExists() throws ApiException {
        int id = 0;
        brandDto.get(id);
    }

    @Test
    public void testGetWhenIdExists() throws ApiException {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("PuMa    ");
        brandForm.setCategory("    SHOES    ");
        BrandData brandData = brandDto.add(brandForm);

        int id = brandData.getId();
        brandData = brandDto.get(id);

        Assert.assertEquals("puma", brandData.getBrand());
        Assert.assertEquals("shoes", brandData.getCategory());
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
        brandForm.setBrand("PuMa    ");
        brandForm.setCategory("    SHOES    ");
        BrandData brandData = brandDto.add(brandForm);

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
        brandForm.setBrand("  AdiDaS    ");
        brandForm.setCategory("    ShiRtS    ");
        BrandData brandData = brandDto.add(brandForm);

        int id = brandData.getId();
        brandForm.setBrand("   PUMA ");
        brandForm.setCategory("   ShoeS   ");

        brandDto.update(id, brandForm);
    }

}
