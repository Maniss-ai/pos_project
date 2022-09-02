package com.increff.employee.service;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.pojo.BrandPojo;
import io.swagger.annotations.Api;
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
        return dao.insert(p);
    }

    @Transactional(rollbackOn = ApiException.class)
    public BrandPojo get(Integer id) throws ApiException {
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
    public BrandPojo update(Integer id, BrandPojo p) throws ApiException {
        BrandPojo brandPojo = getCheck(id);
        brandPojo.setCategory(p.getCategory());
        brandPojo.setBrand(p.getBrand());
        return brandPojo;
    }

    @Transactional
    public BrandPojo getCheck(Integer id) throws ApiException {
        try {
            return dao.select(id);
        }
        catch (Exception e) {
            throw new ApiException("Id doesn't exists");
        }
    }

    @Transactional
    public BrandPojo getBrandCategory(String brand, String category) throws ApiException {
        try {
            return dao.getBrandCategory(brand, category);
        }
        catch (Exception e) {
            throw new ApiException("Brand Category pair doesn't exists");
        }
    }

    @Transactional
    public List<BrandPojo> getBrand(String brand) throws ApiException {
        List<BrandPojo> brandPojoList = dao.getBrand(brand);
        if(brandPojoList.size() == 0) {
            throw new ApiException("Brand doesn't exists");
        }

        return brandPojoList;
    }

    @Transactional
    public List<BrandPojo> getCategory(String category) throws ApiException {
        List<BrandPojo> brandPojoList = dao.getCategory(category);
        if(brandPojoList.size() == 0)
            throw new ApiException("Category doesn't exists");

        return brandPojoList;
    }

}
