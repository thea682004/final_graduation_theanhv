package com.app.service;

import java.util.Date;
import java.util.List;
import com.app.entity.Order;
import com.app.entity.OrderDetail;
import com.fasterxml.jackson.databind.JsonNode;

public interface OrderService {
	//post data through out RestAPI
	Order create(JsonNode orderData);

	//order details follow orderId
	Order findById(Long id);
	
	List<Order> findAll();
	//list all orders follow username
	List<Order> findByUsername(String username, String status);

	Object getTotalAmountCurrentDay();

	Integer totalProductSold();

	List<Object[]> top5ProductAWeek();

	List<OrderDetail> getOrderDetail(Long orderId);

	List<Object[]> getRevenueForAWeek(Date start, Date end);

	Order updateOder(Order order);

}
