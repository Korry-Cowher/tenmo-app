package com.techelevator.tenmo.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Account;

public class AccountService {

	private String BASE_URL;
	private RestTemplate restTemplate = new RestTemplate();
	
	public AccountService(String url) {
		this.BASE_URL = url;
	}
	
	public Account[] getAll(String authToken) {
		Account[] accounts = null;
		try {
			accounts = restTemplate.exchange(BASE_URL + "/accounts", HttpMethod.GET, makeAuthEntity(authToken), Account[].class).getBody();
		} catch (RestClientResponseException e) {
			e.printStackTrace();
		}
		return accounts;
	}
	
	public Account getAccount(int id, String authToken) {
		Account myAccount = new Account();
		try {
			myAccount = restTemplate.exchange(BASE_URL +"/accounts/" + id, HttpMethod.GET, makeAuthEntity(authToken), Account.class).getBody();
		} catch (RestClientResponseException e) {
			e.printStackTrace();
		}
		return myAccount;
	}
	
	public Account getAccountByUserId(int id, String authToken) {
		Account myAccount = new Account();
		try {
			myAccount = restTemplate.exchange(BASE_URL +"/useraccount/" + id, HttpMethod.GET, makeAuthEntity(authToken), Account.class).getBody();
		} catch (RestClientResponseException e) {
			e.printStackTrace();
		}
		return myAccount;
	}
	private HttpEntity makeAuthEntity(String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(authToken);
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}
	
    private HttpEntity<Account> makeTransferEntity(Account account, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        HttpEntity<Account> entity = new HttpEntity<>(account, headers);
        return entity;
    }
}
