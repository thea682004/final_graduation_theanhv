package com.app.service.impl;

import com.app.model.UpdateQuantityRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dao.ProductSizeDAO;
import com.app.entity.ProductSize;
import com.app.service.ProductSizeService;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
public class ProductSizeServiceImpl implements ProductSizeService{
	@Autowired
	ProductSizeDAO pzdao;

	@Override
	public ProductSize create(ProductSize productSize) {
		return pzdao.save(productSize);
	}

	@Override
	public Integer getQuantityByProductAndSize(Long id, String size) {
		Optional<ProductSize> productSizeOptional = pzdao.findByProductAndSize(id, size);
		if(productSizeOptional.isPresent()) {
			ProductSize productSize = productSizeOptional.get();
			return productSize.getQuantity();
		}
		return null;
	}

	@Override
	public ProductSize updateSize(@RequestBody UpdateQuantityRequest request) {
		Optional<ProductSize> productSizeOptional = pzdao.findByProductAndSize(request.getProductId(), request.getSize());
		if(productSizeOptional.isPresent()) {
			ProductSize productSize = productSizeOptional.get();

			if(request.isIncrease()) {
				productSize.setQuantity(productSize.getQuantity() + request.getQuantity());
            } else {
				if(productSize.getQuantity() < request.getQuantity()) {
					throw new RuntimeException("Quantity exceeds maximum quantity");
				}
				productSize.setQuantity(productSize.getQuantity() - request.getQuantity());
            }
            pzdao.save(productSize);
            return productSize;
        }
		return null;
	}

}
