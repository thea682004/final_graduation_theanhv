package com.app.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.entity.Account;

@Repository
public interface AccountDAO extends JpaRepository<Account, String>{
	@Query("select distinct ar.account from Authority ar where ar.role.id in ('DIRE','STAF')")
	List<Account> getAdministrators();

	@Query("SELECT COUNT(acc.username) FROM Account acc WHERE acc.createDate = ?1")
	Integer numberOfUserCurrentDay(Date date);
}
