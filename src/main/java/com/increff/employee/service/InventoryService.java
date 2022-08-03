package com.increff.employee.service;

import com.increff.employee.dao.InventoryDao;
import com.increff.employee.pojo.InventoryPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Transactional
    public void delete(int id) {
        dao.delete(id);
    }

    @Transactional(rollbackOn = ApiException.class)
    public InventoryPojo get(int id) throws ApiException {
        System.out.println("get worked");
        return dao.select(id);
    }

    @Transactional
    public List<InventoryPojo> getAll() {
        return dao.selectAll();
    }

    @Transactional(rollbackOn  = ApiException.class)
    public void update(String barcode, InventoryPojo p) throws ApiException {
        InventoryPojo pojo = getCheck(barcode);
        pojo.setInventory(p.getInventory());
        dao.update(p);
    }

    @Transactional
    public InventoryPojo getCheck(String barcode) throws ApiException {
        InventoryPojo p = dao.selectBarcode(barcode);
        if (p == null) {
            throw new ApiException("Inventory with given barcode : " + barcode + " does not exit");
        }
        return p;
    }
}