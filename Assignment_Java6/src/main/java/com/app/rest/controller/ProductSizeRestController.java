package com.app.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dao.ProductSizeDAO;
import com.app.entity.ProductSize;
import com.app.service.ProductSizeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@CrossOrigin("*")
@RestController
@RequestMapping("/rest/productsize")
public class ProductSizeRestController {
  @Autowired
	ProductSizeService productSizeService;

  @Autowired
  ProductSizeDAO dao;

  @PostMapping()
	public ProductSize create(@RequestBody ProductSize productsize) {
		return productSizeService.create(productsize);
	}

  @GetMapping()
  public List<ProductSize> getMethodName(@RequestParam String id) {
      return dao.findByProductId(Long.parseLong(id));
  }

  @PutMapping("update")
	public ProductSize update(@RequestBody ProductSize productsize) {
		return dao.save(productsize);
	}

  @DeleteMapping("delete/{id}")
  public void delete(@PathVariable("id") Integer id) {
    dao.deleteById(id);
  }
}
