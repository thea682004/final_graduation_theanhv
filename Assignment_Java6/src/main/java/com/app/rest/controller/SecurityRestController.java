package com.app.rest.controller;

import com.app.entity.Account;
import com.app.entity.Authority;
import com.app.service.AccountRegistrationService;
import com.app.service.AccountService;
import com.app.service.AuthorityService;
import com.app.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class SecurityRestController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private AccountRegistrationService accService;

	@PostMapping("/login/create-customer")
	public Account createCustomer(@RequestBody Account account) {
		account.setCreateDate(new Date());
		Authority auth = new Authority();
		auth.setAccount(account);

		auth.setRole(roleService.findById("CUS"));

		accountService.signUP(account);
		authorityService.create(auth);
		accService.registerNewAccount(account);
		return account;
	}

}
