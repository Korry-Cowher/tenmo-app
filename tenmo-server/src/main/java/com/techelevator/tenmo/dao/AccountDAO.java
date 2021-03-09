package com.techelevator.tenmo.dao;

import java.util.List;

import com.techelevator.tenmo.model.Account;

public interface AccountDAO {

	List<Account> list();
	
	Account getAccount(int account_id);
	
	Account create(Account account);
	
	void updateBalance(int account_id, double newBalance);
	
	void delete(int account_id);
	
	Account getAccountByUserId(int userId);
	
}
