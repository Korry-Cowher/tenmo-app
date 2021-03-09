package com.techelevator.tenmo.services;

import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class UserService {
	
	private String BASE_URL;
	private RestTemplate restTemplate = new RestTemplate();
	
	public UserService(String url) {
		this.BASE_URL = url;
	}
	
	public String getUsernameById(Integer id) {
		String username = "";
		try {
			username = restTemplate.getForObject(BASE_URL + "/users/" + id, String.class);
		} catch (RestClientResponseException e) {
			e.printStackTrace();
		}
		return username;
	}

}
