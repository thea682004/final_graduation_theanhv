package com.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.entity.Account;
import com.app.entity.Authority;


@Repository
public interface AuthorityDAO extends JpaRepository<Authority, Integer>{
	@Query(value = "select au.role.id from Authority au where au.account.username = ?1")
	public List<String> findRoles(String username);

	@Query("select distinct a from Authority a where a.account in ?1")
	public List<Authority> authoritiesOf(List<Account> accounts);
}
