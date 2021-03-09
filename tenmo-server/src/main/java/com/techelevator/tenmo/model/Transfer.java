package com.techelevator.tenmo.model;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class Transfer {
	
	
	public int transferId;
	
	@NotNull(message = "Input cannot be null")
	@Positive(message = "transferTypeId must be a positive int")
	public int transferTypeId;
	
	@NotNull(message = "Input cannot be null")
	@Positive(message = "transferStatusId must be a positive int")
	public int transferStatusId;
	
	@NotNull(message = "Input cannot be null")
	@Positive(message = "accountTo must be a positive int")
	public int accountTo;
	
	@NotNull(message = "Input cannot be null")
	@Positive(message = "accountFrom must be a positive int")
	public int accountFrom;
	
	@NotNull(message = "Input cannot be null")
	@Positive(message = "Transfer must be a positive double")
	public double amount;
	
	public Transfer() {
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
	
	
}
