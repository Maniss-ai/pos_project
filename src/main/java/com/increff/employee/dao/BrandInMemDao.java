package com.increff.employee.dao;

import com.increff.employee.pojo.BrandPojo;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class BrandInMemDao {
    private HashMap<Integer, BrandPojo> rows;
    private Integer lastId;

    @PostConstruct
    public void init() {
        rows = new HashMap<>();
    }

    public void insert(BrandPojo p) {
        lastId++;
        p.setId(lastId);
        rows.put(lastId, p);
    }

    public void delete(Integer id) {
        rows.remove(id);
    }

    public BrandPojo select(Integer id) {
        return rows.get(id);
    }

    public List<BrandPojo> selectAll() {
        return new ArrayList<>(rows.values());
    }

    public void update(Integer id, BrandPojo p) {
        rows.put(id, p);
    }
}
