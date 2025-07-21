package com.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.entity.Category;


@Repository
public interface CategoryDAO extends JpaRepository<Category, Integer>{
  @Query("SELECT c, COUNT(p) FROM Category c LEFT JOIN c.products p GROUP BY c")
  List<Object[]> findAllCategoriesWithProductCount();
}
