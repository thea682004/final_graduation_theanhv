package com.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dao.ProductDAO;
import com.app.entity.Product;
import com.app.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{

	@Autowired
	ProductDAO pdao;

	@Override
	public List<Product> findAll() {
		// TODO Auto-generated method stub
		return pdao.findAll();
	}

	@Override
	public Product findById(Integer id) {
		// TODO Auto-generated method stub
		return pdao.findById(id).get();
	}


	@Override
	public Product create(Product product) {
		// TODO Auto-generated method stub
		return pdao.save(product);
	}


	@Override
	public Product update(Product product) {
		// TODO Auto-generated method stub
		return pdao.save(product);
	}


	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		pdao.deleteById(id);
	}

}
