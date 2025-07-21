package com.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dao.AccountDAO;
import com.app.dao.AuthorityDAO;
import com.app.entity.Account;
import com.app.entity.Authority;
import com.app.service.AuthorityService;

@Service
public class AuthorityServiceImpl implements AuthorityService{
	
	@Autowired
	AuthorityDAO audao;
	
	@Autowired
	AccountDAO acdao;

	@Override
	public List<String> findRoles(String username) {
		return audao.findRoles(username);
	}

	@Override
	public List<Authority> findAuthoritiesOfAdministrators() {
		List<Account> accounts = acdao.getAdministrators();
		return audao.authoritiesOf(accounts);
	}

	@Override
	public List<Authority> findAll() {
		// TODO Auto-generated method stub
		return audao.findAll();
	}

	@Override
	public Authority create(Authority auth) {
		// TODO Auto-generated method stub
		return audao.save(auth);
	}

	@Override
	public void delete(Integer id) {
		audao.deleteById(id);
	}
}
