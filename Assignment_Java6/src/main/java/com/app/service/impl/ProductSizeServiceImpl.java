package com.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dao.ProductSizeDAO;
import com.app.entity.ProductSize;
import com.app.service.ProductSizeService;

@Service
public class ProductSizeServiceImpl implements ProductSizeService{
	@Autowired
	ProductSizeDAO pzdao;

	@Override
	public ProductSize create(ProductSize productSize) {
		return pzdao.save(productSize);
	}

}
