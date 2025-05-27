package com.example.taskmate.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.taskmate.entity.Status;
import com.example.taskmate.entity.Task;
import com.example.taskmate.entity.TaskSummary;
import com.example.taskmate.service.StatusService;
import com.example.taskmate.service.TaskService;

@WebMvcTest(controllers = TaskSearchController.class)
public class TaskSearchControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	private TaskService taskService;
	@MockitoBean
	private StatusService statusService;
	
	@Test
	void testShowListSelection() throws Exception {
		// テストデータ
		Status record1 = new Status();
		record1.setStatusCode("00");
		record1.setStatusName("未着手");
		Status record2 = new Status();
		record2.setStatusCode("05");
		record2.setStatusName("進行中");
		Status record3 = new Status();
		record3.setStatusCode("10");
		record3.setStatusName("完了");
		
		// モックの設定
		when(statusService.findAll()).thenReturn(List.of(record1, record2, record3));
		
		mockMvc.perform(get("/top").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		// 実際のアプリでは数が膨大であることが多いので、一部の検証をいくつか行えばよい
		.andExpect(model().attribute("statusList", hasItems(
				hasProperty("statusCode", is("00")),
				hasProperty("statusName", is("進行中")),
				hasProperty("statusCode", is("10"))
		)));
		
		verify(statusService, times(1)).findAll();
	}
	
	@Test
	void testSearchList() throws Exception {
		// テストデータ
		Status status = new Status();
		status.setStatusCode("00");
		status.setStatusName("未着手");
		// モックの設定
		when(statusService.findAll()).thenReturn(List.of(status));
		
		// テストデータ
		TaskSummary task1 = new TaskSummary();
		task1.setTaskId(1);
		task1.setTaskName("a");
		task1.setLimitDate(Date.valueOf("2025-05-01"));
		task1.setStatus(status);
		task1.setMemoCnt(2);
		
		// TaskServiceのモック
		when(taskService.findListByConditions(ArgumentMatchers.any(Task.class)))
			.thenReturn(List.of(task1));
		
		// テスト
		mockMvc.perform(post("/task-search-list").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("taskName", "")
				.param("statusCode", ""))
		.andExpect(status().isOk())
		.andExpect(model().attribute("taskSummaryList", hasItems(
				hasProperty("taskName", is("a"))
				)))
		.andExpect(model().attributeExists("statusList"));
		
		// 呼び出し回数のテスト
		verify(statusService, times(1)).findAll();
		verify(taskService, times(1)).findListByConditions(ArgumentMatchers.any(Task.class));
	}

}
