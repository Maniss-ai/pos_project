package com.increff.employee;

import javax.transaction.Transactional;

import com.increff.employee.model.form.BrandForm;
import com.increff.employee.pojo.BrandPojo;
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

}
