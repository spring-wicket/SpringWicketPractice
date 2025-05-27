package com.example.taskmate.service;

import java.util.List;

import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.aggregate.Count;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.taskmate.entity.Task;
import com.example.taskmate.entity.TaskDetail;
import com.example.taskmate.entity.TaskSummary;
import com.example.taskmate.entity.support.MemoDynamicSqlSupport;
import com.example.taskmate.entity.support.StatusDynamicSqlSupport;
import com.example.taskmate.entity.support.TaskDynamicSqlSupport;
import com.example.taskmate.repository.TaskRepository;

@Service
public class TaskServiceImpl implements TaskService {
	private final TaskRepository taskRepository;
	
//	@Autowired
	public TaskServiceImpl(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}


	@Override
	@Transactional
	public int regist(Task task) {
		InsertStatementProvider<Task> insert = SqlBuilder.insert(task)
												.into(TaskDynamicSqlSupport.task)
												.map(TaskDynamicSqlSupport.taskName).toProperty("taskName")
												.map(TaskDynamicSqlSupport.limitDate).toProperty("limitDate")
												.map(TaskDynamicSqlSupport.statusCode).toProperty("statusCode")
												.map(TaskDynamicSqlSupport.remarks).toProperty("remarks")
												.build()
												.render(RenderingStrategies.MYBATIS3);
		
		return taskRepository.insert(insert);
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<TaskSummary> findListByConditions(Task task) {
		SelectStatementProvider select = SqlBuilder
										.select(TaskDynamicSqlSupport.taskId,
												TaskDynamicSqlSupport.taskName,
												TaskDynamicSqlSupport.limitDate,
												TaskDynamicSqlSupport.statusCode,
												StatusDynamicSqlSupport.statusName,
												// memoIdをmemo_cntって名前でカウントする
												Count.of(MemoDynamicSqlSupport.memoId).as("memo_cnt"))
										.from(TaskDynamicSqlSupport.task)
										.join(StatusDynamicSqlSupport.status)
											.on(TaskDynamicSqlSupport.statusCode, 
													SqlBuilder.equalTo(StatusDynamicSqlSupport.statusCode))
										.leftJoin(MemoDynamicSqlSupport.memoInstance)
											.on(TaskDynamicSqlSupport.taskId, 
													SqlBuilder.equalTo(MemoDynamicSqlSupport.taskId))
										.where(TaskDynamicSqlSupport.taskName,  
												SqlBuilder.isLikeWhenPresent(task.getTaskName()))
										.and(TaskDynamicSqlSupport.limitDate,  
												SqlBuilder.isLessThanOrEqualToWhenPresent(task.getLimitDate()))
										.and(TaskDynamicSqlSupport.statusCode,  
												SqlBuilder.isEqualToWhenPresent(task.getStatusCode()))
										.groupBy(TaskDynamicSqlSupport.taskId,
												TaskDynamicSqlSupport.taskName,
												TaskDynamicSqlSupport.limitDate,
												StatusDynamicSqlSupport.statusCode,
												StatusDynamicSqlSupport.statusName)
										.orderBy(TaskDynamicSqlSupport.taskId)
										.build()
										.render(RenderingStrategies.MYBATIS3);
		List<TaskSummary> list = taskRepository.selectList(select);
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public TaskDetail findDetailByTaskId(Integer taskId) {
		SelectStatementProvider select = SqlBuilder
				.select(TaskDynamicSqlSupport.task.allColumns(),
						StatusDynamicSqlSupport.statusName,
						MemoDynamicSqlSupport.memoInstance.allColumns())
				.from(TaskDynamicSqlSupport.task)
				.join(StatusDynamicSqlSupport.status)
					.on(TaskDynamicSqlSupport.statusCode, 
							SqlBuilder.equalTo(StatusDynamicSqlSupport.statusCode))
				.leftJoin(MemoDynamicSqlSupport.memoInstance)
					.on(TaskDynamicSqlSupport.taskId, 
							SqlBuilder.equalTo(MemoDynamicSqlSupport.taskId))
				.where(TaskDynamicSqlSupport.taskId,  
						SqlBuilder.isEqualTo(taskId))
				.build()
				.render(RenderingStrategies.MYBATIS3);
		return taskRepository.selectDetailByTaskId(select);
	}

}
