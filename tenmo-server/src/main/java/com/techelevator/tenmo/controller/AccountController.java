package com.techelevator.tenmo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.JDBCAccountDAO;
import com.techelevator.tenmo.model.Account;

@RestController
public class AccountController {
	
	private AccountDAO dao;
	
	private SingleConnectionDataSource dataSource;
	
	private SingleConnectionDataSource setupDataSource() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");
        return dataSource;
	}
	
	public AccountController() {
		this.dao = new JDBCAccountDAO(setupDataSource());
	}
	
    @RequestMapping(path = "/accounts/{id}/balance", method = RequestMethod.GET)
    public Double getAmount(@Valid @PathVariable int id) {
    	return dao.getAccount(id).getBalance();
    }
    

    @RequestMapping(path = "/accounts", method = RequestMethod.GET)
    	public List<Account> listAllAccounts(){
    		return dao.list();
    	}
    
    @RequestMapping(path = "/accounts/{id}", method = RequestMethod.GET)
    	public Account getAccount(@Valid @PathVariable int id) {
    		return dao.getAccount(id);
    }
    
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/accounts", method = RequestMethod.POST)
    public Account createAccount(@Valid @RequestBody Account account) {
    	 dao.create(account);
    	 return account;
    }
    
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/accounts/{id}", method = RequestMethod.DELETE)
    public void deleteAccount(@Valid @PathVariable int id) {
    	dao.delete(id);
    }
    
    @RequestMapping(path = "/useraccount/{id}", method = RequestMethod.GET)
    	public Account getAccountByUserId(@Valid @PathVariable int id) {
    	return dao.getAccountByUserId(id);
    }
    
    
    
}
