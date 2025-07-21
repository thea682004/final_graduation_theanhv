package com.app.rest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.entity.Account;
import com.app.service.AccountRegistrationService;
import com.app.service.AccountService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/accounts")
public class AccountRestController {

	
	@Autowired
	AccountService accountService;

	@Autowired
	AccountRegistrationService accService;

	
	@GetMapping
	public List<Account> getAccounts(@RequestParam("admin")Optional<Boolean> admin){
		if (admin.orElse(false)) {
			return accountService.getAdministrators();
		}
		return accountService.findAll();
	}

	@GetMapping("/signupCurrentDay")
	public Integer signupCurrentDay() {
		return accountService.countUserCurrentDay();
	}

	@GetMapping("profile")
	public Account getMethodName(@RequestParam Optional<String> username) {
		return accountService.findById(username.get());
	}

	@PutMapping("update")
	public void updateUser(@RequestBody Account account) {
		 accountService.signUP(account);
		 accService.updateAccount(account);
	}
}
