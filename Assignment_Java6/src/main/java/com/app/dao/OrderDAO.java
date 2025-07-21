package com.app.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.entity.Order;

@Repository
public interface OrderDAO extends JpaRepository<Order, Long> {
	List<Order> findAllByOrderByIdDesc();

	@Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status != 'cancel'")
	Double getTotalRevenue();

	@Query(value = "select o from Order o where o.account.username = ?1 and o.status = ?2 ORDER BY o.id DESC")
	List<Order> findByUsername(String username, String status);

	@Query("SELECT SUM(o.totalAmount) AS totalAmount, COUNT(o.id) AS orderCount FROM Order o WHERE o.createDate = ?1 AND o.status IN ('confirmed', 'delivering', 'completed')")
	Object totalAmountCurrentDay(Date date);

	@Query("SELECT o.createDate, SUM(o.totalAmount) FROM Order o " +
			"WHERE o.createDate BETWEEN ?1 AND ?2 AND o.status != 'cancel' " +
			"GROUP BY o.createDate")
	List<Object[]> getRevenueByDateRange(Date startDate, Date endDate);

	@Query("SELECT o FROM Order o WHERE CAST(o.id AS string) LIKE CONCAT(?1, '%')")
	List<Order> findByIdLike(String id);

	// Lọc đơn hàng theo khoảng thời gian
	List<Order> findByCreateDateBetween(Date from, Date to);

	// (tuỳ chọn) Tìm theo trạng thái đơn hàng
	List<Order> findByStatus(String status);

	Optional<Order> findById(Integer id);
}
