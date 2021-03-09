package com.techelevator.tenmo.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.tenmo.model.Transfer;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCTransferDAO implements TransferDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCTransferDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Transfer> list() {

		String sqlGetAllTransfers = "SELECT " + "transfer_id, " + "transfer_type_id, " + "transfer_status_id, "
				+ "account_from," + "account_to, " + "amount " + "FROM transfers;";

		List<Transfer> transfers = new ArrayList<>();

		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlGetAllTransfers);

		while (rowSet.next()) {
			Transfer theTransfer = mapTransferFromRowSet(rowSet);
			transfers.add(theTransfer);
		}
		return transfers;
	}

	@Override
	public Transfer getTransfer(int transferId) {
		String sqlGetSingleTransfer = "SELECT " + "transfer_id, " + "transfer_type_id, " + "transfer_status_id, "
				+ "account_from," + "account_to, " + "amount " + "FROM transfers " + "WHERE transfer_Id = ?;";

		Transfer theTransfer = new Transfer();
		
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlGetSingleTransfer, transferId);

		if(rowSet.next()) {
			theTransfer = mapTransferFromRowSet(rowSet);
		}
		return theTransfer;
	}

	@Override
	public Transfer create(Transfer transfer) {
		String sqlAddTransfer = "INSERT INTO transfers(transfer_id,transfer_type_id,transfer_status_id,account_from,account_to,amount) "
								+ "VALUES (?,?,?,?,?,?); ";
		transfer.setTransferId((int)getNextIdFromSequence());
		
		jdbcTemplate.update(sqlAddTransfer, 
								transfer.getTransferId(), 
								transfer.getTransferTypeId(),
								transfer.getTransferStatusId(),
								transfer.getAccountFrom(),
								transfer.getAccountTo(),
								transfer.getAmount());
		return transfer;
	}

	@Override
	public void executeTransfer(Transfer transfer) {
		String sqlNewTransfer = "INSERT INTO transfers(transfer_id,transfer_type_id,transfer_status_id,account_from,account_to,amount) "
							  + " VALUES (?,?,?,?,?,?);";
		transfer.setTransferId((int)getNextIdFromSequence());
		
		jdbcTemplate.update(sqlNewTransfer, transfer.getTransferId(), 							
											transfer.getTransferTypeId(),
											transfer.getTransferStatusId(),
											transfer.getAccountFrom(),
											transfer.getAccountTo(),
											transfer.getAmount());

		transferring(transfer);
		}
	
	@Override
	public void executeRequest(Transfer transfer) {
		String sqlUpdateTransfer = "UPDATE transfers SET transfer_status_id = ? WHERE transfer_id = ?";
		jdbcTemplate.update(sqlUpdateTransfer, transfer.getTransferStatusId(), transfer.getTransferId());
		transferring(transfer);
	}
	
	@Override
	public void rejectRequest(int id) {
		jdbcTemplate.update("UPDATE transfers SET transfer_status_id = 3 WHERE transfer_id = ?", id);
	}
	
	private void transferring(Transfer transfer) {
		String subtractionSql = "UPDATE accounts SET balance = balance - ? WHERE user_id = ?;";
		String additionSql = "UPDATE accounts SET balance = balance + ? WHERE user_id = ?;";
		jdbcTemplate.update(subtractionSql, transfer.getAmount(), transfer.getAccountFrom());
		jdbcTemplate.update(additionSql, transfer.getAmount(), transfer.getAccountTo());
	}
	
	@Override
	public void delete(int id) {
		String sqlDeleteTransfer = ("DELETE FROM transfers "
									+ "WHERE transfer_id = ?; ");
		jdbcTemplate.update(sqlDeleteTransfer, id);
	}
	
	@Override
	public List<Transfer> getPendingRequests(Integer userId) {
		String sqlGetAllPendingRequest = "SELECT " + "transfer_id, " + "transfer_type_id, " + "transfer_status_id, "
				+ "account_from," + "account_to, " + "amount " + "FROM transfers WHERE transfer_status_id = 1 AND account_from = ?;";

		List<Transfer> transfers = new ArrayList<>();

		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlGetAllPendingRequest, userId);

		while (rowSet.next()) {
			Transfer theTransfer = mapTransferFromRowSet(rowSet);
			transfers.add(theTransfer);
		}
		return transfers;
	}

	private Transfer mapTransferFromRowSet(SqlRowSet rowset) {

		Transfer theTransfer = new Transfer();

		int transferId = rowset.getInt("transfer_id");
		theTransfer.setTransferId(transferId);

		int transfer_type_id = rowset.getInt("transfer_type_id");
		theTransfer.setTransferTypeId(transfer_type_id);

		int transfers_status_id = rowset.getInt("transfer_status_id");
		theTransfer.setTransferStatusId(transfers_status_id);

		int account_from = rowset.getInt("account_from");
		theTransfer.setAccountFrom(account_from);

		int account_to = rowset.getInt("account_to");
		theTransfer.setAccountTo(account_to);

		double amount = rowset.getDouble("amount");
		theTransfer.setAmount(amount);

		return theTransfer;
	}
	
	private long getNextIdFromSequence() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('seq_transfer_id'); ");
		if(nextIdResult.next()) {
			return nextIdResult.getLong(1);
		} else {
			throw new RuntimeException("Something went wrong finding next transfer_id sequence");
		}
	}

	

}
