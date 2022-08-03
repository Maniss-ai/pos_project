package com.increff.employee.service;

import com.increff.employee.dao.ProductDao;
import com.increff.employee.model.form.ReportForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductDao dao;

    @Transactional
    public ProductPojo add(ProductPojo p) {
        return dao.insert(p);
    }

    @Transactional
    public void delete(int id) {
        dao.delete(id);
    }

    @Transactional(rollbackOn = ApiException.class)
    public ProductPojo get(String barcode) throws ApiException {
        return getCheckWithBarcode(barcode);
    }

    @Transactional
    public List<ProductPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(int id, ProductPojo p) throws ApiException {
        ProductPojo pojo = getCheck(id);
        pojo.setBarcode(p.getBarcode());
        pojo.setProduct(p.getProduct());
        pojo.setMrp(p.getMrp());
        dao.update(p);
    }

    @Transactional
    public ProductPojo getInventoryBarcode(InventoryPojo pojo) throws ApiException {
        return dao.getInventoryBarcode(pojo.getBarcode());
    }

    @Transactional
    public List<ProductPojo> getProductWithBrandCategory(ReportForm form) {
        return dao.getProductWithBrandCategory(form);
    }

    @Transactional
    public List<ProductPojo> getProductWithBrand(String brand) {
        return dao.getProductWithBrand(brand);
    }

    @Transactional
    public List<ProductPojo> getProductWithCategory(ReportForm form) {
        return dao.getProductWithCategory(form);
    }

    @Transactional
    public List<ProductPojo> getProductWithDate(ReportForm form) {
        return dao.getProductWithDate(form);
    }

    @Transactional
    public ProductPojo getCheck(int id) throws ApiException {
        ProductPojo p = dao.select(id);
        if (p == null) {
            throw new ApiException("Brand-Category does not exit");
        }
        return p;
    }

    private ProductPojo getCheckWithBarcode(String barcode) throws ApiException {
        ProductPojo p = dao.getInventoryBarcode(barcode);
        if (p == null) {
            throw new ApiException("Brand-Category " + get(barcode).getBrand() + " does not exit");
        }
        return p;
    }
}
