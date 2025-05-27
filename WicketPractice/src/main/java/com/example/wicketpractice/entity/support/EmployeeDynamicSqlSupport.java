package com.example.wicketpractice.entity.support;

import java.sql.JDBCType;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public class EmployeeDynamicSqlSupport {
	public static final Employee employee = new Employee();
	public static final SqlColumn<Integer> id = employee.id;
	public static final SqlColumn<String> name = employee.name;
	
	public static final class Employee extends SqlTable {
		private final SqlColumn<Integer> id = column("id", JDBCType.INTEGER);
		private final SqlColumn<String> name = column("name", JDBCType.VARCHAR);
		protected Employee() {
			super("employee");
		}
		
	}

}
