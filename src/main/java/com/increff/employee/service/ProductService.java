package com.increff.employee.service;

import com.increff.employee.dao.ProductDao;
import com.increff.employee.model.form.ReportForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.TypedQuery;
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

    @Transactional(rollbackOn = ApiException.class)
    public ProductPojo getWithId(Integer id) throws ApiException {
        return getCheck(id);
    }

    @Transactional(rollbackOn = ApiException.class)
    public ProductPojo getWithBarcode(String barcode) throws ApiException {
        return getCheckWithBarcode(barcode);
    }

    @Transactional
    public List<ProductPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public ProductPojo update(Integer id, ProductPojo p) throws ApiException {
        ProductPojo pojo = getCheck(id);
        pojo.setBarcode(p.getBarcode());
        pojo.setProduct(p.getProduct());
        pojo.setMrp(p.getMrp());
        return pojo;
    }

    @Transactional
    public ProductPojo getInventoryBarcode(String barcode) throws ApiException {
        try {
            return dao.getInventoryBarcode(barcode);
        }
        catch (Exception e) {
            throw new ApiException("Barcode doesn't exists");
        }
    }

    @Transactional
    public List<ProductPojo> getWithBrandCategory(Integer brandCategory) {
        return dao.getWithBrandCategory(brandCategory);
    }

    @Transactional
    public ProductPojo getCheck(Integer id) throws ApiException {
        try {
            return dao.select(id);
        }
        catch (Exception e) {
            throw new ApiException("Id doesn't exists");
        }
    }

    private ProductPojo getCheckWithBarcode(String barcode) throws ApiException {
        try {
            return dao.getInventoryBarcode(barcode);
        }
        catch (Exception e) {
            throw new ApiException("Barcode doesn't exists");
        }
    }
}
