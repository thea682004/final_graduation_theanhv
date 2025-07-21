package com.app.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.entity.Color;
import com.app.service.ColorService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/colors")
public class ColorRestController {

	@Autowired
	ColorService colorService;

	@GetMapping()
	public List<Color> getAll() {
		return colorService.findAll();
	}

	@PostMapping()
	public Color create(@RequestBody Color color) {
		return colorService.create(color);
	}

	@GetMapping("count")
	public List<Object[]> count() {
		return colorService.getAllColorsWithCount();
	}
}
