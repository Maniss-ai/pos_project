package com.increff.employee.service;

import com.increff.employee.dao.InventoryDao;
import com.increff.employee.pojo.InventoryPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private InventoryDao dao;

    @Transactional
    public InventoryPojo add(InventoryPojo p) {
        return dao.insert(p);
    }

    @Transactional(rollbackOn = ApiException.class)
    public InventoryPojo get(Integer id) throws ApiException {
        try {
            return dao.select(id);
        }
        catch (Exception e) {
            throw new ApiException("Id doesn't exists");
        }
    }

    @Transactional
    public List<InventoryPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public InventoryPojo update(String barcode, InventoryPojo p) throws ApiException {
        InventoryPojo pojo = getCheck(barcode);
        pojo.setInventory(p.getInventory());
        return pojo;
    }

    @Transactional
    public InventoryPojo getCheck(String barcode) throws ApiException {
        try {
            InventoryPojo p = dao.selectBarcode(barcode);
            if (p == null) {
                throw new ApiException("Inventory with given barcode : " + barcode + " does not exit");
            }
            return p;
        }
        catch (Exception e) {
            throw new ApiException("Barcode doesn't exists");
        }
    }
}
