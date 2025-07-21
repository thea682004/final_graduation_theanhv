package com.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.entity.Brand;

@Repository
public interface BrandDAO extends JpaRepository<Brand, Integer> {
  @Query("SELECT b, COUNT(p) FROM Brand b LEFT JOIN b.products p GROUP BY b")
  List<Object[]> findAllBrandsWithProductCount();
}
