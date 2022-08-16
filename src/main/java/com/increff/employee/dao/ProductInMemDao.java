package com.increff.employee.dao;

import com.increff.employee.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class ProductInMemDao {
    private HashMap<Integer, ProductPojo> rows;
    private Integer lastId;

    @PostConstruct
    public void init() {
        rows = new HashMap<>();
    }

    public void insert(ProductPojo p) {
        lastId++;
        p.setId(lastId);
        rows.put(lastId, p);
    }

    public void delete(Integer id) {
        rows.remove(id);
    }

    public ProductPojo select(Integer id) {
        return rows.get(id);
    }

    public List<ProductPojo> selectAll() {
        return new ArrayList<>(rows.values());
    }

    public void update(Integer id, ProductPojo p) {
        rows.put(id, p);
    }
}
