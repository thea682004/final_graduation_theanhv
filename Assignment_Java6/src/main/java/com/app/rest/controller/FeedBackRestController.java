package com.app.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dao.FeedbackDAO;
import com.app.entity.FeedBacks;
import org.springframework.web.bind.annotation.RequestParam;


@CrossOrigin("*")
@RestController
@RequestMapping("/rest/feedbacks")
public class FeedBackRestController {

	@Autowired
	FeedbackDAO fdao;

	@PostMapping("send")
	public FeedBacks create(@RequestBody FeedBacks feedBacks) {
		return fdao.save(feedBacks);
	}

	@GetMapping("")
	public List<FeedBacks> getFeedBackByProductId(@RequestParam Integer id) {
		return fdao.findByProductId(id);
	}
}
