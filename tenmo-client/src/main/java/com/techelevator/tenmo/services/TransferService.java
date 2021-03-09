package com.techelevator.tenmo.services;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;

public class TransferService {

	private String BASE_URL;
	private RestTemplate restTemplate = new RestTemplate();

	
	public TransferService(String url) {
		this.BASE_URL = url;
	}
	
	
	public Transfer[] getAll(String authToken) {
		Transfer[] transfers = null;
		try {
			transfers = restTemplate.exchange(BASE_URL + "/transfers", HttpMethod.GET, makeAuthEntity(authToken), Transfer[].class).getBody();
		} catch (RestClientResponseException e) {
			e.printStackTrace();
		}
		return transfers;
	}
	
	public Transfer create(Transfer transfer, String authToken) {
		try {
			restTemplate.exchange(BASE_URL + "/Createtransfer", HttpMethod.POST, makeTransferEntity(transfer, authToken), Transfer.class).getBody();
		} catch (RestClientResponseException e) {
			e.printStackTrace();
		}
		return transfer;
	}
	
	public Transfer getTransferById(int id, String authToken) {
		Transfer myTransfer = new Transfer();
		try {
			myTransfer = restTemplate.exchange(BASE_URL +"/transfers/" + id, HttpMethod.GET, makeAuthEntity(authToken), Transfer.class).getBody();
		} catch (RestClientResponseException e) {
			e.printStackTrace();
		}
		return myTransfer;
	}
	
	public boolean executeTransfer(Transfer transfer, String authToken) {
		try {
			restTemplate.exchange(BASE_URL + "/executeTransfer", HttpMethod.POST, makeTransferEntity(transfer, authToken), Boolean.class).getBody();
		} catch (RestClientResponseException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public Transfer[] getPendingRequests(Integer userId, String authToken) {
		Transfer[] transfers = null;
		try {
			transfers = restTemplate.exchange(BASE_URL + "/pendingtransfers/" + userId, HttpMethod.GET, makeAuthEntity(authToken), Transfer[].class).getBody();
		} catch (RestClientResponseException e) {
			e.printStackTrace();
		}
		return transfers;
	}
	
	public boolean executeRequest(Transfer transfer, String authToken) {
		try {
			restTemplate.exchange(BASE_URL + "/executeRequest", HttpMethod.PUT, makeTransferEntity(transfer, authToken), Boolean.class).getBody();
		} catch (RestClientResponseException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean rejectRequest(int id, String authToken) {
		try {
			restTemplate.exchange(BASE_URL + "/rejectRequest/" + id, HttpMethod.PUT, makeAuthEntity(authToken), Boolean.class).getBody();
		} catch (RestClientResponseException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private HttpEntity makeAuthEntity(String authToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(authToken);
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}
	
    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        return entity;
    }
}
