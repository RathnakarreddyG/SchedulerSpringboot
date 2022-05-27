package com.scheduler.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scheduler.models.Employee;

public interface EmployeeDao extends JpaRepository<Employee, Long> {

	Employee findByEmail(String email);

}
