package com.app.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.entity.Category;
import com.app.entity.Product;
import com.app.entity.ProductSize;

@Repository
public interface ProductDAO extends JpaRepository<Product, Integer> {
	Page<Product> findByCategoryId(String categoryId, Pageable pageable);

	@Query(value = "select p from Product p where p.name like ?1")
	Page<Product> findByName(String name, Pageable pageable);

	Page<Product> findByPriceBetween(double minPrice, double maxPrice, Pageable pageable);

	Page<Product> findByPriceBetweenAndCategoryIn(double minPrice, double maxPrice, List<Category> categories,
			Pageable pageable);

	@Query("SELECT p.category.id, p.category.name, COUNT(p) FROM Product p GROUP BY p.category.id, p.category.name")
	List<Object[]> countProductsByCategory();

	@Query("SELECT ps FROM ProductSize ps WHERE ps.product.id = ?1")
	List<ProductSize> findSizesByProductId(Integer productId);

	@Query(value = "SELECT p FROM Product p ORDER BY p.createDate DESC")
	List<Product> findTop5ByOrderByCreatedDateDesc();

}
