package com.app.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dao.AccountDAO;
import com.app.entity.Account;
import com.app.service.AccountService;


@Service
public class AccountSeviceImpl implements AccountService {

	@Autowired
	AccountDAO adao;

	@Override
	public Account findById(String username) {
		// TODO Auto-generated method stub
		return adao.findById(username).get();
	}

	@Override
	public List<Account> findAll() {
		// TODO Auto-generated method stub
		return adao.findAll();
	}

	@Override
	public List<Account> getAdministrators() {
		// TODO Auto-generated method stub
		return adao.getAdministrators();
	}

	@Override
	public Account signUP(Account account) {
		// TODO Auto-generated method stub
		return adao.save(account);
	}

	@Override
	public Integer countUserCurrentDay() {
		return adao.numberOfUserCurrentDay(new Date());
	}
}
