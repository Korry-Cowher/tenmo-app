package com.techelevator.tenmo.model;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class Account {
	
	@NotNull(message = "Input cannot be null")
	@Positive(message = "account_id must be a positive int")
	private int account_id;
	
	@NotNull(message = "Input cannot be null")
	@Positive(message = "user_id must be a positive int")
	private int user_id;
	
	@NotNull(message = "Input cannot be null")
	@Positive(message = "account_id must be a positive double")
	private double balance;
	
	public Account() {
	}

	public int getAccount_id() {
		return account_id;
	}

	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	

}
