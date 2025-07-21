package com.app.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dao.ColorDAO;
import com.app.dao.ProductDAO;
import com.app.entity.Color;
import com.app.service.ColorService;

@Service
public class ColorServiceImpl implements ColorService {

	@Autowired
	ColorDAO cdao;

	@Autowired
	ProductDAO pdao;

	@Override
	public List<Color> findAll() {
		// TODO Auto-generated method stub
		return cdao.findAll();
	}

	@Override
	public Color create(Color color) {
		return cdao.save(color);
	}

	@Override
	public List<Object[]> getAllColorsWithCount() {
		return cdao.findAllColorsWithProductCount();
	}

	@Override
	public Map<Color, Long> countProductsByColor() {
		List<Object[]> result = cdao.findAllColorsWithProductCount();
		Map<Color, Long> countMap = new HashMap<>();
		for (Object[] objects : result) {
			Color color = (Color) objects[0];
			Long count = (Long) objects[1];
			countMap.put(color, count);
		}
		return countMap;
	}
}
