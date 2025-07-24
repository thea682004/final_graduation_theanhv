package com.app.rest.controller;

import java.util.Date;
import java.util.List;
// import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dao.OrderDAO;
import com.app.dao.OrderDetailDAO;
import com.app.entity.Order;
import com.app.entity.OrderDetail;
import com.app.service.OrderService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/orders")
public class OrderRestController {

	@Autowired
	OrderService orderService;

	@Autowired
	OrderDetailDAO ddao;

	@Autowired
	OrderDAO dao;

	@PostMapping()
	public Order create(@RequestBody JsonNode orderData) {
		return orderService.create(orderData);
	}

	@GetMapping("/all")
	public List<Order> getAll() {
		return dao.findAllByOrderByIdDesc();
	}

	@GetMapping("{id}")
	public Order getOrder(@PathVariable Long id) {
		return dao.findById(id).get();
	}

	@GetMapping("findId")
	public List<Order> getOrderId(@RequestParam String id) {
		return dao.findByIdLike(id);
	}

	@GetMapping("totalAmountCurrentDay")
	public Object totalAmountCurrentDay() {
		return orderService.getTotalAmountCurrentDay();
	}

	@GetMapping("totalProductSold")
	public Integer totalProductSold() {
		return orderService.totalProductSold();
	}

	@GetMapping("top5ProductAWeek")
	public List<Object[]> getMethodName() {
		return orderService.top5ProductAWeek();
	}

	@GetMapping("detail")
	public List<OrderDetail> getMethodName(@RequestParam String id) {
		return orderService.getOrderDetail(Long.parseLong(id));
	}

	@GetMapping("revenueAWeek")
	public List<Object[]> getRevenueForAWeek(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) {
		return orderService.getRevenueForAWeek(start, end);
	}

	@PutMapping("update")
	public Order update(@RequestBody Order order) {
		return orderService.updateOder(order);
	}

	@PutMapping("updateDetail")
	public OrderDetail putMethodName(@RequestBody OrderDetail orderDetail) {
		return ddao.save(orderDetail);
	}

}
