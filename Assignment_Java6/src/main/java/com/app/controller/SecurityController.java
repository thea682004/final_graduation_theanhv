package com.app.controller;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.entity.Account;
import com.app.entity.Authority;
import com.app.service.AccountRegistrationService;
import com.app.service.AccountService;
import com.app.service.AuthorityService;
import com.app.service.RoleService;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SecurityController {

	@Autowired
	private AccountService accountService;

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private AccountRegistrationService accService;

	@GetMapping("login/form")
	public String signUp(Model model) {
		model.addAttribute("account", new Account());
		return "login/loginform-d";
	}

	@PostMapping("/login/signup")
	public String postMethodName(@ModelAttribute Account account, Model model) {
		if (!account.getPassword().equals(account.getConfirmPassword())) {
			return "redirect:/login/signup?error=passwordMismatch";
		}
		account.setCreateDate(new Date());
		Authority auth = new Authority();
		auth.setAccount(account);

		auth.setRole(roleService.findById("CUS"));

		accountService.signUP(account);
		authorityService.create(auth);
		accService.registerNewAccount(account);
		model.addAttribute("messageSignup", "Sign-up successfully!");
		return "login/loginform-d";
	}

	@RequestMapping("/login/success")
	public String login(Model model) {
		model.addAttribute("message", "Success!");
		return "forward:/";
	}

	@RequestMapping("/login/error")
	public String error(Model model) {
		model.addAttribute("message", "Invaid username or password!");
		return "forward:/login/form";
	}

	@RequestMapping("/logout/success")
	public String logout(Model model) {
		model.addAttribute("message", "Logout success!");
		return "forward:/";
	}

	@RequestMapping("/unauthoried")
	public String unauthoried() {
		return "forward:/login/form";
	}

	@ResponseBody
	@GetMapping("/rest/authorities")
	public String abc() {
		return "Hello Director";
	}
}
