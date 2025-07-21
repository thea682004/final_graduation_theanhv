package com.app.service;

import java.util.List;
import java.util.Map;

import com.app.entity.Color;

public interface ColorService {

	List<Color> findAll();

	Color create(Color color);

	List<Object[]> getAllColorsWithCount();

	Map<Color, Long> countProductsByColor();
}
