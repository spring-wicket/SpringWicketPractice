package com.example.taskmate.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;

import com.example.taskmate.entity.Task;
import com.example.taskmate.entity.TaskSummary;
import com.example.taskmate.repository.TaskRepository;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {
	@InjectMocks
	private TaskServiceImpl service;
	
	@Mock
	private TaskRepository repository;
		
	@SuppressWarnings("unchecked")
	@Test
	void testRegist() {
		Task target = new Task();
		target.setTaskName("test");
		target.setLimitDate(Date.valueOf("2025-04-01"));
		target.setStatusCode("00");
		target.setRemarks("test");
		
		// モックの設定
		when(repository.insert(ArgumentMatchers.any(InsertStatementProvider.class))).thenReturn(1);
		
		assertEquals(1, service.regist(target));
		// insertの呼び出し回数のテスト
		verify(repository, times(1)).insert(ArgumentMatchers.any(InsertStatementProvider.class));
	}
	
	@Test
	void testFindByCondition() {
		TaskSummary task = new TaskSummary();
		when(repository.selectList(ArgumentMatchers.any(SelectStatementProvider.class))).thenReturn(List.of(task));
		
		List<TaskSummary> result = service.findListByConditions(new Task());
		assertEquals(1, result.size());
		verify(repository, times(1)).selectList(ArgumentMatchers.any(SelectStatementProvider.class));
	}
}
