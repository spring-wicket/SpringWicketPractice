package com.example.wicketpractice.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Employee implements Serializable {
	private Integer id;
	private String name;

}
