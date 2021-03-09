package com.techelevator.tenmo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.UserService;
import com.techelevator.view.ConsoleService;

public class App {

	private static final String API_BASE_URL = "http://localhost:8080";

	private static final String MENU_OPTION_EXIT = "Exit";
	private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN,
			MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS,
			MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS,
			MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };

	private AuthenticatedUser currentUser;
	private ConsoleService console;
	private AuthenticationService authenticationService;
	private User user = new User();
	private UserService userService = new UserService(API_BASE_URL);
	private Scanner scanner = new Scanner(System.in);
	private TransferService transferService = new TransferService(API_BASE_URL);
	private AccountService accountService = new AccountService(API_BASE_URL);

	public static void main(String[] args) {
		App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL));
		app.run();
	}

	public App(ConsoleService console, AuthenticationService authenticationService) {
		this.console = console;
		this.authenticationService = authenticationService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");

		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while (true) {
			String choice = (String) console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if (MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if (MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if (MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if (MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if (MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if (MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		Account myAccount = accountService.getAccountByUserId((currentUser.getUser().getId()), currentUser.getToken());
		System.out.println("Current balance: $" + myAccount.getBalance());
	}

	private void viewTransferHistory() {
		Transfer[] transfers = transferService.getAll(currentUser.getToken());
		String username = "";
		List<Integer> idList = new ArrayList<>();
		System.out.println("-------------------------------------------");
		System.out.println("              Transfers");
		System.out.println("ID          From/To                 Amount");
		System.out.println("-------------------------------------------");
		// Loops through array of transfers and displays them
		for (int i = 0; i < transfers.length; i++) {
			if (currentUser.getUser().getId() == transfers[i].getAccountFrom()) {
				username = userService.getUsernameById(transfers[i].getAccountTo());
				idList.add(transfers[i].getTransferId());
				System.out.println("[" + transfers[i].getTransferId() + "]" + "            To: " + username
						+ "                     $" + transfers[i].getAmount());
			} else if (currentUser.getUser().getId() == transfers[i].getAccountTo()) {
				username = userService.getUsernameById(transfers[i].getAccountFrom());
				idList.add(transfers[i].getTransferId());
				System.out.println("[" + transfers[i].getTransferId() + "]" + "          From: " + username
						+ "                     $" + transfers[i].getAmount());
			}
		}
		if (idList.isEmpty()) {
			System.out.println("You have no past transfers, returning to main menu.");
			mainMenu();
		}
		System.out.println("-------------------------------------------");
		System.out.println("Select a transfer you would like to view (0 to return to main menu).");
		String input = scanner.nextLine();
		// Checks if they want to exit to main menu or not
		Integer intInput = 0;
		try {
			intInput = Integer.parseInt(input);
		} catch (Exception e) {
			System.out.println("You did not input a correct ID. Going back to main menu.");
			mainMenu();
		}
		if (intInput == 0) {
			mainMenu();
		} else if (idList.contains(intInput)) {
			Transfer myTransfer = new Transfer();
			myTransfer = transferService.getTransferById(intInput, currentUser.getToken());
			System.out.println(myTransfer.transferToString(myTransfer));
		} else {
			System.out.println("You did not input a correct ID. Going back to main menu.");
			mainMenu();
		}
	}

	private void viewPendingRequests() {
		Transfer[] transfers = transferService.getPendingRequests(currentUser.getUser().getId(),
				currentUser.getToken());
		String username = "";
		Map<Integer, Transfer> requestsMap = new HashMap<>();
		Account usersAccount = new Account();
		usersAccount = accountService.getAccountByUserId(currentUser.getUser().getId(), currentUser.getToken());
		System.out.println("-------------------------------------------");
		System.out.println("ID                To                 Amount");
		System.out.println("-------------------------------------------");
		// Loops through array of requests and displays them
		for (int i = 0; i < transfers.length; i++) {
			if (currentUser.getUser().getId() == transfers[i].getAccountFrom()) {
				username = userService.getUsernameById(transfers[i].getAccountTo());
				requestsMap.put(transfers[i].getTransferId(), transfers[i]);
				System.out.println("[" + transfers[i].getTransferId() + "]" + "            To: " + username
						+ "                     $" + transfers[i].getAmount());
			}
		}
		if (requestsMap.isEmpty()) {
			System.out.println("You have no pending requests, returning to main menu.");
			mainMenu();
		}
		System.out.println("Please enter a transfer ID to approve/reject (0 to exit to main menu).");
		String userInput = scanner.nextLine();
		Integer input = 0;
		try {
			input = Integer.parseInt(userInput);
		} catch (Exception e) {
			System.out.println("Failed to parse input into integer.");
			mainMenu();
		}
		if (input == 0) {
			mainMenu();
		} else if (requestsMap.containsKey(input)) {
			Transfer request = requestsMap.get(input);
			System.out.println("Would you like to approve[1] or reject[2] the transfer? [0] to return to main menu.");
			String approveOrRejectString = scanner.nextLine();
			Integer approveOrReject = 0;
			try {
				approveOrReject = Integer.parseInt(approveOrRejectString);
			} catch (Exception e) {
				System.out.println("Failed to parse input into integer.");
				mainMenu();
			}
			if (approveOrReject < 0 || approveOrReject > 2) {
				System.out.println("Incorrect input, returning to main menu.");
				mainMenu();
			}
			if (approveOrReject == 0) {
				mainMenu();
			} else if (approveOrReject == 1 && usersAccount.getBalance() >= requestsMap.get(input).getAmount()) {
				request.setTransferStatusId(2);
				transferService.executeRequest(request, currentUser.getToken());
				mainMenu();
			} else if (approveOrReject == 1 && usersAccount.getBalance() < requestsMap.get(input).getAmount()) {
				System.out.println("Not enough money, returning to main menu.");
				mainMenu();
			} else {
				request.setTransferStatusId(3);
				transferService.rejectRequest(request.getTransferId(), currentUser.getToken());
				mainMenu();
			}
		}
	}

	private void sendBucks() {
		// display Users
		Account[] allAccounts = accountService.getAll(currentUser.getToken());
		Account usersAccount = new Account();
		usersAccount = accountService.getAccountByUserId(currentUser.getUser().getId(), currentUser.getToken());
		for (Account account : allAccounts) {
			String userName = userService.getUsernameById(account.getUser_id());
			System.out.println("[" + account.getUser_id() + "] " + userName);
		}

		// accept input from user to select receiver
		System.out.println("Select a a user to send funds to by user ID");
		// check if user exists/ proper input
		String input = scanner.nextLine();
		int userIdInput = 0;
		try {
			userIdInput = Integer.parseInt(input);
		} catch (Exception e) {
			System.out.println("Failed To Select Proper User");
			mainMenu();
		}
		boolean isValidUserId = false;

		for (Account account : allAccounts) {
			if (account.getUser_id() == userIdInput) {
				isValidUserId = true;
			}
		}

		if (isValidUserId) {
			System.out.println("How much would you like to send?");
			viewCurrentBalance();
			String transactionRequest = scanner.nextLine();
			double request = 0;
			try {
				request = Double.parseDouble(transactionRequest);
			} catch (Exception e) {
				System.out.println("Failed Transaction Request");
				mainMenu();
			}

			if (usersAccount.getBalance() < request) {
				System.out.println("Insufficent Funds, get more money bud");
				mainMenu();
			} else {

				Transfer transferData = new Transfer();
				transferData.setTransferTypeId(2);
				transferData.setTransferStatusId(2);
				transferData.setAccountFrom((int) usersAccount.getUser_id());
				transferData.setAccountTo((int) userIdInput);
				transferData.setAmount(request);

				boolean isTransfered = transferService.executeTransfer(transferData, currentUser.getToken());

				if (!isTransfered) {
					System.out.println("Transfer Failed");
					mainMenu();
				} else

					System.out.println("Transaction Successful!");
				viewCurrentBalance();
				mainMenu();
			}

		} else {
			System.out.println("Invalid User ID");
			mainMenu();

		}
		// if statement to check if sender has sufficent funds

		// execute transaction if funds available else send back to insuffient funds
		// message
	}

	private void requestBucks() {
		// display Users
		Account[] allAccounts = accountService.getAll(currentUser.getToken());
		Account usersAccount = new Account();
		usersAccount = accountService.getAccountByUserId(currentUser.getUser().getId(), currentUser.getToken());
		for (Account account : allAccounts) {
			String userName = userService.getUsernameById(account.getUser_id());
			System.out.println("[" + account.getUser_id() + "] " + userName);
		}

		// accept input from user to select receiver
		System.out.println("Select a a user to request funds from by user ID");
		// check if user exists/ proper input
		String input = scanner.nextLine();
		int userIdInput = 0;
		try {
			userIdInput = Integer.parseInt(input);
		} catch (Exception e) {
			System.out.println("Failed To Select Proper User");
			mainMenu();
		}
		boolean isValidUserId = false;

		for (Account account : allAccounts) {
			if (account.getUser_id() == userIdInput) {
				isValidUserId = true;
			}
		}
		if (isValidUserId == false) {
			System.out.println("User doesn't Exist, returning to main menu, ya dummie");
			mainMenu();
		}

		System.out.println("Select an amount to bum off your buddy");
		String inputAmount = scanner.nextLine();
		double amountRequest = 0;
		try {
			amountRequest = Double.parseDouble(inputAmount);
		} catch (Exception e) {
			System.out.println("Improper value, returning to main menu");
			mainMenu();
		}

		Transfer transferData = new Transfer();
		transferData.setTransferTypeId(1);
		transferData.setTransferStatusId(1);
		transferData.setAccountFrom((int) (userIdInput));
		transferData.setAccountTo((int) usersAccount.getUser_id());
		transferData.setAmount(amountRequest);
		transferService.create(transferData, currentUser.getToken());

	}

	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while (!isAuthenticated()) {
			String choice = (String) console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
		while (!isRegistered) // will keep looping until user is registered
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				authenticationService.register(credentials);
				isRegistered = true;
				System.out.println("Registration successful. You can now login.");
			} catch (AuthenticationServiceException e) {
				System.out.println("REGISTRATION ERROR: " + e.getMessage());
				System.out.println("Please attempt to register again.");
			}
		}
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) // will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: " + e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}

	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
