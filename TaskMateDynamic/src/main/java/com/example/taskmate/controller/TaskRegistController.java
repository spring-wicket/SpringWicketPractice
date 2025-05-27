package com.example.taskmate.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.taskmate.entity.Status;
import com.example.taskmate.entity.Task;
import com.example.taskmate.form.TaskRegistForm;
import com.example.taskmate.service.StatusService;
import com.example.taskmate.service.TaskService;

@Controller
public class TaskRegistController {
	
	private final StatusService statusService;
	private final TaskService taskService;

//	@Autowired
	public TaskRegistController(StatusService statusService, TaskService taskService) {
		this.statusService = statusService;
		this.taskService = taskService;
	}

	@PostMapping("/task-show-regist")
	// @ModelAttributeでtaskRegistFormというModelを作成する
	public String showRegist(@ModelAttribute TaskRegistForm form, Model model) {
		List<Status> list = statusService.findAll();
		model.addAttribute("statusList", list);
		
		return "task-regist";
	}
	
	@PostMapping("/task-regist")
	public String regist(
			@Validated @ModelAttribute TaskRegistForm form,
			// バリデーションの結果
			BindingResult result,
			Model model) {
		
		if (result.hasErrors()) {
			List<Status> list = statusService.findAll();
			model.addAttribute("statusList", list);
			
			return "task-regist";
		}
		
		Status status = statusService.findByCode(form.getStatusCode());
		form.setStatusName(status.getStatusName());
		
		return "task-confirm-regist";
	}
	
	@PostMapping("/task-confirm-regist")
	public String confirmRegist(
			@Validated @ModelAttribute TaskRegistForm form,
			BindingResult result,
			RedirectAttributes redirectAttributes,
			Model model) {
		
		if (result.hasErrors()) {
			List<Status> list = statusService.findAll();
			model.addAttribute("statusList", list);
			
			return "task-regist";
		}
		
		Task task = new Task();
		task.setTaskName(form.getTaskName());
		task.setLimitDate(form.getLimitDate());
		task.setStatusCode(form.getStatusCode());
		task.setRemarks(form.getRemarks());
		System.out.println(task);
		taskService.regist(task);
		
		redirectAttributes.addFlashAttribute("msg", "(タスク登録)");
		
		return "redirect:/task-complete";
	}
}
