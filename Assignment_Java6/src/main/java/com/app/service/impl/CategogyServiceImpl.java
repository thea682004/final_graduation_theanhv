package com.app.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dao.CategoryDAO;
import com.app.dao.ProductDAO;
import com.app.entity.Category;
import com.app.service.CategoryService;


@Service
public class CategogyServiceImpl implements CategoryService{

	@Autowired
	CategoryDAO cdao;

	@Autowired
	ProductDAO pdao;

	@Override
	public List<Category> findAll() {
		// TODO Auto-generated method stub
		return cdao.findAll();
	}

	@Override
	public Category create(Category category) {
		return cdao.save(category);
	}

	@Override
	public List<Object[]> getAllCategoriesWithCount() {
		return cdao.findAllCategoriesWithProductCount();
	}

	@Override
	public Map<Category, Long> countProductsByCategory() {
		List<Object[]> result = cdao.findAllCategoriesWithProductCount();
		Map<Category, Long> countMap = new HashMap<>();
		for (Object[] objects : result) {
				Category category = (Category) objects[0];
				Long count = (Long) objects[1];
				countMap.put(category, count);
		}
		return countMap;
	}
}
