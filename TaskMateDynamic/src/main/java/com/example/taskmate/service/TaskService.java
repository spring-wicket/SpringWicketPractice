package com.example.taskmate.service;

import java.util.List;

import com.example.taskmate.entity.Task;
import com.example.taskmate.entity.TaskDetail;
import com.example.taskmate.entity.TaskSummary;

public interface TaskService {
	
//	List<TaskSummary> findListAll();
	
	int regist(Task task);
	
	List<TaskSummary> findListByConditions(Task task);
	
	TaskDetail findDetailByTaskId(Integer taskId);

}
