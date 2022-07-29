package com.increff.employee.service;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandDao dao;

    @Transactional
    public BrandPojo add(BrandPojo p) {
        dao.insert(p);
        return p;
    }

    @Transactional
    public void delete(int id) throws ApiException {
        dao.delete(id);
    }

    @Transactional(rollbackOn = ApiException.class)
    public BrandPojo get(int id) throws ApiException {
        try {
            return getCheck(id);
        }
        catch (Exception e) {
            throw new ApiException("Unable to fetch, ID doesn't exists");
        }
    }

    @Transactional
    public List<BrandPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public BrandPojo update(int id, BrandPojo p) throws ApiException {
        BrandPojo ex = getCheck(id);
        ex.setCategory(p.getCategory());
        ex.setBrand(p.getBrand());
        dao.update(p);
        return ex;
    }

    @Transactional
    public BrandPojo getCheck(int id) throws ApiException {
        BrandPojo p = dao.select(id);
        if (p == null) {
            throw new ApiException("Brand " + get(id).getBrand() + " does not exit");
        }
        return p;
    }

    @Transactional
    public BrandPojo getBrandCategory(ProductPojo pojo) throws ApiException {
        return dao.getBrandCategory(pojo);
    }

}
