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
    private int lastId;

    @PostConstruct
    public void init() {
        rows = new HashMap<>();
    }

    public void insert(BrandPojo p) {
        lastId++;
        p.setId(lastId);
        rows.put(lastId, p);
    }

    public void delete(int id) {
        rows.remove(id);
    }

    public BrandPojo select(int id) {
        return rows.get(id);
    }

    public List<BrandPojo> selectAll() {
        ArrayList<BrandPojo> list = new ArrayList<>();
        list.addAll(rows.values());
        return list;
    }

    public void update(int id, BrandPojo p) {
        rows.put(id, p);
    }
}
