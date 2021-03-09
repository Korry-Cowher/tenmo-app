package com.techelevator.tenmo.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountDAO;
import com.techelevator.tenmo.dao.JDBCAccountDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.dao.UserSqlDAO;

@RestController
public class UserController {
	
	private UserDAO dao;
	private JdbcTemplate jdbcTemplate;
	private SingleConnectionDataSource dataSource;
	
	private SingleConnectionDataSource setupDataSource() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");
        return dataSource;
	}

	
	public UserController() {
		this.jdbcTemplate = new JdbcTemplate(setupDataSource());
		this.dao = new UserSqlDAO(jdbcTemplate);
	}

	
    @RequestMapping(path = "/users/{id}", method = RequestMethod.GET)
    public String getUsernameById(@PathVariable int id) {
    	return dao.findUsernameById(id);
    }

}
