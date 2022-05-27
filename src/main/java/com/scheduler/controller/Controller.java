package com.scheduler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scheduler.dao.EmployeeDTO;
import com.scheduler.models.Employee;
import com.scheduler.service.EmployeeService;

@RestController
@RequestMapping("/employee")
public class Controller {
	
	@Autowired
	private EmployeeService employeeService;
	
	@PostMapping("/login")
	public Object employeeLogin(@RequestBody EmployeeDTO employeeDTO) {
		return employeeService.loginInto(employeeDTO);
	}
	
	@PostMapping("/add")
	public Object addEmployee(@RequestBody Employee employee) {
		return employeeService.addEmployee(employee);
	}

}
