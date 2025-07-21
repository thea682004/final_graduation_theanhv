package com.app.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dao.OrderDAO;
import com.app.dao.OrderDetailDAO;
import com.app.dao.ProductSizeDAO;
import com.app.entity.Order;
import com.app.entity.OrderDetail;
import com.app.entity.ProductSize;
import com.app.service.OrderService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	OrderDAO dao;

	@Autowired
	OrderDetailDAO ddao;
	
	LocalDate endDate = LocalDate.now();
	LocalDate startDate = endDate.minusDays(6);
	Date start =  Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	Date end =  Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

	@Autowired
ProductSizeDAO psizeDAO;

public Order create(JsonNode orderData) {
	ObjectMapper mapper = new ObjectMapper();
	Order order = mapper.convertValue(orderData, Order.class);
	dao.save(order);

	TypeReference<List<OrderDetail>> type = new TypeReference<List<OrderDetail>>() {};
	List<OrderDetail> details = mapper.convertValue(orderData.get("orderDetails"), type).stream()
			.peek(d -> d.setOrder(order))
			.collect(Collectors.toList());
	ddao.saveAll(details);

	// Trừ kho
	details.forEach(detail -> {
		ProductSize ps = psizeDAO.findByProductIdAndSize(detail.getProduct().getId(), detail.getSize());
		if (ps != null) {
			ps.setQuantity(ps.getQuantity() - detail.getQuantity());
			psizeDAO.save(ps);
		}
	});

	return order;
}


	@Override
	public Order findById(Long id) {
		return dao.findById(id).get();
	}

	@Override
	public List<Order> findByUsername(String username, String status) {
		return dao.findByUsername(username, status);
	}

	@Override
	public Object getTotalAmountCurrentDay() {
		return dao.totalAmountCurrentDay(new Date());
	}

	@Override
	public Integer totalProductSold() {
		return ddao.totalProductSoldCurrentDay(new Date());
	}

	@Override
	public List<Object[]> top5ProductAWeek() {
		return ddao.getTop5SellingProductsForWeek(start, end);
	}

	@Override
	public List<Order> findAll(){
		return dao.findAll();
	}

	@Override
	public List<OrderDetail> getOrderDetail(Long orderId) {
		return ddao.findByOrderId(orderId);
	}

	@Override
	public List<Object[]> getRevenueForAWeek(Date start, Date end) {
		return dao.getRevenueByDateRange(start, end);
	}
}
