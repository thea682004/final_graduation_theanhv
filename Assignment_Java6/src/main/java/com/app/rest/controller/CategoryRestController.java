package com.app.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.entity.Category;
import com.app.service.CategoryService;


@CrossOrigin("*")
@RestController
@RequestMapping("/rest/categories")
public class CategoryRestController {

	@Autowired
	CategoryService categoryService;

	@GetMapping()
	public List<Category> getAll() {
		return categoryService.findAll();
	}

	@PostMapping()
	public Category create(@RequestBody Category category) {
		return categoryService.create(category);
	}

	@GetMapping("count")
	public List<Object[]> count() {
		return categoryService.getAllCategoriesWithCount();
	}
}
