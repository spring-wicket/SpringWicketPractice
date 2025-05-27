package com.example.taskmate.entity.support;

import java.sql.JDBCType;

import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public class StatusDynamicSqlSupport {
	public static final Status status = new Status();
	public static final SqlColumn<String> statusCode = status.statusCode;
	public static final SqlColumn<String> statusName = status.statusName;
	
	public static final class Status extends SqlTable {
		public final SqlColumn<String> statusCode = column("status_code", JDBCType.VARCHAR);
		public final SqlColumn<String> statusName = column("status_name", JDBCType.VARCHAR);
		
		protected Status() {
			super("m_status");
		}
		
	}

}
