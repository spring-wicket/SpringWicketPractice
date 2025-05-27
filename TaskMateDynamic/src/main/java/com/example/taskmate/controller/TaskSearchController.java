package com.example.taskmate.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.taskmate.entity.Status;
import com.example.taskmate.entity.Task;
import com.example.taskmate.entity.TaskDetail;
import com.example.taskmate.entity.TaskSummary;
import com.example.taskmate.form.TaskSearchDetailForm;
import com.example.taskmate.form.TaskSearchListForm;
import com.example.taskmate.service.StatusService;
import com.example.taskmate.service.TaskService;

@Controller
public class TaskSearchController {
	
	private final TaskService taskService;
	private final StatusService statusService;

//	@Autowired
	public TaskSearchController(TaskService taskService, StatusService statusService) {
		this.taskService = taskService;
		this.statusService = statusService;
	}

	@GetMapping("/top")
	private String showListSelection(@ModelAttribute TaskSearchListForm form, Model model) {
		
		List<Status> list = statusService.findAll();
		model.addAttribute("statusList", list);
		
		return "task-list";
	}
	
	@PostMapping("task-search-list")
	private String searchList(@Validated @ModelAttribute TaskSearchListForm form, 
			BindingResult result,
			Model model) {
		
		Task task = new Task();
		if (!form.getTaskName().equals("")) {
			task.setTaskName("%" + form.getTaskName() + "%");
		}
		task.setLimitDate(form.getLimitDate());
		if (!form.getStatusCode().equals("")) {
			task.setStatusCode(form.getStatusCode());
		}
		
		List<TaskSummary> list = taskService.findListByConditions(task);
		
		// ステータスリストをModelに設定(次回検索用)
		List<Status> statusList = statusService.findAll();
		model.addAttribute("statusList", statusList);
		
		model.addAttribute("taskSummaryList", list);
		return "task-list";
	}
	
	@PostMapping("/task-search-detail")
	private String searchDetail(TaskSearchDetailForm form, Model model) {
		TaskDetail taskDetail = taskService.findDetailByTaskId(form.getTaskId());
		model.addAttribute("taskDetail", taskDetail);
		
		return "task-detail";
	}
}
