package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.EmployeeDao;
import com.increff.employee.pojo.EmployeePojo;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeDao dao;

	@Transactional
	public void add(EmployeePojo p) {
		normalize(p);
		dao.insert(p);
	}

	@Transactional
	public void delete(int id) {
		dao.delete(id);
	}

	@Transactional(rollbackOn = ApiException.class)
	public EmployeePojo get(int id) throws RuntimeException {
		return getCheck(id);
	}

	@Transactional
	public List<EmployeePojo> getAll() {
		return dao.selectAll();
	}

	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id, EmployeePojo p) throws RuntimeException {
		normalize(p);
		EmployeePojo ex = getCheck(id);
		ex.setAge(p.getAge());
		ex.setName(p.getName());
		dao.update(p);
	}

	@Transactional
	public EmployeePojo getCheck(int id) {
		EmployeePojo p = dao.select(id);
		if (p == null) {
			throw new RuntimeException();
		}
		return p;
	}

	protected static void normalize(EmployeePojo p) {
		p.setName(p.getName().toLowerCase().trim());
	}
}
