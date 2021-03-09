package com.techelevator.tenmo.dao;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.tenmo.model.Transfer;

public class JDBCTransferDAOIntegrationTest extends BaseDAOIntegrationTest {

	private TransferDAO dao;
	private JdbcTemplate jdbcTemplate;

	@Before
	public void setup() {
		dao = new JDBCTransferDAO(dataSource);
		jdbcTemplate = new JdbcTemplate(dataSource);

		String sqlDeleteDataFromTransfer = "TRUNCATE TABLE transfers;";

		String sqlInsertFakeTransfer1 = "INSERT INTO transfers (transfer_id,transfer_type_id,transfer_status_id,account_from,account_to,amount) "
				+ "VALUES (1000,1,1,1,2,1000); ";
		String sqlInsertFakeTransfer2 = "INSERT INTO transfers (transfer_id,transfer_type_id,transfer_status_id,account_from,account_to,amount) "
				+ "VALUES (1001,1,1,2,1,1000); ";
		String sqlInsertFakeTransfer3 = "INSERT INTO transfers (transfer_id, transfer_type_id,transfer_status_id,account_from,account_to,amount) "
				+ "VALUES (1002,1,1,1,2,1000); ";

		jdbcTemplate.update(sqlDeleteDataFromTransfer);
		jdbcTemplate.update(sqlInsertFakeTransfer1);
		jdbcTemplate.update(sqlInsertFakeTransfer2);
		jdbcTemplate.update(sqlInsertFakeTransfer3);

	}

	@Test
	public void list_Check_If_Properly_Lists_All_Transfers() {
		// Act
		List<Transfer> transferTest = new ArrayList<>();
		// Arrange
		transferTest = dao.list();
		int testId = transferTest.get(0).getTransferId();
		// Assert
		Assert.assertEquals(3, transferTest.size());
		Assert.assertEquals(1000, testId);
	}

	@Test
	public void getTransfer_Check_If_Properly_Gets_Transfer() {
		// Act
		Transfer testTransfer = new Transfer();
		// Arrange
		testTransfer = dao.getTransfer(1000);
		double testAmount = testTransfer.getAmount();
		double expected = 1000;
		int testAccountTo = testTransfer.getAccountTo();
		// Assert
		Assert.assertEquals(expected, testAmount, 0);
		Assert.assertEquals(2, testAccountTo);
	}
	
	@Test
	public void create_Check_If_Properly_Creates_Transfer() {
		// Act
		Transfer testTransfer = new Transfer();
		testTransfer.setTransferTypeId(1);
		testTransfer.setTransferStatusId(1);
		testTransfer.setAccountFrom(2);
		testTransfer.setAccountTo(1);
		testTransfer.setAmount(1212);
		List<Transfer> transferBeforeCreation = new ArrayList<>();
		List<Transfer> transferTestAfterCreation = new ArrayList<>();
		// Arrange
		transferBeforeCreation = dao.list();
		dao.create(testTransfer);
		
		double testAmount = testTransfer.getAmount();
		double expected = 1212;
		int testAccountTo = testTransfer.getAccountTo();
		transferTestAfterCreation = dao.list();
		// Assert
		Assert.assertEquals(expected, testAmount, 0);
		Assert.assertEquals(1, testAccountTo);
		Assert.assertEquals(transferBeforeCreation.size() + 1, transferTestAfterCreation.size());
	}
	
	@Test 
	public void delete_check_if_properly_deletes_a_transfer() {
		//Act
		List<Transfer> transferBeforeDeletion = new ArrayList<>();
		List<Transfer> transferTestAfterDeletion = new ArrayList<>();
		//Arrange
		transferBeforeDeletion = dao.list();
		dao.delete(1000);
		transferTestAfterDeletion = dao.list();
		//Assert
		Assert.assertEquals(transferBeforeDeletion.size() - 1, transferTestAfterDeletion.size());
	}
	
	@Test
	public void executeTransfer_properly_creates_transfer_and_executes_to_accounts() {
		//Arrange
		Transfer theTransfer = new Transfer();
		String sqlDeleteDataFromAccounts = "TRUNCATE TABLE accounts CASCADE;";
		String sqlDeleteDataFromTransfer = "TRUNCATE TABLE transfers;";
		String createFakeAccount1 = "INSERT INTO accounts (account_id, user_id, balance) VALUES (1,1,1000)";
		String createFakeAccount2 = "INSERT INTO accounts (account_id, user_id, balance) VALUES (2,2,1000)";
		String createFakeTransfer = "INSERT INTO transfers (transfer_id,transfer_type_id,transfer_status_id,account_from,account_to,amount) "
								  + "VALUES (444,2,2,1,2,100)";
		jdbcTemplate.update(sqlDeleteDataFromAccounts);
		jdbcTemplate.update(createFakeAccount1);
		jdbcTemplate.update(createFakeAccount2);
		jdbcTemplate.update(createFakeTransfer);
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * FROM transfers WHERE transfer_id = 444");
		if(rowSet.next()) {
			theTransfer = mapTransferFromRowSet(rowSet);
		}
		//Act
		dao.executeTransfer(theTransfer);
		Double accountOneValue = jdbcTemplate.queryForObject("SELECT balance FROM accounts WHERE user_id = 1", Double.class);
		Double accountTwoValue = jdbcTemplate.queryForObject("SELECT balance FROM accounts WHERE user_id = 2", Double.class);
		//Assert
		Assert.assertEquals(accountOneValue, 900, 0);
		Assert.assertEquals(accountTwoValue, 1100, 0);
	}
	
	@Test
	public void getPendingRequest_returns_all_trasfers_with_status_pending() {
		//Arrange
		List<Transfer> testList = new ArrayList<>();
		//Act
		testList = dao.getPendingRequests(2);
		//Assert
		Assert.assertEquals(1, testList.size());
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
	
}