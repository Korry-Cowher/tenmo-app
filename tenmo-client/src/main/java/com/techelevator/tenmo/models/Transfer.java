package com.techelevator.tenmo.models;

public class Transfer {
	public int transferId;
	public int transferTypeId;
	public int transferStatusId;
	public int accountTo;
	public int accountFrom;
	public double amount;
	
	public Transfer() {
		
	}
	
	public Transfer(int transferId, int transferTypeId, int transferStatusId, int accountTo, int accountFrom, double amount) {
		this.transferId = transferId;
		this.transferTypeId = transferTypeId;
		this.transferStatusId = transferStatusId;
		this.accountTo = accountTo;
		this.accountFrom = accountFrom;
		this.amount = amount;
	}

	public int getTransferId() {
		return transferId;
	}

	public void setTransferId(int transferId) {
		this.transferId = transferId;
	}

	public int getTransferTypeId() {
		return transferTypeId;
	}

	public void setTransferTypeId(int transferTypeId) {
		this.transferTypeId = transferTypeId;
	}

	public int getTransferStatusId() {
		return transferStatusId;
	}

	public void setTransferStatusId(int transferStatusId) {
		this.transferStatusId = transferStatusId;
	}

	public int getAccountTo() {
		return accountTo;
	}

	public void setAccountTo(int accountTo) {
		this.accountTo = accountTo;
	}

	public int getAccountFrom() {
		return accountFrom;
	}

	public void setAccountFrom(int accountFrom) {
		this.accountFrom = accountFrom;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public String transferToString(Transfer transfer) {
		User user = new User();
		String transferType = "";
		String transferStatus = "";
		String transferString = "";
		if (transfer.getTransferTypeId() == 1) {
			transferType = "Request";
		} else transferType = "Send";
		if (transfer.getTransferStatusId() == 1) {
			transferStatus = "Pending";
		} else if (transfer.getTransferStatusId() == 2) {
			transferStatus = "Approved";
		} else transferStatus = "Rejected";
		
		
		
		transferString = "Id: " + transfer.getTransferId() + "\nType: " + transferType + 
						 "\nStatus: " + transferStatus + "\nTo: " + transfer.getAccountTo() + "\nFrom: " + transfer.getAccountFrom()+
						 "\nAmount: $" + transfer.getAmount();
		/* 			System.out.println("ID: " + myTransfer.getTransferId());
			System.out.println("From: " + myTransfer.getAccountFrom());
			System.out.println("To: " + myTransfer.getAccountTo());
			System.out.println("Type: " + myTransfer.get);
			System.out.println("Status: ");
			System.out.println("Amount: $");
			*/
		return transferString;
	}
	
}
