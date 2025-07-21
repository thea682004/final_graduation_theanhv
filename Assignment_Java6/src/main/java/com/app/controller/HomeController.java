package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.dao.ProductDAO;
import com.app.entity.Product;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@Autowired
	private ProductDAO pdao;

	@RequestMapping("/")
	public String index(Model model) {
		List<Product> newArrivals = pdao.findTop5ByOrderByCreatedDateDesc();
		model.addAttribute("newArrivals", newArrivals);
		return "layout/index";
	}

	@RequestMapping("/about")
	public String about() {
		return "layout/about";
	}

	@RequestMapping("/contact")
	public String contact() {
		return "layout/contact";
	}

	@RequestMapping("/product")
	public String product() {
		return "layout/product";
	}

	@RequestMapping("/admin")
	public String admin() {
		return "redirect:/admin/index.html";
	}

	@GetMapping("/my-profile")
	public String getProfile() {
		return "login/user";
	}

	@GetMapping("info")
	public String getInfo() {
		return "layout/info";
	}
}
