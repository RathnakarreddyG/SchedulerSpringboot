package com.scheduler.service;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.scheduler.dao.EmployeeDTO;
import com.scheduler.dao.EmployeeDao;
import com.scheduler.models.Employee;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeDao employeeDao;

	public ResponseEntity<Object> addEmployee(Employee employee) {
		Employee checkEmail = employeeDao.findByEmail(employee.getEmail());
		if (checkEmail == null) {
			employee.setLastLoginTime(new Date());
			employee.setBlocked(false);
			employeeDao.save(employee);
			return new ResponseEntity<>(employeeDao.save(employee), HttpStatus.CREATED);
		} else {

			return new ResponseEntity<>("Email already registered", HttpStatus.BAD_REQUEST);
		}
	}

	public Object loginInto(EmployeeDTO employeeDTO) {
		Employee employee = employeeDao.findByEmail(employeeDTO.getUsername());
		if (employee != null && employee.getPassword().equalsIgnoreCase(employeeDTO.getPassword())) {
			if (employee.isBlocked() == true) {
				return new ResponseEntity<>("Employee Login was blocked", HttpStatus.BAD_REQUEST);
			}
			employee.setLastLoginTime(new Date());
			employeeDao.save(employee);
			return new ResponseEntity<>("Employee Login successful", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Invalid Credentials, Please try again", HttpStatus.BAD_REQUEST);
		}

	}

	@Scheduled(cron = "0/30 * * * * ?") // every 30 Seconds
	@Scheduled(cron = "0 0 12 * * ?") // every day 12AM)
	public void employeeScheduler() {
		List<Employee> employee = employeeDao.findAll().stream().filter(emp -> emp.isBlocked() == false).toList();
		employee.forEach(employeeInfo -> {
			long totalDaysSinceLastLogin = ChronoUnit.DAYS.between(employeeInfo.getLastLoginTime().toInstant(),
					new Date().toInstant());
			System.out.println("==========" + totalDaysSinceLastLogin);
			if (totalDaysSinceLastLogin >= 170) {
				employeeInfo.setBlocked(true);
				employeeDao.save(employeeInfo);
				System.out.println("This Employee login was blocked"+employeeInfo.getEmail());
			}
		});
	}

}
