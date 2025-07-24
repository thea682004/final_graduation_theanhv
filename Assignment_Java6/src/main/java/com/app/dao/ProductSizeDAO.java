package com.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.entity.ProductSize;

@Repository
public interface ProductSizeDAO extends JpaRepository<ProductSize, Integer> {
  @Query("SELECT p FROM ProductSize p WHERE p.product.id = ?1 ORDER BY p.size ASC")
  List<ProductSize> findByProductId(Long productId);

  @Query("SELECT ps FROM ProductSize ps WHERE ps.product.id = :productId AND ps.size = :size")
    ProductSize findByProductIdAndSize(@Param("productId") Long productId, @Param("size") String size);

  @Query("SELECT p FROM ProductSize p WHERE p.size LIKE ?1 AND p.product.id = ?2")
  ProductSize findBySizeAndProductId(String size, Long productId);

}
