package com.techelevator.tenmo.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

public class JDBCAccountDAOIntegrationTest extends BaseDAOIntegrationTest {

	private AccountDAO dao;
	private JdbcTemplate jdbcTemplate;
	
	@Before
	public void setup() {
		dao = new JDBCAccountDAO(dataSource);
		jdbcTemplate = new JdbcTemplate(dataSource);
		
		String sqlDeleteDataFromAccounts = "TRUNCATE TABLE accounts CASCADE;";

		String sqlInsertFakeAccount1 = "INSERT INTO accounts (account_id, user_id, balance) "
				+ "VALUES (1000, 1, 1000); ";
		String sqlInsertFakeAccount2 = "INSERT INTO accounts (account_id, user_id, balance) "
				+ "VALUES (1001, 2, 2000); ";
		String sqlInsertFakeAccount3 = "INSERT INTO accounts (account_id, user_id, balance) "
				+ "VALUES (1002, 3, 3000); ";
		
		
		jdbcTemplate.update(sqlDeleteDataFromAccounts);
		jdbcTemplate.update(sqlInsertFakeAccount1);
		jdbcTemplate.update(sqlInsertFakeAccount2);
		jdbcTemplate.update(sqlInsertFakeAccount3);
	}
	
	@Test
	public void list_Check_If_Properly_Lists_All_Transfers() {
		// Act
		List<Account> accountTest = new ArrayList<>();
		// Arrange
		accountTest = dao.list();
		int testId = accountTest.get(0).getAccount_id();
		// Assert
		Assert.assertEquals(3, accountTest.size());
		Assert.assertEquals(1000, testId);
	}
	
	@Test
	public void getAccount_Check_If_Properly_Gets_Account() {
		// Act
		Account testAccount = new Account();
		// Arrange
		testAccount = dao.getAccount(1000);
		double testAmount = testAccount.getBalance();
		double expected = 1000;
		// Assert
		Assert.assertEquals(expected, testAmount, 0);
	}
	
	@Test
	public void create_check_if_properly_creates_new_account() {
		//Act
		List<Account> beforeCreate = new ArrayList<>();
		List<Account> afterCreate = new ArrayList<>();
		Account account = new Account();
		account.setBalance(2000);
		account.setUser_id(1);
		//Arrange
		beforeCreate = dao.list();
		dao.create(account);
		afterCreate = dao.list();
		double testBalance = dao.getAccount(account.getAccount_id()).getBalance();
		//Assert
		Assert.assertEquals(beforeCreate.size() + 1, afterCreate.size());
		Assert.assertEquals(2000, testBalance, 0);
	}
	
	@Test
	public void updateBalance_check_if_properly_updates_balance() {
		//Act
		
		//Arrange
		dao.updateBalance(1000, 2000);
		double result = dao.getAccount(1000).getBalance();
		//Assert
		Assert.assertEquals(2000, result, 0);
	}
	
	@Test
	public void delete_check_if_properly_deletes_account() {
		//Act
		List<Account> beforeDelete = new ArrayList<>();
		List<Account> afterDelete = new ArrayList<>();
		//Arrange
		beforeDelete = dao.list();
		dao.delete(1000);
		afterDelete = dao.list();
		//Assert
		Assert.assertEquals(beforeDelete.size() - 1, afterDelete.size());
	}
	
	@Test
	public void getAccountByUserId_returns_proper_account() {
		//Act
		Account account = new Account();
		//Arrange
		account = dao.getAccountByUserId(3);
		//Assert
		Assert.assertEquals(3000, account.getBalance(), 0 );
	}


}	
