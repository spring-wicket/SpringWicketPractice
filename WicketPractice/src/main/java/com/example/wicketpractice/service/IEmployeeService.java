package com.example.wicketpractice.service;

import java.util.List;

import com.example.wicketpractice.entity.Employee;

public interface IEmployeeService {
	
	public List<Employee> select(Employee employee);
		
	public int regist(Employee employee);
	
	public int delete(int id);
}
