package com.app.service;

import java.util.List;
import java.util.Map;

import com.app.entity.Category;

public interface CategoryService {

	List<Category> findAll();

	Category create(Category category);

	List<Object[]> getAllCategoriesWithCount();
	
	Map<Category, Long> countProductsByCategory();
}
