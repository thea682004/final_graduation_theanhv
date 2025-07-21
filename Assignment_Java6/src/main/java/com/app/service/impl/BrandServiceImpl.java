package com.app.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dao.BrandDAO;
import com.app.dao.ProductDAO;
import com.app.entity.Brand;
import com.app.service.BrandService;

@Service
public class BrandServiceImpl implements BrandService {

	@Autowired
	BrandDAO bdao;

	@Autowired
	ProductDAO pdao;

	@Override
	public List<Brand> findAll() {
		// TODO Auto-generated method stub
		return bdao.findAll();
	}

	@Override
	public Brand create(Brand brand) {
		return bdao.save(brand);
	}

	@Override
	public List<Object[]> getAllBrandsWithCount() {
		return bdao.findAllBrandsWithProductCount();
	}

	@Override
	public Map<Brand, Long> countProductsByBrand() {
		List<Object[]> result = bdao.findAllBrandsWithProductCount();
		Map<Brand, Long> countMap = new HashMap<>();
		for (Object[] objects : result) {
			Brand brand = (Brand) objects[0];
			Long count = (Long) objects[1];
			countMap.put(brand, count);
		}
		return countMap;
	}
}
