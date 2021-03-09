package com.techelevator.tenmo.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

public class JDBCAccountDAO implements AccountDAO {
	
	private JdbcTemplate jdbcTemplate;
	public JDBCAccountDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Account> list() {
		String sqlGetAllAccounts = "SELECT account_id, user_id, balance "
									+ "FROM accounts; ";
		
		List<Account> accounts = new ArrayList<>();
		
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlGetAllAccounts);
		
		while(rowSet.next()) {
			Account theAccount = mapAccountFromRowSet(rowSet);
			accounts.add(theAccount);
		}
		return accounts;
	}

	@Override
	public Account getAccount(int account_id) {
		String sqlGetAccount = "SELECT account_id, user_id, balance "
				+ "FROM accounts "
				+ "WHERE account_id = ?;";
		
		Account theAccount = new Account();
		
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlGetAccount, account_id);

		if(rowSet.next()) {
			theAccount = mapAccountFromRowSet(rowSet);
		}
		return theAccount;	
	}

	@Override
	public Account create(Account account) {
		String sqlCreateNewAccount = "INSERT INTO accounts(account_id, user_id, balance) "
									+ "VALUES (?,?,?); ";
		
		account.setAccount_id((int)getNextIdFromSequence());
		
		jdbcTemplate.update(sqlCreateNewAccount,
								account.getAccount_id(),
								account.getUser_id(),
								account.getBalance());
		return account;
	}

	@Override
	public void updateBalance(int account_id, double newBalance) {
		String sqlUpdateBalance = "UPDATE accounts "
									+ "SET balance = ? "
									+ "WHERE account_id = ?; ";
		
		jdbcTemplate.update(sqlUpdateBalance, newBalance, account_id);
	}

	@Override
	public void delete(int account_id) {
		String sqlDeleteAccount = ("DELETE FROM accounts "
				+ "WHERE account_id = ?; ");
		jdbcTemplate.update(sqlDeleteAccount, account_id);
		
	}
	
	@Override
	public Account getAccountByUserId(int userId) {
		String sqlGetAccountByUserId = "SELECT account_id, user_id, balance "
										+ "FROM accounts "
										+ "WHERE user_id = ?; ";
		
		Account theAccount = new Account();
		
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlGetAccountByUserId, userId);
		
		if(rowSet.next()) {
			theAccount = mapAccountFromRowSet(rowSet);
		}
		return theAccount;	
	}
	
	private Account mapAccountFromRowSet(SqlRowSet rowset) {

		Account theAccount = new Account();

		int accountId = rowset.getInt("account_id");
		theAccount.setAccount_id(accountId);

		int userId = rowset.getInt("user_id");
		theAccount.setUser_id(userId);

		double balance = rowset.getDouble("balance");
		theAccount.setBalance(balance);

		return theAccount;

	}
	
	private long getNextIdFromSequence() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('seq_account_id'); ");
		if(nextIdResult.next()) {
			return nextIdResult.getLong(1);
		} else {
			throw new RuntimeException("Something went wrong finding next transfer_id sequence");
		}
	}

	
}
