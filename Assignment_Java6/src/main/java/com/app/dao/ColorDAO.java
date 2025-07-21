package com.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.entity.Color;

@Repository
public interface ColorDAO extends JpaRepository<Color, Integer> {
  @Query("SELECT c, COUNT(p) FROM Color c LEFT JOIN c.products p GROUP BY c")
  List<Object[]> findAllColorsWithProductCount();
}
