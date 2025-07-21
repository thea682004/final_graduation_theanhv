package com.app.service;

import java.util.List;
import java.util.Map;

import com.app.entity.Brand;

public interface BrandService {

	List<Brand> findAll();

	Brand create(Brand brand);

	List<Object[]> getAllBrandsWithCount();
	
	Map<Brand, Long> countProductsByBrand();
}
