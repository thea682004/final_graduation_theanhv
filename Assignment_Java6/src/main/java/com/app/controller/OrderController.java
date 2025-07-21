package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("order")
public class OrderController {

	@Autowired
	OrderService orderService;

	@RequestMapping("list")
	public String list(Model model, HttpServletRequest request) {
		model.addAttribute("orders", orderService.findByUsername(request.getRemoteUser(), "confirmed"));
		model.addAttribute("deliveringOrders", orderService.findByUsername(request.getRemoteUser(), "delivering"));
		model.addAttribute("completedOrders", orderService.findByUsername(request.getRemoteUser(), "completed"));
		model.addAttribute("cancelOrders", orderService.findByUsername(request.getRemoteUser(), "cancel"));
		return "order/list";
	}

	@RequestMapping("checkout")
	public String checkout() {
		return "order/checkout";
	}

	@RequestMapping("detail/{id}")
	public String detail(@PathVariable("id") Long id, Model model) {
		model.addAttribute("order", orderService.findById(id));
		return "order/detail";
	}
}
