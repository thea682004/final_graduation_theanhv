package com.app.service;

import java.util.List;
import com.app.entity.Product;

public interface ProductService {
	List<Product> findAll();

	Product findById(Integer id);

	Product create(Product product);

	Product update(Product product);

	void delete(Integer id);
}
