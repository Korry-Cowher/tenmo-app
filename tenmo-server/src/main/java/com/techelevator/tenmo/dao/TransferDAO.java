package com.techelevator.tenmo.dao;

import java.util.List;

import com.techelevator.tenmo.model.Transfer;

public interface TransferDAO {

	List<Transfer> list();
	
	Transfer getTransfer(int transferId);
	
	Transfer create(Transfer transfer);
	
	void delete(int id);

	void executeTransfer(Transfer transfer);
	
	List<Transfer> getPendingRequests(Integer userId);
	
	void executeRequest(Transfer transfer);
	
	void rejectRequest(int id);
	
}
