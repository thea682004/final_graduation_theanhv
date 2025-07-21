package com.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dao.RoleDAO;
import com.app.entity.Role;
import com.app.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService{
	
	@Autowired
	RoleDAO dao;
	@Override
	public List<Role> findAll() {
		// TODO Auto-generated method stub
		return dao.findAll();
	}

	@Override
	public Role findById(String id) {
		return dao.getReferenceById(id);
	}

}
