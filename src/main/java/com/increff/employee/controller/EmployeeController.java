package com.increff.employee.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.data.EmployeeData;
import com.increff.employee.model.form.EmployeeForm;
import com.increff.employee.pojo.EmployeePojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.EmployeeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class EmployeeController {

	@Autowired
	private EmployeeService service;

	@ApiOperation(value = "Adds an employee")
	@RequestMapping(path = "/api/employee", method = RequestMethod.POST)
	public void add(@RequestBody EmployeeForm form) {
		EmployeePojo pojo = convertFormToPojo(form);
		service.add(pojo);
	}

	
	@ApiOperation(value = "Deletes an employee")
	@RequestMapping(path = "/api/employee/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		service.delete(id);
	}

	@ApiOperation(value = "Gets an employee by ID")
	@RequestMapping(path = "/api/employee/{id}", method = RequestMethod.GET)
	public EmployeeData get(@PathVariable int id) throws ApiException {
		EmployeePojo pojo = service.get(id);
		return convertPojoToData(pojo);
	}

	@ApiOperation(value = "Gets list of all employees")
	@RequestMapping(path = "/api/employee", method = RequestMethod.GET)
	public List<EmployeeData> getAll() {
		List<EmployeePojo> list = service.getAll();
		List<EmployeeData> list2 = new ArrayList<>();
		for (EmployeePojo p : list) {
			list2.add(convertPojoToData(p));
		}
		return list2;
	}

	@ApiOperation(value = "Updates an employee")
	@RequestMapping(path = "/api/employee/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody EmployeeForm form) throws ApiException {
		EmployeePojo pojo = convertFormToPojo(form);
		service.update(id, pojo);
	}
	

	private static EmployeeData convertPojoToData(EmployeePojo pojo) {
		EmployeeData data = new EmployeeData();
		data.setAge(pojo.getAge());
		data.setName(pojo.getName());
		data.setId(pojo.getId());
		return data;
	}

	private static EmployeePojo convertFormToPojo(EmployeeForm form) {
		EmployeePojo pojo = new EmployeePojo();
		pojo.setAge(form.getAge());
		pojo.setName(form.getName());
		return pojo;
	}

}
