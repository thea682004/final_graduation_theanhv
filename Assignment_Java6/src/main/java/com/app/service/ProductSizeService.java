package com.app.service;

import com.app.entity.ProductSize;
import com.app.model.UpdateQuantityRequest;
import org.springframework.web.bind.annotation.RequestBody;

public interface ProductSizeService {

	ProductSize create(ProductSize product);

	ProductSize updateSize(@RequestBody UpdateQuantityRequest request);

	Integer getQuantityByProductAndSize(Long id, String size);

}
