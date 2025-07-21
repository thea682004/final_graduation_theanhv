package com.app.service;

import java.util.List;

import com.app.entity.Role;

public interface RoleService {
	List<Role> findAll();
	Role findById(String id);
}
