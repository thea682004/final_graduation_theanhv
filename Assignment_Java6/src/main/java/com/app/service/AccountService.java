package com.app.service;

import java.util.List;

import com.app.entity.Account;

public interface AccountService {

	Account findById(String username);
	
	List<Account> findAll();

	List<Account> getAdministrators();

	Account signUP(Account account);

	Integer countUserCurrentDay();
}
