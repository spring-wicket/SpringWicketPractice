package com.example.taskmate.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;

import com.example.taskmate.entity.Status;

@Mapper
public interface StatusRepository {
	
	@SelectProvider(type=SqlProviderAdapter.class, method="select")
	@Results(id = "StatusResult", value = {
		@Result(column = "status_code", property = "statusCode", jdbcType = JdbcType.VARCHAR),
		@Result(column = "status_name", property = "statusName", jdbcType = JdbcType.VARCHAR)
	})
	List<Status> select(SelectStatementProvider selectStatementProvider);
	
}
