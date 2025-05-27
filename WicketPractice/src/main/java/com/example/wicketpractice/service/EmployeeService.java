package com.example.wicketpractice.service;

import java.util.List;

import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.springframework.stereotype.Service;

import com.example.wicketpractice.entity.Employee;
import com.example.wicketpractice.entity.support.EmployeeDynamicSqlSupport;
import com.example.wicketpractice.mapper.EmployeeMapper;

@Service
public class EmployeeService implements IEmployeeService {
	private final EmployeeMapper mapper;
	
	public EmployeeService(EmployeeMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public List<Employee> select(Employee employee) {
		SelectStatementProvider provider = SqlBuilder
				.select(EmployeeDynamicSqlSupport.employee.allColumns())
				.from(EmployeeDynamicSqlSupport.employee)
				.where(EmployeeDynamicSqlSupport.id, SqlBuilder.isEqualToWhenPresent(employee.getId()))
				.where(EmployeeDynamicSqlSupport.name, SqlBuilder.isLikeWhenPresent(employee.getName()))
				.orderBy(EmployeeDynamicSqlSupport.id)
				.build()
				.render(RenderingStrategies.MYBATIS3);
		return mapper.select(provider);
	}

	@Override
	public int regist(Employee employee) {
		InsertStatementProvider<Employee> provider = SqlBuilder
				.insert(employee)
				.into(EmployeeDynamicSqlSupport.employee)
				.map(EmployeeDynamicSqlSupport.id).toProperty("id")
				.map(EmployeeDynamicSqlSupport.name).toProperty("name")
				.build()
				.render(RenderingStrategies.MYBATIS3);
		return mapper.insert(provider);
	}

	@Override
	public int delete(int id) {
		DeleteStatementProvider provider = SqlBuilder
				.deleteFrom(EmployeeDynamicSqlSupport.employee)
				.where(EmployeeDynamicSqlSupport.id, SqlBuilder.isEqualTo(id))
				.build()
				.render(RenderingStrategies.MYBATIS3);
		return mapper.delete(provider);
	}

}
