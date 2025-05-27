package com.example.wicketpractice.mapper;

import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;

import com.example.wicketpractice.entity.Employee;

@Mapper
public interface EmployeeMapper {
	@SelectProvider(type=SqlProviderAdapter.class, method="select")
	@Results(id = "EmployeeResult", value = {
		@Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER),
		@Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR)
	})
	public List<Employee> select(SelectStatementProvider provider);
	
	@InsertProvider(type = SqlProviderAdapter.class, method = "insert")
	public int insert(InsertStatementProvider<Employee> provider);
	
	@DeleteProvider(type = SqlProviderAdapter.class, method = "delete")
	public int delete(DeleteStatementProvider provider);
}
