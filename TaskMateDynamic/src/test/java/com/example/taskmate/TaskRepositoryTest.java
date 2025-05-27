package com.example.taskmate;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Date;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import com.example.taskmate.entity.Task;
import com.example.taskmate.entity.support.TaskDynamicSqlSupport;
import com.example.taskmate.repository.TaskRepository;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@Import(DatasourceConfig.class)
@Transactional
public class TaskRepositoryTest {
	@Autowired
	private DataSource dataSource;
	@Autowired
	private TaskRepository taskRepository;
	
	private Connection con;
	
	private IDatabaseConnection iconn;
	
	private static final String TABLE_NAME = "t_task";
	
	
	@BeforeAll
	public void beforeAll() throws Exception {
		 this.con = dataSource.getConnection();
		 this.iconn = new DatabaseConnection(con);
	}
	
	@BeforeEach
	public void cleanAndInsert() throws Exception {
		Path xlsxFile = Paths.get("src\\test\\resources\\test_t_task.xlsx");
		IDataSet dataSet = new XlsDataSet(xlsxFile.toFile());
		// シーケンスのリセット(追加するデータのIDの次から割り振るようにする)
		this.iconn.getConnection().createStatement().execute("select setval ('t_task_task_id_seq', " 
				+ dataSet.getTable(TABLE_NAME).getRowCount() + ", true)");
		DatabaseOperation.CLEAN_INSERT.execute(iconn, dataSet);
	}
	
	@AfterAll
	public void afterAll() throws Exception {
		this.con.close();
	}
	
	@Test
	public void testInsert() throws Exception {
		// 期待値をファイルから読み込み
		Path xlsxFile = Paths.get("src\\test\\resources\\expected_testInsert.xlsx");
		ITable expected = new XlsDataSet(xlsxFile.toFile()).getTable(TABLE_NAME);
		
		
		Task task = new Task();
		task.setTaskName("test_insert");
		task.setLimitDate(Date.valueOf("2025-04-01"));
		task.setStatusCode("05");
		task.setRemarks("insertのテスト");
		
		InsertStatementProvider<Task> insert = SqlBuilder.insert(task)
				.into(TaskDynamicSqlSupport.task)
				.map(TaskDynamicSqlSupport.taskName).toProperty("taskName")
				.map(TaskDynamicSqlSupport.limitDate).toProperty("limitDate")
				.map(TaskDynamicSqlSupport.statusCode).toProperty("statusCode")
				.map(TaskDynamicSqlSupport.remarks).toProperty("remarks")
				.build()
				.render(RenderingStrategies.MYBATIS3);
		
		assertEquals(1, taskRepository.insert(insert));
		ITable result = iconn.createDataSet().getTable(TABLE_NAME);
		
		assertEquals(expected.getRowCount(), result.getRowCount());
		
		for (int row = 0; row < expected.getRowCount(); row++) {
			for (Column column : expected.getTableMetaData().getColumns()) {
				String columnName = column.getColumnName();
				assertEquals(expected.getValue(row, columnName).toString(), 
						result.getValue(row, columnName).toString(), columnName + "でエラー");
			}
		}
		
	}

}
