package com.example.taskmate.entity.support;

import java.sql.Date;
import java.sql.JDBCType;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public class TaskDynamicSqlSupport {
	public static final Task task = new Task();
	public static final SqlColumn<Integer> taskId = task.taskId;
	public static final SqlColumn<String> taskName = task.taskName;
	public static final SqlColumn<Date> limitDate = task.limitDate;
	public static final SqlColumn<String> statusCode = task.statusCode;
	public static final SqlColumn<String> remarks = task.remarks;
	
	
	public static final class Task extends SqlTable {
		public final SqlColumn<Integer> taskId = column("task_id",JDBCType.INTEGER);
		public final SqlColumn<String> taskName = column("task_name", JDBCType.VARCHAR);
		public final SqlColumn<Date> limitDate = column("limit_date", JDBCType.DATE);
		public final SqlColumn<String> statusCode = column("status_code", JDBCType.VARCHAR);
		public final SqlColumn<String> remarks = column("remarks", JDBCType.VARCHAR);

		public Task() {
			super("t_task");
		}
		
	}

}
