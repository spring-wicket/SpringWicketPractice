package com.example.taskmate.repository;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;

import com.example.taskmate.entity.Memo;
import com.example.taskmate.entity.Task;
import com.example.taskmate.entity.TaskDetail;
import com.example.taskmate.entity.TaskSummary;

@Mapper
public interface TaskRepository {
	
	@SelectProvider(type=SqlProviderAdapter.class, method="select")
	@Results(id = "TaskResult", value = {
		@Result(column = "task_id", property = "taskId", jdbcType = JdbcType.INTEGER),
		@Result(column = "task_name", property = "taskName", jdbcType = JdbcType.VARCHAR),
		@Result(column = "limit_date", property = "limitDate", jdbcType = JdbcType.DATE),
		@Result(column = "status_code", property = "status.statusCode", jdbcType = JdbcType.VARCHAR),
		@Result(column = "status_name", property = "status.statusName", jdbcType = JdbcType.VARCHAR),
		@Result(column = "memo_cnt", property = "memoCnt", jdbcType = JdbcType.INTEGER)
	})
	List<TaskSummary> selectList(SelectStatementProvider selectStatementProvider);

	@InsertProvider(type=SqlProviderAdapter.class, method="insert")
	int insert(InsertStatementProvider<Task> insertStatementProvider);
	
	@SelectProvider(type=SqlProviderAdapter.class, method="select")
	@Results(id = "TaskDetail", value = {
			@Result(column = "task_id", property = "taskId", jdbcType = JdbcType.INTEGER),
			@Result(column = "task_name", property = "taskName", jdbcType = JdbcType.VARCHAR),
			@Result(column = "limit_date", property = "limitDate", jdbcType = JdbcType.DATE),
			@Result(column = "status_code", property = "status.statusCode", jdbcType = JdbcType.VARCHAR),
			@Result(column = "status_name", property = "status.statusName", jdbcType = JdbcType.VARCHAR),
			@Result(column = "remarks", property = "remarks", jdbcType = JdbcType.VARCHAR),
			@Result(column = "task_id", property = "memoList", many = @Many(select = "selectMemoByTaskId"))
		})
	TaskDetail selectDetailByTaskId(SelectStatementProvider selectStatementProvider);
	
	@Select("SELECT * FROM t_memo WHERE task_id = #{taskId}")
	List<Memo> selectMemoByTaskId(int taskId);

}
