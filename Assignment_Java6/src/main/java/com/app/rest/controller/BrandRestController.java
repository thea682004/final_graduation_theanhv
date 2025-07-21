package com.app.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.entity.Brand;
import com.app.service.BrandService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/brands")
public class BrandRestController {

	@Autowired
	BrandService brandService;

	@GetMapping()
	public List<Brand> getAll() {
		return brandService.findAll();
	}

	@PostMapping()
	public Brand create(@RequestBody Brand brand) {
		return brandService.create(brand);
	}

	@GetMapping("count")
	public List<Object[]> count() {
		return brandService.getAllBrandsWithCount();
	}
}
