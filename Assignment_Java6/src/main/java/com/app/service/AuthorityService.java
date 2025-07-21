package com.app.service;

import java.util.List;

import com.app.entity.Authority;

public interface AuthorityService {

	List<String> findRoles(String username);

	List<Authority> findAuthoritiesOfAdministrators();

	List<Authority> findAll();

	Authority create(Authority auth);

	void delete(Integer id);
}
