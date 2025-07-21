package com.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.entity.FeedBacks;

@Repository
public interface FeedbackDAO extends JpaRepository<FeedBacks, Integer>{

  @Query("SELECT f FROM FeedBacks f JOIN FETCH f.account WHERE f.product.id = ?1")
  List<FeedBacks> findByProductId(int productId);
}
