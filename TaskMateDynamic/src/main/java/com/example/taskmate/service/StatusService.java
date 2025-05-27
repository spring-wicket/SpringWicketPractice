package com.example.taskmate.service;

import java.util.List;

import com.example.taskmate.entity.Status;

public interface StatusService {
	
	public List<Status> findAll();
	
	public Status findByCode(String statusCode);

}
