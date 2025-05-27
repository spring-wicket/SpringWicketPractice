package com.example.taskmate.entity.support;

import java.sql.JDBCType;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public class MemoDynamicSqlSupport {
	public static final Memo memoInstance = new Memo();
	public static final SqlColumn<Integer> memoId = memoInstance.memoId;
	public static final SqlColumn<Integer> taskId = memoInstance.taskId;
	public static final SqlColumn<String> memo = memoInstance.memo;
	
	public static final class Memo extends SqlTable {
		public final SqlColumn<Integer> memoId = column("memo_id", JDBCType.INTEGER);
		public final SqlColumn<Integer> taskId = column("task_id", JDBCType.INTEGER);
		public final SqlColumn<String> memo = column("memo", JDBCType.VARCHAR);
		
		protected Memo() {
			super("t_memo");
		}
		
	}

}
