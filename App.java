package cs174a;                                             // THE BASE PACKAGE FOR YOUR APP MUST BE THIS ONE.  But you may add subpackages.

// You may have as many imports as you need.
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.util.Properties;
import oracle.jdbc.pool.OracleDataSource;
import oracle.jdbc.OracleConnection;
import java.lang.*;
import java.util.Scanner;

/**
 * The most important class for your application.
 * DO NOT CHANGE ITS SIGNATURE.
 */
public class App implements Testable
{
	private OracleConnection _connection;                   // Example connection object to your DB.
	private String currentTaxID = "";
        private String[] monthStr = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
	private String defaultBranchName = "DefaultBranch";
	/**
	 * Default constructor.
	 * DO NOT REMOVE.
	 */
	App()
	{
		// TODO: Any actions you need.
	}

	/**
	 * This is an example access operation to the DB.
	 */
	void exampleAccessToDB()
	{
		// Statement and ResultSet are AutoCloseable and closed automatically.
		try( Statement statement = _connection.createStatement() )
		{
			try( ResultSet resultSet = statement.executeQuery( "SELECT owner, table_name FROM user_tables" ) )
			{
				while( resultSet.next() )
					System.out.println( resultSet.getString( 1 ) + " " + resultSet.getString( 2 ) + " " );
			}
		}
		catch( SQLException e )
		{
			System.err.println( e.getMessage() );
		}
	}

	//runs the program. also functions as the UI
	public void run()
	{
		System.out.println(initializeSystem());
		//System.out.println(dropTables());
		//System.out.println(createTables());

		Scanner in = new Scanner(System.in);
    int choice = 0;
		boolean exit = false;

		int type = 0;
		AccountType accountType;
		double interestRate = 0.0;

		//sample data
		/*System.out.println(createCheckingSavingsAccount(AccountType.STUDENT_CHECKING, "17431", 1200, "344151573", "Joe Pepsi", "3210 State St"));
		setPIN("1717", "3692");
		System.out.println(createCustomer("17431", "412231856", "Cindy Laugher", "7000 Hollister"));
		setPIN("1717","3764");
		System.out.println(createCustomer("17431", "322175130", "Ivan Lendme", "1235 Johnson Dr"));
		setPIN("1717","8471");
		//System.out.println("a");

		System.out.println(createCheckingSavingsAccount(AccountType.STUDENT_CHECKING, "54321", 21000, "212431965", "Hurryson Ford", "678 State St"));
		setPIN("1717", "3532");
		System.out.println(createCustomer("54321", "412231856", "Cindy Laugher", "7000 Hollister"));
		//setPIN("1717","3764");
		System.out.println(createCustomer("54321", "122219876", "Elizabeth Sailor", "4321 State St"));
		setPIN("1717","3856");
		System.out.println(createCustomer("54321", "203491209", "Nam-Hoi Chung", "1997 Peoples St HK"));
		setPIN("1717","5340");
		//System.out.println("b");

		System.out.println(createCheckingSavingsAccount(AccountType.STUDENT_CHECKING, "12121", 1200, "207843218", "David Copperfill", "1357 State St"));
		setPIN("1717", "8582");
		//System.out.println("c");

		System.out.println(createCheckingSavingsAccount(AccountType.INTEREST_CHECKING, "41725", 15000, "201674933", "George Brush", "5346 Foothill Av"));
		setPIN("1717", "9824");
		createCustomer("41725", "401605312", "Fatal Castro", "3756 La Cumbre Plaza");
		setPIN("1717","8193");
		createCustomer("41725", "231403227", "Billy Clinton", "5777 Hollister");
		setPIN("1717","1468");
		//System.out.println("d");

		System.out.println(createCheckingSavingsAccount(AccountType.INTEREST_CHECKING, "76543", 8456, "212116070", "Li Kung", "2 Peoples Rd Beijing"));
		setPIN("1717", "9173");
		createCustomer("76543", "188212217", "Magic Jordon", "3852 Court Rd");
		setPIN("1717","7351");
		//System.out.println("e");

		System.out.println(createCheckingSavingsAccount(AccountType.INTEREST_CHECKING, "93156", 2000000, "209378521", "Kelvin Costner", "Santa Cruz #3579"));
		setPIN("1717", "4659");
		createCustomer("93156", "188212217", "Magic Jordon", "3852 Court Rd");
		//setPIN("1717","7351");
		createCustomer("93156", "210389768", "Olive Stoner", "6689 El Colegio #151");
		setPIN("1717","8452");
		createCustomer("93156", "122219876", "Elizabeth Sailor", "4321 State St");
		//setPIN("1717","3856");
		createCustomer("93156", "203491209", "Nam-Hoi Chung", "1997 Peoples St HK");
		//setPIN("1717","5340");
		//System.out.println("f");

		System.out.println(createCheckingSavingsAccount(AccountType.SAVINGS, "43942", 1289, "361721022", "Alfred Hitchcock", "6667 El Colegio #40"));
		setPIN("1717", "1234");
		createCustomer("43942", "400651982", "Pit Wilson", "911 State St");
		setPIN("1717","1821");
		createCustomer("43942", "212431965", "Hurryson Ford", "678 State St");
		//setPIN("1717","3532");
		createCustomer("43942", "322175130", "Ivan Lendme", "1235 Johnson Dr");
		//setPIN("1717","8471");
		//System.out.println("g");

		System.out.println(createCheckingSavingsAccount(AccountType.SAVINGS, "29107", 34000, "209378521", "Kelvin Costner", "Santa Cruz #3579"));
		//setPIN("1717", "4659");
		createCustomer("29107", "212116070", "Li Kung", "2 Peoples Rd Beijing");
		//setPIN("1717","9173");
		createCustomer("29107", "210389768", "Olive Stoner", "6689 El Colegio #151");
		//setPIN("1717","8452");
		//System.out.println("h");

		System.out.println(createCheckingSavingsAccount(AccountType.SAVINGS, "19023", 2300, "412231856", "Cindy Laugher", "7000 Hollister"));
		//setPIN("1717","3764");
		createCustomer("19023", "201674933", "George Brush", "5346 Foothill Av");
		//setPIN("1717","9824");
		createCustomer("19023", "401605312", "Fatal Castro", "3756 La Cumbre Plaza");
		//setPIN("1717","8193");
		//System.out.println("i");

		System.out.println(createCheckingSavingsAccount(AccountType.SAVINGS, "32156", 1000, "188212217", "Magic Jordon", "3852 Court Rd"));
		//setPIN("1717","7351");
		createCustomer("32156", "207843218", "David Copperfill", "1357 State St");
		//setPIN("1717", "8582");
		createCustomer("32156", "122219876", "Elizabeth Sailor", "4321 State St");
		//setPIN("1717","3856");
		createCustomer("32156", "344151573", "Joe Pepsi", "3210 State St");
		//setPIN("1717", "3692");
		createCustomer("32156", "203491209", "Nam-Hoi Chung", "1997 Peoples St HK");
		//setPIN("1717","5340");
		createCustomer("32156", "210389768", "Olive Stoner", "6689 El Colegio #151");
		//setPIN("1717","8452");
		//System.out.println("j");

		System.out.println(createPocketAccount("53027", "12121", 50, "207843218"));
		System.out.println(createPocketAccount("43947", "29107", 30, "212116070"));
		System.out.println(createPocketAccount("60413", "43942", 20, "400651982"));
		System.out.println(createPocketAccount("67521", "19023", 100, "401605312"));

		
		deposit("17431", 8800.0);
    withdrawal("54321", 3000.0);
		withdrawal("76543", 2000.0);
    purchase("53027", 5.0);
    deposit("41725", 15000.0);
    deposit("93156", 2000000.0);
    top_up("53027", 50.0);
    deposit("361721022", 1289.0, 43942);
    deposit("209378521", 34000.0, 29107);
    deposit("412231856", 2300.0, 19023);
    top_up("400651982", 20.0, 60413);
    deposit("188212217", 1000.0, 32156);
    deposit("212116070", 8456.0, 76543);
    top_up("212116070", 30.0, 43947);
    top_up("401605312", 100.0, 67521);*/

		//AccountType.STUDENT_CHECKING,AccountType.INTEREST_CHECKING,AccountType.SAVINGS,AccountType.POCKET;
		//create checking savings(accountType, account, initialBalance, currentTaxID, name, address);
		//create customer ( String accountId, String tin, String name, String address )
		//createPocketAccount( String id, String linkedId, double initialTopUp, String tin )

		do {

			System.out.println("\nChoose one of the following options:");
			System.out.println("0: Show customer interface");
			System.out.println("1: Show bank teller interface");
			System.out.println("2: Set system date");
			System.out.println("3: Set interest rate");
			System.out.println("4: Create tables");
			System.out.println("5: Drop tables");
			System.out.println("6: Exit application");
			choice = in.nextInt();
			in.nextLine();
			//System.out.println();

			switch(choice)
			{
				case 0:
					runCustomerInterface();
					break;
				case 1:
					runBankTellerInterface();
					break;
				case 2:
					changeSystemDate();
					break;
				case 3:
					accountType = null;
					do {
						System.out.print("Please input the account type (0: Student Checking, 1: Interest Checking, 2: Savings, 3: Pocket): ");
						type = in.nextInt();
						in.nextLine();
						if (type == 0)
							accountType = AccountType.STUDENT_CHECKING;
						else if (type == 1)
							accountType = AccountType.INTEREST_CHECKING;
						else if (type == 2)
							accountType = AccountType.SAVINGS;
						else if (type == 3)
							accountType = AccountType.POCKET;
						else
						{
							System.out.println("Please choose a valid option.");
						}
					} while (accountType == null);
					System.out.print("Please input the new interest rate: ");
					interestRate = in.nextDouble();
					System.out.println(changeInterestRate(accountType, interestRate));
					break;
				case 4:
					System.out.println(createTables());
					break;
				case 5:
					System.out.println(dropTables());
					break;
				case 6:
					exit = true;
					break;
				default:
					System.out.println("Please choose a valid option.");
					break;
			}
		} while(!exit);
		System.out.println("Exiting application.");
	}

	private void runCustomerInterface()
	{
		Scanner in = new Scanner(System.in);
		int choice = 0;
		//String tid = "";
		String pin = "";
		System.out.print("Please enter your tax identification number: ");
		currentTaxID = in.nextLine();
		System.out.print("Please enter your PIN (4 digits): ");
		pin = in.nextLine();
		if (pin.length() != 4 || !verifyPin(pin))
		{
			System.out.println("\nVerification failed.");
			return;
		}

		boolean exit = false;
		String account1 = "";
		String account2 = "";
		double amount = 0.0;
		String oldPIN = "";
		String newPIN = "";

		do {
			System.out.println("\n[CUSTOMER INTERFACE]");
			System.out.println("Choose one of the following options:");
			System.out.println("0: Make a deposit");
			System.out.println("1: Make a top-up");
			System.out.println("2: Withdraw money");
			System.out.println("3: Make a purchase");
			System.out.println("4: Make a transfer");
			System.out.println("5: Collect money from a pocket account");
			System.out.println("6: Wire money to another account");
			System.out.println("7: Pay a friend");
			System.out.println("8: Show balance of an account");
			System.out.println("9: Change PIN");
			System.out.println("10: Return to main menu");

			choice = in.nextInt();
			in.nextLine();
			System.out.println();

			switch(choice)
			{
				case 0:
					System.out.print("Please input the ID of the account you would like to deposit to: ");
					account1 = in.nextLine();
					System.out.print("Please input the amount you would like to deposit: ");
					amount = in.nextDouble();
					if (!isOwnerOf(account1, currentTaxID))
					{
						System.out.println("1 Account does not exist OR current customer is not an owner of the account.");
					}
					System.out.println(deposit(account1, amount));
					break;
				case 1:
					System.out.print("Please input the ID of the account you would like to make a top-up to: ");
					account1 = in.nextLine();
					System.out.print("Please input the amount you would like to top-up: ");
					amount = in.nextDouble();
					if (!isOwnerOf(account1, currentTaxID))
					{
						System.out.println("1 Account does not exist OR current customer is not an owner of the account.");
					}
					System.out.println(topUp(account1, amount));
					break;
				case 2:
					System.out.print("Please input the ID of the account you would like to withdraw from: ");
					account1 = in.nextLine();
					System.out.print("Please input the amount you would like to withdraw: ");
					amount = in.nextDouble();

					if (!isOwnerOf(account1, currentTaxID))
					{
						System.out.println("1 Account does not exist OR current customer is not an owner of the account.");
					}
					System.out.println(withdrawal(account1, amount));
					break;
				case 3:
					System.out.print("Please input the ID of the account you would like to make a purchase with: ");
					account1 = in.nextLine();
					System.out.print("Please input the amount you would like to spend: ");
					amount = in.nextDouble();
					if (!isOwnerOf(account1, currentTaxID))
					{
						System.out.println("1 Account does not exist OR current customer is not an owner of the account.");
					}
					System.out.println(purchase(account1, amount));
					break;
				case 4:
					System.out.print("Please input the ID of the account you would like to transfer from: ");
					account1 = in.nextLine();
					System.out.print("Please input the ID of the account you would like to transfer to: ");
					account2 = in.nextLine();
					System.out.print("Please input the amount you would like to transfer: ");
					amount = in.nextDouble();
					if (!isOwnerOf(account1, currentTaxID) || !isOwnerOf(account2, currentTaxID))
					{
						System.out.println("1 Account(s) does not exist OR current customer is not an owner of at least one of the accounts.");
					}
					System.out.println(transfer(account1, account2, amount));
					break;
				case 5:
					System.out.print("Please input the ID of the account you would like to collect from: ");
					account1 = in.nextLine();
					System.out.print("Please input the amount you would like to collect: ");
					amount = in.nextDouble();
					if (!isOwnerOf(account1, currentTaxID))
					{
						System.out.println("1 Account does not exist OR current customer is not an owner of the account.");
					}
					System.out.println(collect(account1, amount));
					break;
				case 6:
					System.out.print("Please input the ID of the account you would like to wire money from: ");
					account1 = in.nextLine();
					System.out.print("Please input the ID of the account you would like to send money to: ");
					account2 = in.nextLine();
					System.out.print("Please input the amount you would like to send: ");
					amount = in.nextDouble();
					if (!isOwnerOf(account1, currentTaxID))
					{
						System.out.println("1 Account does not exist OR current customer is not an owner of the sending account.");
					}

					if (isOwnerOf(account2, currentTaxID))
					{
						System.out.println("1 Current customer is also an owner of the receiving account.");
					}
					System.out.println(wire(account1, account2, amount));
					break;
				case 7:
					System.out.print("Please input the ID of the account you would like to send money from: ");
					account1 = in.nextLine();
					System.out.print("Please input the ID of the account you would like to send money to: ");
					account2 = in.nextLine();
					System.out.print("Please input the amount you would like to send: ");
					amount = in.nextDouble();
					if (!isOwnerOf(account1, currentTaxID))
					{
						System.out.println("1 Account does not exist OR current customer is not an owner of the sending account.");
					}

					if (isOwnerOf(account2, currentTaxID))
					{
						System.out.println("1 Current customer is also an owner of the receiving account.");
					}
					System.out.println(payFriend(account1, account2, amount));
					break;
				case 8:
					System.out.print("Please input the ID of the account you would like to show the balance of: ");
					account1 = in.nextLine();
					if (!isOwnerOf(account1, currentTaxID))
					{
						System.out.println("1 Account does not exist OR current customer is not an owner of the account.");
					}
					System.out.println(showBalance(account1));
					break;
				case 9:
					System.out.print("Please input your current PIN: ");
					oldPIN = in.nextLine();
					System.out.print("Please input your new PIN: ");
					newPIN = in.nextLine();
					setPIN(oldPIN, newPIN);
					break;
				case 10:
					exit = true;
					break;
				default:
					System.out.println("Please choose a valid option.");
					break;
			}
		} while(!exit);
		System.out.println("Returning to main menu.");
	}

	private void runBankTellerInterface()
	{
		Scanner in = new Scanner(System.in);
		int choice = 0;
		boolean exit = false;
		String account = "";
		String tid = "";
		String linkedId = "";
		String name = "";
		String address = "";
		double initialBalance = 0.0;
		double amount = 0.0;
		int type = 0;
		AccountType accountType = null;
		int numOwners = 0;
		String result = "";

		do {
			System.out.println("\n[BANK TELLER INTERFACE]");
			System.out.println("Choose one of the following options:");
			System.out.println("0: Enter check transaction");
			System.out.println("1: Generate monthly statement");
			System.out.println("2: List closed accounts");
			System.out.println("3: Generate government drug and tax evasion report");
			System.out.println("4: Customer report");
			System.out.println("5: Add interest");
			System.out.println("6: Create account");
			System.out.println("7: Delete closed accounts and customers");
			System.out.println("8: Delete transactions");
			System.out.println("9: Return to main menu");

			choice = in.nextInt();
			in.nextLine();
			System.out.println();
			numOwners = 0;

			switch(choice)
			{
				case 0:
					System.out.print("Please input the ID of the account involved in the transaction: ");
					account = in.nextLine();
					System.out.print("Please input the check amount: ");
					amount = in.nextDouble();
					System.out.println(writeCheck(account, amount));
					break;
				case 1:
					System.out.print("Please input the ID of the customer to generate a monthly statement for: ");
					account = in.nextLine();
					System.out.println(getMonthlyStatement(account));
					break;
				case 2:
					System.out.println(listClosedAccounts());
					break;
				case 3:
					System.out.println(getDTER());
					break;
				case 4:
					System.out.print("Please input the customer's tax ID: ");
					tid = in.nextLine();
					currentTaxID = tid;
					System.out.println(getCustomerReport(tid));
					break;
				case 5:
					System.out.println(addInterest());
					break;
				case 6:
					System.out.print("Please input the ID of the new account: ");
					account = in.nextLine();
					do {
						System.out.print("Please input the account type (0: Student Checking, 1: Interest Checking, 2: Savings, 3: Pocket): ");
						type = in.nextInt();
						in.nextLine();
						if (type == 0)
							accountType = AccountType.STUDENT_CHECKING;
						else if (type == 1)
							accountType = AccountType.INTEREST_CHECKING;
						else if (type == 2)
							accountType = AccountType.SAVINGS;
						else if (type == 3)
							accountType = AccountType.POCKET;
						else
						{
							System.out.println("Please choose a valid option.");
						}
					} while (accountType == null);

					System.out.print("Please input the initial balance: ");
					initialBalance = in.nextDouble();
					in.nextLine();

					if (type == 3)
					{
						System.out.print("Please input the primary account owner's tax ID: ");
						currentTaxID = in.nextLine();
						System.out.print("Please input the account ID you would like to link the pocket account with: ");
						linkedId = in.nextLine();
						System.out.println(createPocketAccount(account, linkedId, initialBalance, currentTaxID));
					}
					else
					{
						System.out.println("Please input the number of owners (including the primary owner):");
						numOwners = in.nextInt();
						in.nextLine();
						System.out.print("Please input the primary account owner's tax ID: ");
						currentTaxID = in.nextLine();
						System.out.print("Please input the primary account owner's name: ");
						name = in.nextLine();
						System.out.print("Please input the primary account owner's address: ");
						address = in.nextLine();
						result = createCheckingSavingsAccount(accountType, account, initialBalance, currentTaxID, name, address);
						System.out.println(result);
						if (result.length() > 0 && result.charAt(0) == '0')
						{
							for(int i = 0; i < numOwners-1; i++)
							{
								System.out.print("Please input the co-owner's tax ID: ");
								currentTaxID = in.nextLine();
								System.out.print("Please input the co-owner's name: ");
								name = in.nextLine();
								System.out.print("Please input the co-owner's address: ");
								address = in.nextLine();
								System.out.println(createCustomer(account, currentTaxID, name, address));
							}
						}
					}
					break;
				case 7:
					System.out.println(deleteClosedAccounts());
					break;
				case 8:
					System.out.println(deleteTransactions());
					break;
				case 9:
					exit = true;
					break;
				default:
					System.out.println("Please choose a valid option.");
					break;
			}
		} while(!exit);
		System.out.println("Returning to main menu.");
	}

	private void changeSystemDate()
	{
	    Scanner in = new Scanner(System.in);
	    System.out.println("Enter year:");
	    int year = in.nextInt();
	    System.out.println("Enter month:");
	    int month = in.nextInt();
	    System.out.println("Enter day:");
	    int day = in.nextInt();
	    System.out.println(setDate(year, month, day));
	}

	private String requestAccountID(String type)
	{
		Scanner in = new Scanner(System.in);
		System.out.println("Please input the " + type + " account ID:");
		return in.nextLine();
	}

	////////////////////////////// Implement all of the methods given in the interface /////////////////////////////////
	// Check the Testable.java interface for the function signatures and descriptions.

	@Override
	public String initializeSystem()
	{
		// Some constants to connect to your DB.
		final String DB_URL = "jdbc:oracle:thin:@cs174a.cs.ucsb.edu:1521/orcl";
		//final String DB_USER = "c##dzhang01";
		//final String DB_PASSWORD = "4361382";
		final String DB_USER = "c##shirlyntang";
		final String DB_PASSWORD = "5320411";

		// Initialize your system.  Probably setting up the DB connection.
		Properties info = new Properties();
		info.put( OracleConnection.CONNECTION_PROPERTY_USER_NAME, DB_USER );
		info.put( OracleConnection.CONNECTION_PROPERTY_PASSWORD, DB_PASSWORD );
		info.put( OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH, "20" );

		try
		{
			OracleDataSource ods = new OracleDataSource();
			ods.setURL( DB_URL );
			ods.setConnectionProperties( info );
			_connection = (OracleConnection) ods.getConnection();

			// Get the JDBC driver name and version.
			DatabaseMetaData dbmd = _connection.getMetaData();
			System.out.println( "Driver Name: " + dbmd.getDriverName() );
			System.out.println( "Driver Version: " + dbmd.getDriverVersion() );

			// Print some connection properties.
			System.out.println( "Default Row Prefetch Value is: " + _connection.getDefaultRowPrefetch() );
			System.out.println( "Database Username is: " + _connection.getUserName() );
			System.out.println();

			return "0";
		}
		catch( SQLException e )
		{
			System.err.println( e.getMessage() );
			return "1";
		}
	}

	///////////////////////////////////////// Functions for testing your system ////////////////////////////////////////

	@Override
	public String dropTables()
	{
		try( Statement statement = _connection.createStatement() )
		{
			statement.executeQuery( "DROP TABLE Owns" );
			statement.executeQuery( "DROP TABLE Transactions" );
			statement.executeQuery( "DROP TABLE CurrentDate" );
			statement.executeQuery( "DROP TABLE InterestAdded" );
			statement.executeQuery( "DROP TABLE PocketOwner" );
			statement.executeQuery( "DROP TABLE Customers" );
			statement.executeQuery( "DROP TABLE Accounts" );
			statement.executeQuery( "DROP TABLE InterestRates");
			return "0";
		}
		catch( SQLException e )
		{
			System.err.println( e.getMessage() );
			return "1";
		}
	}

	@Override
	public String createTables()
	{
		try( Statement statement = _connection.createStatement() )
		{
				String[] statements = new String[12];
				statements[0] = "CREATE TABLE Customers (taxID CHAR(9), name CHAR(20), address CHAR(50), PIN CHAR(50), PRIMARY KEY (taxID))";
				statements[1] = "CREATE TABLE Accounts (accountID CHAR(50), branchName CHAR(20), balance REAL, isOpen NUMBER(1,0), accountType CHAR(20), PRIMARY KEY (accountID))";
				statements[2] = "CREATE TABLE Owns (taxID CHAR(9), accountID CHAR(50), isPrimary NUMBER(1,0), PRIMARY KEY (taxID, accountID), FOREIGN KEY (taxID) REFERENCES Customers ON DELETE CASCADE, FOREIGN KEY (accountID) REFERENCES Accounts ON DELETE CASCADE)";
				statements[3] = "CREATE TABLE Transactions (transactionID INTEGER, accountID CHAR(50), otherAccount CHAR(50), transactionDate DATE, transactionType CHAR(20), amount REAL, checkNumber INTEGER, PRIMARY KEY (transactionID), FOREIGN KEY (accountID) REFERENCES Accounts)";
				statements[4] = "CREATE TABLE CurrentDate (currentdate Date)";
				statements[5] = "CREATE TABLE InterestAdded (dateAdded DATE)";
				statements[6] = "CREATE TABLE PocketOwner(pocketID CHAR(50), ownerID CHAR(50), PRIMARY KEY(pocketID, ownerID), FOREIGN KEY (pocketID) REFERENCES Accounts (accountID) ON DELETE CASCADE, FOREIGN KEY (ownerID) REFERENCES Accounts (accountID) ON DELETE CASCADE)";
				statements[7] = "CREATE TABLE InterestRates(accountType char(20), rate REAL)";
				statements[8] = "INSERT INTO InterestRates(accountType, rate) values (\'InterestChecking\', 0.03)";
				statements[9] = "INSERT INTO InterestRates(accountType, rate) values (\'Savings\', 0.048)";
				statements[10] = "INSERT INTO InterestRates(accountType, rate) values (\'Pocket\', 0)";
				statements[11] = "INSERT INTO InterestRates(accountType, rate) values (\'StudentChecking\', 0)";
				for (int i = 0; i < statements.length; i++){
				    ResultSet resultSet = statement.executeQuery( statements[i] );
						//System.out.println(i);
				}
				ResultSet resultSet = statement.executeQuery( "INSERT INTO CurrentDate (currentdate) VALUES ('01-JAN-2000')");

				}
				catch( SQLException e )
				{
					System.err.println( e.getMessage() );
					return "1";
				}
			return "0";
	}

	@Override
	public String setDate( int year, int month, int day )
	{
		if (year > 9999 || year < 1000){
			    return "1";
			}
			if (month < 1 || month > 12){
			    return "1";
			}
			if (day < 1){
			    return "1";
			}
			if (day > 28 && month == 2){
			    return "1";
			} else if (day > 30 && (month == 4 || month == 6 || month == 9 || month == 11)){
			    return "1";
			} else if (day > 31){
			    return "1";
			}
			String dateStr = day + "-" + monthStr[month-1] + "-" + year;
			//System.out.println(dateStr);
			try(Statement statement = _connection.createStatement()){
			    //Statement statement = _connection.createStatement();

			    ResultSet resultSet = statement.executeQuery("UPDATE CurrentDate SET currentdate = \'" + dateStr + "\'");
			    return "0\n" + dateStr;
			}catch (Exception e){
			    System.err.println( e.getMessage() );
			    return "1";
			}
	}

	@Override
	public String createCheckingSavingsAccount( AccountType accountType, String id, double initialBalance, String tin, String name, String address )
	{
		int counter = 0;
		if (initialBalance < 1000)
		{
			System.out.println("Initial balance too low.");
			return "1";
		}
		double interestRate = 0.0;
		String type = "";
		boolean createdCustomer = false;
		String result = "";
		switch (accountType)
		{
			case INTEREST_CHECKING:
			    //interestRate = 0.03;
				type = "InterestChecking";
				break;
			case SAVINGS:
			    //interestRate = 0.048;
				type = "Savings";
				break;
			case STUDENT_CHECKING:
			    //interestRate = 0.0;
				type = "StudentChecking";
				break;
			default:
				System.out.println("Wrong account type.");
				return "1";
		}


		try (Statement statement = _connection.createStatement())
		{
			//System.out.println("create_saving_checking0");
			counter = 1;
			ResultSet resultSet = statement.executeQuery("SELECT * FROM Customers WHERE taxID = \'" + tin + "\'");
			//System.out.println("create_saving_checking1");
			counter = 2;
			if( !resultSet.next() )
			{
				//System.out.println("test1");
				result = createCustomer(id, tin, name, address);
				if (result.length() <= 0 || result.charAt(0) == '1')
					return "1 Customer was unable to be created.";
				createdCustomer = true;
				//System.out.println("create_saving_checking2");
				counter = 3;
			}

    	statement.executeQuery("INSERT INTO Accounts VALUES (\'" + id + "\',\'" + defaultBranchName + "\',0.0, 1, \'" + type + "\')"); //1 near the end means account is open
			statement.executeQuery("INSERT INTO Owns VALUES (\'" + tin + "\',\'" + id + "\', 1)");
			//System.out.println("create_saving_checking4");
			result = deposit(id, initialBalance);

			if (result.length() <= 0 || result.charAt(0) == '1')
			{
				try{
					statement.executeQuery("DELETE FROM Transactions WHERE accountID = \'" + id + "\'");
				}
					catch( SQLException e ){}
				try{
					statement.executeQuery("DELETE FROM Owns WHERE accountID = \'" + id + "\' AND taxID = \'" + tin + "\'");
				}
					catch( SQLException e ){}
				try{statement.executeQuery("DELETE FROM Accounts WHERE accountID = \'" + id + "\'");
				} //1 near the end means account is open
					catch( SQLException e ){}
				if (createdCustomer)
				{
					try{
						statement.executeQuery("DELETE FROM Customers WHERE taxID = \'" + tin + "\'");
					}
						catch( SQLException e ){}
				}
				return "1 Initial deposit failed. Account not created.";
			}
    	return "0 " + id + " " + type + " " + String.format("%.2f", initialBalance) + " " + tin;
    }
    catch( SQLException e )
		{
			System.err.println( counter + " " + e.getMessage() );
			return "1";
		}
	}

	@Override
	public String createPocketAccount( String id, String linkedId, double initialTopUp, String tin )
	{
		double interestRate = 0.0;
		String type = "Pocket";
		String branch = "";

		try (Statement statement = _connection.createStatement()) {

			ResultSet resultSet = statement.executeQuery( "SELECT * FROM Owns WHERE taxID = \'" + tin + "\' and accountID = \'" + linkedId + "\'");
			//System.out.println("create_pocket0");
			if (!resultSet.next())
			{
				System.out.println("Linked account is invalid.");
				return "1";
			}

			//System.out.println("create_pocket0.5");

			ResultSet resultSet2 = statement.executeQuery( "SELECT isOpen, accountType FROM Accounts WHERE accountID = \'" + linkedId + "\'" );

			if( !resultSet2.next() || resultSet2.getInt(1) == 0 || resultSet2.getString(2).trim().equals("Pocket"))
			{
				//System.out.println("create_pocket1");
				return "1 Linked account is invalid.";
			}
			else
			{
				//System.out.println("create_pocket2");
				ResultSet linkedAccount = statement.executeQuery( "SELECT balance, branchName FROM Accounts WHERE accountID = \'" + linkedId + "\'");
				if( !linkedAccount.next() )
				{
					return "1 Linked account is invalid.";
				}
				//System.out.println("create_pocket3");
				if (linkedAccount.getDouble(1) - (initialTopUp + 5) <= 0.01) //TODO: not sure if getDouble works, check
				{
					return "1 Not enough balance in the linked account.";
				}
				//System.out.println("create_pocket4");
				branch = linkedAccount.getString(2);
			}

			//System.out.println("create_pocket4");
			statement.executeQuery("INSERT INTO Accounts VALUES (\'" + id + "\',\'" + branch + "\', 0.0, 1, \'Pocket\')"); //1 near the end means account is open
			statement.executeQuery("INSERT INTO Owns VALUES (\'" + tin + "\',\'" + id + "\', 1)");
			statement.executeQuery("INSERT INTO PocketOwner VALUES (\'" + id + "\',\'"+ linkedId + "\')"); //1 near the end means account is open

			//System.out.println("create_pocket5");
			String result = topUp(id, initialTopUp);

			if (result.length() <= 0 || result.charAt(0) == '1')
			{
				//System.out.println("create_pocket6");
				try{
					statement.executeQuery("DELETE FROM Owns WHERE accountID = \'" + id + "\' AND taxID = \'" + tin + "\'");
				}
					catch( SQLException e ){}
				//System.out.println("create_pocket6.2");
				try{
					statement.executeQuery("DELETE FROM PocketOwner WHERE pocketID = \'" + id + "\' AND ownerID = \'" + linkedId + "\'");
				}
					catch( SQLException e ){}
				try{
					statement.executeQuery("DELETE FROM Transactions WHERE accountID = \'" + id + "\'");
				}
					catch( SQLException e ){}
				//System.out.println("create_pocket6.5");
				try{
					statement.executeQuery("DELETE FROM Accounts WHERE accountID = \'" + id + "\'");
				}
					catch( SQLException e ){}
				//System.out.println("create_pocket6.7");
				return "1 Initial topup failed.";
			}
			return "0 " + id + " Pocket " + String.format("%.2f", initialTopUp) + " " + tin;
    }
    catch( SQLException e )
		{
			System.err.println( e.getMessage() );
			return "1";
		}
	}

	@Override
	public String createCustomer( String accountId, String tin, String name, String address )
	{
		//System.out.println("create customer0");
		String pin = "1717";
		currentTaxID = tin;
		String hashedPin = hashPin(pin);
		try (Statement statement = _connection.createStatement()) {

        try {
					statement.executeQuery("INSERT INTO Customers VALUES (\'" + tin + "\',\'" + name + "\',\'" + address + "\',\'" + hashedPin + "\')");
				} catch( SQLException e ){};//System.err.println( e.getMessage() );}

				ResultSet resultSet = statement.executeQuery("SELECT * FROM Accounts WHERE accountID = \'" + accountId + "\'");
				if (resultSet.next())
				{
					if (resultSet.getInt("isOpen") == 1) //if the account exists, insert a relation into owns. otherwise, we should take care of this in the account creation.
					{
						//System.out.println("inserting into owns in create customer");
						statement.executeQuery("INSERT INTO Owns VALUES (\'" + tin + "\',\'" + accountId + "\', 0)");
					}
					else
					{
						System.out.println("Account with id " + accountId + " is closed.");
						return "1";
					}
				}
				return "0";
    }
    catch( SQLException e )
		{
			System.err.println( e.getMessage() );
			return "1";
		}
	}

	@Override
	public String deposit( String accountId, double amount )
	{
			double bal = 0.0;
			if (amount <= 0)
			{
				return "1 Amount to deposit is invalid.";
			}
			int transactionId = 0;

			try (Statement statement = _connection.createStatement()) {
					if (verifyAccountType(accountId, "Pocket"))
					{
						return "1 The account is not a checkings or savings account.";
					}

					ResultSet balance = statement.executeQuery( "SELECT balance FROM Accounts WHERE accountID = \'" + accountId + "\'");
					if( !balance.next() )
					{
						return "1 Account is invalid.";
					}
					bal = balance.getDouble(1);

					//System.out.println("deposit 1");
					ResultSet transIds = statement.executeQuery( "SELECT nvl(max(transactionId), 0) AS maxTransId FROM Transactions");
					if (!transIds.next())
					{
						return "1 No transactions but weird bug";
					}
					transactionId = transIds.getInt("maxTransId")+1;

					//System.out.println("deposit 3");
					ResultSet day = statement.executeQuery("SELECT * FROM CurrentDate");
					//System.out.println("deposit 4");
					Date currDate = new Date(2011, 3, 1);
					if (day.next())
						currDate = day.getDate("currentdate");
					else
						return "1";
					//System.out.println("deposit 5");
					statement.executeQuery("UPDATE Accounts SET balance = balance + " + Double.toString(amount) + " WHERE accountId = \'" + accountId + "\'");
					//System.out.println("deposit 6");
					String insertStatement = "INSERT INTO Transactions VALUES (?,?,?,?,?,?,?)";
					executeTransaction(insertStatement, transactionId, accountId, null, "deposit", amount, 0);
					//System.out.println("deposit 7");
					return "0 " + String.format("%.2f", bal) + " " + String.format("%.2f", bal+amount);
	      }
	        catch( SQLException e )
			{
				System.err.println( e.getMessage() );
				return "1";
			}
	}

	@Override
	public String showBalance( String accountId )
	{
		try( Statement statement = _connection.createStatement() )
		{
		    ResultSet resultSet = statement.executeQuery( "SELECT balance FROM Accounts WHERE accountID = \'" + accountId + "\'" );
			int count = 0;
		    double bal = 0;
			    while( resultSet.next() ){
				count++;
				bal = resultSet.getDouble(1);
			    }
			    if (count == 0){
				return "1 account not found";
			    } else {
				String balString = String.format("%.2f", bal);
				return "0 " + balString;
			    }
		}
		catch( Exception e )
		{
			System.err.println( e.getMessage() );
			return "1";
		}
	}

	@Override
	public String topUp( String accountId, double amount )
	{
		if (amount <= 0)
		{
			return "1 Amount to transfer is invalid.";
		}
		int transactionId = 0;
		String linkedId = "";
		boolean firstTransaction = false;
		double deductableAmount = amount;
		int validTrans = 2;
		double linkedBalance = 0.0;
		double pocketBalance = 0.0;

		try (Statement statement = _connection.createStatement()) {
				if (!verifyAccountType(accountId, "Pocket"))
				{
					return "1 The account is not a pocket account.";
				}

				//System.out.println("topup0");
				ResultSet linkedAccount = statement.executeQuery("SELECT ownerID FROM PocketOwner WHERE pocketID = \'" + accountId + "\'");
				if( !linkedAccount.next() )
				{
					return "1 Pocket account id is invalid.";
				}
				linkedId = linkedAccount.getString(1); // TODO: check if it's 0 or 1
				firstTransaction = isFirstTransaction(accountId);
				if (firstTransaction)
				{
					deductableAmount += 5.00;
				}
				//System.out.println("topup1");

				validTrans = isValidTransaction(linkedId, deductableAmount);
				if (validTrans == 0)
					return "1";

				ResultSet transIds = statement.executeQuery( "SELECT nvl(max(transactionId), 0) as maxTransId FROM Transactions");
				if (!transIds.next())
				{
					return "1 No transactions but weird bug";
				}
				transactionId = transIds.getInt("maxTransId")+1;

				//System.out.println("topup2");
				ResultSet day = statement.executeQuery("SELECT * FROM CurrentDate");
				Date currDate = new Date(2011, 3, 1);
				if (day.next())
					currDate = day.getDate("currentdate");
				else
					return "1";
				String insertStatement = "INSERT INTO Transactions VALUES (?,?,?,?,?,?,?)";

				if (validTrans == 2)
				{
					executeTransaction(insertStatement, transactionId, linkedId, accountId, "topup_send", deductableAmount, 0);
					transactionId++;
				}
				executeTransaction(insertStatement, transactionId, accountId, linkedId, "topup_rec", amount, 0);

				ResultSet rs = statement.executeQuery("SELECT balance FROM Accounts WHERE accountID = \'" + linkedId + "\'");
				//System.out.println("topup3.5");
				rs.next();
				linkedBalance = rs.getFloat(1)-deductableAmount;

				rs = statement.executeQuery("SELECT balance FROM Accounts WHERE accountID = \'" + accountId + "\'");
				rs.next();
				pocketBalance = rs.getFloat(1)+amount;

				statement.executeQuery("UPDATE Accounts SET balance = balance - " + Double.toString(deductableAmount) + "WHERE accountId = \'" + linkedId + "\'");
				statement.executeQuery("UPDATE Accounts SET balance = balance + " + Double.toString(amount) + "WHERE accountId = \'" + accountId + "\'");

				return "0 " + String.format("%.2f", linkedBalance) + " " + String.format("%.2f", pocketBalance);
			}
				catch( SQLException e )
		{
			System.err.println( e.getMessage() );
			return "1";
		}
	}

	@Override
	public String payFriend( String from, String to, double amount )
	{
		if (from.trim().equals(to.trim()))
		{
			return "1 Cannot pay to the same account";
		}
		if (amount <= 0)
		{
			return "1 Amount to deposit is invalid.";
		}
		if (!verifyAccountType(from, "Pocket") || !verifyAccountType(to, "Pocket"))
		{
			return "1 At least one account is not a pocket account.";
		}

		int transactionId = 0;
		String linkedId = "";
		double deductableAmount = amount;
		double receivedAmount = amount;
		boolean fromFirstTransaction = false;
		boolean toFirstTransaction = false;
		int validTrans1 = 2;
		int validTrans2 = 2;
		double fromBalance = 0.0;
		double toBalance = 0.0;

		try (Statement statement = _connection.createStatement()) {

				if (isFirstTransaction(from))
				{
					deductableAmount += 5.00;
					fromFirstTransaction = true;
				}
				if (isFirstTransaction(to))
				{
					receivedAmount -= 5.00;
					toFirstTransaction = true;
				}

				validTrans1 = isValidTransaction(from, deductableAmount);
				if (validTrans1 == 0)
					return "1";

				if (receivedAmount < 0.00)
				{
					validTrans2 = isValidTransaction(to, (-1)*receivedAmount);
					if (validTrans2 == 0)
						return "1";
				}

				ResultSet transIds = statement.executeQuery( "SELECT nvl(max(transactionId), 0) as maxTransId FROM Transactions");
				if (!transIds.next())
				{
					return "1 No transactions but weird bug";
				}
				transactionId = transIds.getInt("maxTransId")+1;

				ResultSet day = statement.executeQuery("SELECT * FROM CurrentDate");
				Date currDate = new Date(2011, 3, 1);
				if (day.next())
					currDate = day.getDate("currentdate");
				else
					return "1";

				String insertStatement = "INSERT INTO Transactions VALUES (?,?,?,?,?,?,?)";
				//PreparedStatement pstmt = _connection.prepareStatement(insertStatement);

				if( fromFirstTransaction && validTrans1 == 2)
				{
					statement.executeQuery("UPDATE Accounts SET balance = balance - 5 WHERE accountId = \'" + from + "\'");
					executeTransaction(insertStatement, transactionId, from, null, "first_transaction_fee", 5.00, 0);
					transactionId++;
				}

				if( toFirstTransaction && validTrans2 == 2)
				{
					statement.executeQuery("UPDATE Accounts SET balance = balance - 5 WHERE accountId = \'" + to + "\'");
					executeTransaction(insertStatement, transactionId, to, null, "first_transaction_fee", 5.00, 0);
					transactionId++;
				}

				if (validTrans1 == 2)
				{
					executeTransaction(insertStatement, transactionId, from, to, "payfriend_send", amount, 0);
					transactionId++;
				}
				if (validTrans2 == 2)
				{
					executeTransaction(insertStatement, transactionId, to, from, "payfriend_rec", amount, 0);
				}

				statement.executeQuery("UPDATE Accounts SET balance = balance + " + Double.toString(amount) + "WHERE accountId = \'" + to + "\'");
				statement.executeQuery("UPDATE Accounts SET balance = balance - " + Double.toString(amount) + "WHERE accountId = \'" + from + "\'");

				ResultSet rs = statement.executeQuery("SELECT balance FROM Accounts WHERE accountId = \'" + from + "\'");
				rs.next();
				fromBalance = rs.getFloat(1);
				rs = statement.executeQuery("SELECT balance FROM Accounts WHERE accountId = \'" + to + "\'");
				rs.next();
				toBalance = rs.getFloat(1);

				return "0 " + String.format("%.2f", fromBalance) + " " + String.format("%.2f", toBalance);

			}
		catch( SQLException e )
		{
			System.err.println( e.getMessage() );
			return "1";
		}
	}

	public String purchase(String id, double amount)
	{
		int validTrans = 2;
		double bal = 0.0;
		try (Statement statement = _connection.createStatement()) {
			double deductableAmount = amount;
			if (amount <= 0)
			{
				return "1 Amount to spend is invalid.";
			}

			if (!verifyAccountType(id, "Pocket"))
			{
				return "1 Account is not a pocket account.";
			}
			if (isFirstTransaction(id))
				deductableAmount += 5.00;

			int transactionId = 0;
			ResultSet balance = statement.executeQuery( "SELECT balance FROM Accounts WHERE accountID = \'" + id + "\'");
			if( !balance.next() )
			{
				return "1 Account is invalid.";
			}
			bal = balance.getDouble(1);

			validTrans = isValidTransaction(id, deductableAmount);
			if (validTrans == 0)
				return "1";

			else
			{
				ResultSet transIds = statement.executeQuery( "SELECT nvl(max(transactionId), 0) as maxTransId FROM Transactions");
				if (!transIds.next())
				{
					return "1 No transactions but weird bug";
				}
				transactionId = transIds.getInt("maxTransId")+1;
				statement.executeQuery("UPDATE Accounts SET balance = balance - " + Double.toString(deductableAmount) + "WHERE accountId = \'" + id + "\'");
				String insertStatement = "INSERT INTO Transactions VALUES (?,?,?,?,?,?,?)";
				if (validTrans == 2)
					executeTransaction(insertStatement, transactionId, id, null, "purchase", deductableAmount, 0);
				return "0 " + String.format("%.2f", bal) + " " + String.format("%.2f", bal-deductableAmount);
			}
		}
		catch( Exception e )
		{
		    System.err.println( e.getMessage() );
		    return "1";
		}
	}

	public String collect(String id, double amount)
	{
		int validTrans = 2;
		double fromBalance = 0.0;
		double toBalance = 0.0;
		try (Statement statement = _connection.createStatement()) {
			double deductableAmount = amount;
			if (amount <= 0)
			{
				return "1 Amount to collect is invalid.";
			}

			if (!verifyAccountType(id, "Pocket"))
			{
				return "1 Account is not a pocket account.";
			}

			deductableAmount*=1.03; //add 3% fee

			if (isFirstTransaction(id))
				deductableAmount += 5.00;

			int transactionId = 0;
			validTrans = isValidTransaction(id, deductableAmount);
			if (validTrans == 0)
				return "1";

			else
			{
				ResultSet transIds = statement.executeQuery( "SELECT nvl(max(transactionId), 0) as maxTransId FROM Transactions");
				if (!transIds.next())
				{
					return "1 No transactions but weird bug";
				}
				transactionId = transIds.getInt("maxTransId")+1;

				ResultSet accountId = statement.executeQuery("SELECT ownerID FROM PocketOwner WHERE pocketID = \'" + id + "\'");
				if (!accountId.next())
				{
					return "1 Linked account does not exist -- weird bug";
				}
				String mainId = accountId.getString(1);
				statement.executeQuery("UPDATE Accounts SET balance = balance - " + Double.toString(deductableAmount) + "WHERE accountId = \'" + id + "\'");
				statement.executeQuery("UPDATE Accounts SET balance = balance + " + Double.toString(amount) + "WHERE accountId IN \'" + mainId + "\'");
				String insertStatement = "INSERT INTO Transactions VALUES (?,?,?,?,?,?,?)";
				if (validTrans == 2)
				{
					executeTransaction(insertStatement, transactionId, id, mainId, "collect_send", deductableAmount, 0);
					transactionId++;
				}
				executeTransaction(insertStatement, transactionId, mainId, id, "collect_rec", amount, 0);

				ResultSet rs = statement.executeQuery("SELECT balance FROM Accounts WHERE accountId = \'" + id + "\'");
				rs.next();
				fromBalance = rs.getFloat(1);
				rs = statement.executeQuery("SELECT balance FROM Accounts WHERE accountId = \'" + mainId + "\'");
				rs.next();
				toBalance = rs.getFloat(1);

				return "0 " + String.format("%.2f", fromBalance) + " " + String.format("%.2f", toBalance);
			}
		}
		catch( Exception e )
		{
		    System.err.println( e.getMessage() );
		    return "1";
		}
	}

	public String withdrawal(String id, double amount)
	{
		int validTrans = 2;
		double bal = 0.0;

		try (Statement statement = _connection.createStatement()) {
			if (amount <= 0)
			{
				return "1 Amount to withdraw is invalid.";
			}

			if (verifyAccountType(id, "Pocket"))
			{
				return "1 Account is not a checkings or savings account.";
			}

			int transactionId = 0;

			ResultSet balance = statement.executeQuery( "SELECT balance FROM Accounts WHERE accountID = \'" + id + "\'");
			if( !balance.next() )
			{
				return "1 Account is invalid.";
			}
			bal = balance.getDouble(1);

			validTrans = isValidTransaction(id, amount);
			if (validTrans == 0)
				return "1";

			else
			{
				ResultSet transIds = statement.executeQuery( "SELECT nvl(max(transactionId), 0) as maxTransId FROM Transactions");
				if (!transIds.next())
				{
					return "1 No transactions but weird bug";
				}
				transactionId = transIds.getInt("maxTransId")+1;
				statement.executeQuery("UPDATE Accounts SET balance = balance - " + Double.toString(amount) + "WHERE accountId = \'" + id + "\'");
				String insertStatement = "INSERT INTO Transactions VALUES (?,?,?,?,?,?,?)";
				if (validTrans == 2)
					executeTransaction(insertStatement, transactionId, id, null, "withdrawal", amount, 0);
				return "0 " + String.format("%.2f", bal) + " " + String.format("%.2f", bal-amount);
			}
		}
		catch( Exception e )
		{
		    System.err.println( e.getMessage() );
		    return "1";
		}
	}

	public String wire(String from, String to, double amount)
	{
		int validTrans = 2;
		double fromBalance = 0.0;
		double toBalance = 0.0;
		if (from.trim().equals(to.trim()))
		{
			return "1 Cannot wire to the same account";
		}

		try (Statement statement = _connection.createStatement()) {
			double deductableAmount = amount;
			if (amount <= 0)
			{
				return "1 Amount to wire is invalid.";
			}

			if (verifyAccountType(from, "Pocket") || verifyAccountType(to, "Pocket"))
			{
				return "1 At least one account is not a checkings or savings account.";
			}

			deductableAmount*=1.02; //add 2% fee

			int transactionId = 0;

			validTrans = isValidTransaction(from, deductableAmount);
			if (validTrans == 0)
				return "1";

			else
			{
				ResultSet transIds = statement.executeQuery( "SELECT nvl(max(transactionId), 0) as maxTransId FROM Transactions");
				if (!transIds.next())
				{
					return "1 No transactions but weird bug";
				}
				transactionId = transIds.getInt("maxTransId")+1;

				statement.executeQuery("UPDATE Accounts SET balance = balance - " + Double.toString(deductableAmount) + "WHERE accountId = \'" + from + "\'");
				statement.executeQuery("UPDATE Accounts SET balance = balance + " + Double.toString(amount) + "WHERE accountId =  \'" + to + "\'");
				String insertStatement = "INSERT INTO Transactions VALUES (?,?,?,?,?,?,?)";
				if (validTrans == 2)
				{
					executeTransaction(insertStatement, transactionId, from, to, "wire_send", deductableAmount, 0);
					transactionId++;
				}
				executeTransaction(insertStatement, transactionId, to, from, "wire_rec", amount, 0);

				ResultSet rs = statement.executeQuery("SELECT balance FROM Accounts WHERE accountId = \'" + from + "\'");
				rs.next();
				fromBalance = rs.getFloat(1);
				rs = statement.executeQuery("SELECT balance FROM Accounts WHERE accountId = \'" + to + "\'");
				rs.next();
				toBalance = rs.getFloat(1);

				return "0 " + String.format("%.2f", fromBalance) + " " + String.format("%.2f", toBalance);
			}
		}
		catch( Exception e )
		{
		    System.err.println( e.getMessage() );
		    return "1";
		}
	}

	public String transfer(String from, String to, double amount)
	{
		double fromBalance = 0.0;
		double toBalance = 0.0;
		int validTrans = 2;
		if (from.trim().equals(to.trim()))
		{
			return "1 Cannot transfer to the same account";
		}
		try (Statement statement = _connection.createStatement()) {
			double deductableAmount = amount;
			if (amount <= 0 || amount > 2000)
			{
				return "1 Amount to transfer is invalid.";
			}

			if (verifyAccountType(from, "Pocket") || verifyAccountType(to, "Pocket"))
			{
				return "1 At least one account is not a checkings or savings account.";
			}

			int transactionId = 0;

			validTrans = isValidTransaction(from, amount);
			if (validTrans == 0)
				return "1";

			else
			{
				ResultSet transIds = statement.executeQuery( "SELECT nvl(max(transactionId), 0) as maxTransId FROM Transactions");
				if (!transIds.next())
				{
					return "1 No transactions but weird bug";
				}
				transactionId = transIds.getInt("maxTransId")+1;

				statement.executeQuery("UPDATE Accounts SET balance = balance - " + Double.toString(amount) + "WHERE accountId = \'" + from + "\'");
				statement.executeQuery("UPDATE Accounts SET balance = balance + " + Double.toString(amount) + "WHERE accountId =  \'" + to + "\'");
				String insertStatement = "INSERT INTO Transactions VALUES (?,?,?,?,?,?,?)";
				if (validTrans == 2)
				{
					executeTransaction(insertStatement, transactionId, from, to, "transfer_send", amount, 0);
					transactionId++;
				}
				executeTransaction(insertStatement, transactionId, to, from, "transfer_rec", amount, 0);

				ResultSet rs = statement.executeQuery("SELECT balance FROM Accounts WHERE accountId = \'" + from + "\'");
				rs.next();
				fromBalance = rs.getFloat(1);
				rs = statement.executeQuery("SELECT balance FROM Accounts WHERE accountId = \'" + to + "\'");
				rs.next();
				toBalance = rs.getFloat(1);

				return "0 " + String.format("%.2f", fromBalance) + " " + String.format("%.2f", toBalance);
			}
		}
		catch( Exception e )
		{
		    System.err.println( e.getMessage() );
		    return "1";
		}
	}

	public String writeCheck(String id, double amount)
	{
		int validTrans = 2;
		try (Statement statement = _connection.createStatement()) {
			if (amount <= 0)
			{
				return "1 Amount to subtract is invalid.";
			}

			if (!verifyAccountType(id, "InterestChecking") && !verifyAccountType(id, "StudentChecking"))
			{
				return "1 Account is not a checkings account.";
			}

			int transactionId = 0;
			int checkNumber = 0;

			validTrans = isValidTransaction(id, amount);
			if (validTrans == 0)
				return "1";

			else
			{
				ResultSet transIds = statement.executeQuery( "SELECT nvl(max(transactionId), 0) as maxTransId FROM Transactions");
				if (!transIds.next())
				{
					return "1 No transactions but weird bug";
				}
				transactionId = transIds.getInt("maxTransId")+1;
				ResultSet checkNumbers = statement.executeQuery( "SELECT nvl(max(checkNumber), 0) as maxCheckNumber FROM Transactions");
				if (!checkNumbers.next())
				{
					return "1 No transactions but weird bug - check numbers";
				}
				checkNumber = checkNumbers.getInt("maxCheckNumber")+1;

				statement.executeQuery("UPDATE Accounts SET balance = balance - " + Double.toString(amount) + "WHERE accountId = \'" + id + "\'");
				String insertStatement = "INSERT INTO Transactions VALUES (?,?,?,?,?,?,?)";
				if (validTrans == 2)
					executeTransaction(insertStatement, transactionId, id, null, "check", amount, checkNumber);
				return "0";
			}
		}
		catch( Exception e )
		{
		    System.err.println( e.getMessage() );
		    return "1";
		}
	}

	@Override
	public String listClosedAccounts()
	{
		try( Statement statement = _connection.createStatement() ){
	    ResultSet resultSet = statement.executeQuery("SELECT accountID FROM Accounts WHERE isOpen = 0");
	    String output = "0";
	    while(resultSet.next()){
					output = output + " " + resultSet.getString(1);
	    }
	    return output;
		}
		catch( Exception e )
		{
		    System.err.println( e.getMessage() );
		    return "1";
		}
	}
	public String deleteClosedAccounts(){
		if (!isLastOfMonth()){
		    return "1 not last of month";
		}
		try( Statement statement = _connection.createStatement() ){
		    ResultSet resultSet = statement.executeQuery("DELETE FROM Accounts WHERE isOpen = 0");
				ResultSet set2 = statement.executeQuery("DELETE FROM Accounts WHERE accountType = \'Pocket\' and accountID not in (Select P.pocketID from PocketOwner P)");
		    ResultSet set3 = statement.executeQuery("DELETE FROM Customers WHERE taxID NOT IN (Select O.taxID from Owns O)");
				return "0";
		}
		catch( Exception e )
		{
		    System.err.println( e.getMessage() );
		    return "1";
		}
    }
	public String deleteTransactions(){
		if (!isLastOfMonth()){
		    return "1 not last of month";
		}
		try (Statement statement = _connection.createStatement()){
		    ResultSet resultSet = statement.executeQuery("DELETE FROM Transactions");
		    return "0";
		}
		catch( Exception e )
		{
		    System.err.println( e.getMessage() );
		    return "1";
		}
  }
    public String getDTER(){
        if (!isLastOfMonth()){
            return "1 not last of month";
        }
        try (Statement statement = _connection.createStatement()){
            Date currentDate = getCurrentDate();
            String start = "01-" + monthStr[currentDate.getMonth()] + "-" + currentDate.getYear();
            String end = currentDate.getDate() + "-" + monthStr[currentDate.getMonth()] + "-" + currentDate.getYear();
            //ResultSet resultSet = statement.executeQuery("Select O2.taxID, Sum(O2.AccTotal) From(Select O.taxID, O.accountID, T2.AccTotal From Owns O INNER JOIN (Select T.accountID, Sum(T.amount) as AccTotal from Transactions T where (T.transactionType = \'deposit\' or T.transactionType = \'transfer_rec\' or T.transactionType = \'wire_rec\') and T.transactionDate BETWEEN \'" + start + "\' AND \'" + end + "\' group by accountID) T2 ON O.accountID = T2.accountID) O2 Group By O2.taxID Having Sum(O2.AccTotal) > 10000");
	    //ResultSet resultSet = statement.executeQuery("Select O2.taxID, Sum(O2.AccTotal) From (Select O.taxID, O.accountID, T2.AccTotal From Owns O INNER JOIN (Select T.accountID, Sum(T.amount) as AccTotal from Transactions T where (T.transactionType = \'deposit\' or T.transactionType = \'transfer_rec\' or T.transactionType = \'wire_rec\') group by accountID) T2 ON O.accountID = T2.accountID) O2 Group By O2.taxID Having Sum(O2.AccTotal) > 10000");
            String output = "0";
	    ResultSet custSet = statement.executeQuery("Select C.taxID from Customers C");
	    while (custSet.next()){
		String taxID = custSet.getString(1).trim();
		double total = 0;
		Statement statement2 = _connection.createStatement();
		ResultSet accSet = statement2.executeQuery("Select A.accountID from Accounts A WHERE A.accountID in (Select O.accountID from Owns O where O.taxID = \'" + taxID + "\')");
		while (accSet.next()){
		    String accID = accSet.getString(1);
		    Statement statement3 = _connection.createStatement();
		    ResultSet sumSet = statement3.executeQuery("Select Sum(T.amount) from Transactions T WHERE T.accountID = \'" + accID + "\' and  (T.transactionType = \'deposit\' or T.transactionType = \'transfer_rec\' or T.transactionType = \'wire_rec\')");
		    while (sumSet.next()){
			total += sumSet.getDouble(1);
		    }
		}
		if (total >= 10000){
		    output += "\n" + taxID;
		}
	    }
            return output;
        }
        catch( Exception e )
        {
            System.err.println( e.getMessage() );
            return "1";
        }
    }
    public String getMonthlyStatement(String taxID){

        if (!isLastOfMonth()){
            return "1 not last of month";
        }
        try (Statement statement = _connection.createStatement()){
            String output = "0 \n";
            Date currentDate = getCurrentDate();
            String startDate = "01-" + monthStr[currentDate.getMonth()] + "-" + currentDate.getYear();
            String endDate = currentDate.getDate() + "-" + monthStr[currentDate.getMonth()] + "-" + currentDate.getYear();
            String accountsQuery = "Select O.accountID from Owns O Where O.taxID = \'" + taxID + "\'";
            ResultSet accNumSet = statement.executeQuery(accountsQuery);
            double totalBalance = 0;
            while(accNumSet.next()){
								Statement statement2 = _connection.createStatement();
                String thisAccNum = accNumSet.getString(1);
                String accOutput = thisAccNum + "\n";
                String transactQuery = "Select * from Transactions T where T.accountID = \'" + thisAccNum + "\'";
                ResultSet transactionSet = statement2.executeQuery(transactQuery);
                int colNum = transactionSet.getMetaData().getColumnCount();
                String finalBalance = showBalance(thisAccNum).substring(2);
                double initialBalance = Double.parseDouble(finalBalance);
                totalBalance += initialBalance;
                while (transactionSet.next()){
                    for (int i = 1; i <= colNum; i++){

			String temp = transactionSet.getString(i);
			if (temp != null){
			    temp = temp.trim();
			}
                        accOutput += temp + " ";
                    }
                    accOutput += "\n";
                    String transactionType = transactionSet.getString(5).trim();
                    if (transactionType.substring(transactionType.length() - 4).equals("send") || transactionType.equals("withdrawal") || transactionType.equals("purchase") || transactionType.equals("check") ||  transactionType.equals("first_transaction_fee")){
                        initialBalance += transactionSet.getDouble(6);
                    } else if (transactionType.substring(transactionType.length() - 4).equals("rec") || transactionType.equals("deposit") || transactionType.equals("interest")){
                        initialBalance -= transactionSet.getDouble(6);
                    }
                }
                accOutput += "Initial balance: " + String.format("%.2f", initialBalance) + "\n";
                accOutput += "Final balance: " + finalBalance + "\n\n";
                output += accOutput;
            }
            if (totalBalance > 100000){
                output += "Insurance limit reached. Total exceeds $100,000.";
            }
            return output;
        }
        catch( Exception e )
        {
            System.err.println( e.getMessage() );
            return "1";
        }
    }

    public String getCustomerReport(String taxId){
        try (Statement statement = _connection.createStatement()){
            String output = "0 \n";
						Date currentDate = getCurrentDate();
            String endDate = currentDate.getDate() + "-" + monthStr[currentDate.getMonth()] + "-" + currentDate.getYear();
            String accountsQuery = "Select O.accountID from Owns O Where O.taxID = \'" + taxId + "\'";
            ResultSet accNumSet = statement.executeQuery(accountsQuery);
            while(accNumSet.next()){
		Statement statement2 = _connection.createStatement();
                String thisAccNum = accNumSet.getString(1);
                output += thisAccNum;
                String accQuery = "Select A.isOpen from Accounts A where A.accountID = \'" + thisAccNum + "\'";
                ResultSet accSet = statement2.executeQuery(accQuery);
                while (accSet.next()){
                    if (accSet.getInt(1) == 1){
                        output += " Open\n";
                    } else {
                        output += " Closed\n";
                    }
                }
            }
            return output;
        }
        catch( Exception e )
        {
            System.err.println( e.getMessage() );
	    //System.err.println("ablong");
            return "1";
        }
    }
	public String addInterest(){
		if (!isLastOfMonth()){
			return "1 is not last of month";
		}

		try (Statement statement = _connection.createStatement()){
		    ResultSet rateSet = statement.executeQuery("Select * from InterestRates I where I.rate > 0");
		    double interestCheckingRate = 0;
		    double savingsRate = 0;
		    while (rateSet.next()){
			if (rateSet.getString(1).trim().equals("InterestChecking")){
			    interestCheckingRate = rateSet.getDouble(2);
			} else if (rateSet.getString(1).trim().equals("Savings")){
			    savingsRate = rateSet.getDouble(2);
			}
		    }
			ResultSet interestDates = statement.executeQuery("Select * from InterestAdded");
			Date currentDate = getCurrentDate();

			while (interestDates.next()){
			    System.out.println(interestDates.getString(1));
				if (interestDates.getDate(1).compareTo(currentDate) == 0){
					return "1 already added interest this month";
				}
			}
			ResultSet accountSet = statement.executeQuery("SELECT A.accountID, A.balance, A.accountType from Accounts A where A.isOpen = 1 and (A.accountType = \'Savings\' or A.accountType = \'InterestChecking\')");
			Date firstDate = new Date(currentDate.getYear(), currentDate.getMonth(), 1);
			int totalDays = currentDate.getDate();
			while (accountSet.next()){
			    Statement statement2 = _connection.createStatement();
				double finalBalance = accountSet.getDouble(2);
				String accId = accountSet.getString(1);
				double interestRate = 0;
			        String accountType = accountSet.getString(3).trim();
				//System.out.println("Account type: " + accountSet.getString(3));
				if (accountType.equals("InterestChecking")){
				    interestRate = interestCheckingRate;
				} else if (accountType.equals("Savings")){
				    interestRate = savingsRate;
				}
				//System.out.println("Made it past, interestRate = " + interestRate);
				double averageBalance = finalBalance;
				//ResultSet transacts = statement2.executeQuery("SELECT T.transactionDate, T.transactionType, T.amount FROM Transactions T WHERE T.accountID = \'" + accId + "\' and T.transactionDate >= \'" + formatDateToSQL(firstDate) + "\' and T.transactionDate <= \'" + formatDateToSQL(currentDate) + "\' order by T.transactionDate desc");
				ResultSet transacts = statement2.executeQuery("SELECT T.transactionDate, T.transactionType, T.amount FROM Transactions T WHERE T.accountID = \'" + accId + "\' order by T.transactionDate desc");
				int currentDays = totalDays;
				while (transacts.next()){
					double averageInfluence = transacts.getDouble(3)*(totalDays-currentDays);
					String transactionType = transacts.getString(2).trim();
					if (transactionType.substring(transactionType.length() - 4).equals("send") || transactionType.equals("withdrawal") || transactionType.equals("purchase") || transactionType.equals("check") || transactionType.equals("first_transaction_fee")){
						averageBalance += averageInfluence;
					} else {
						averageBalance -= averageInfluence;
					}
				}
				double newBalance = averageBalance*interestRate + finalBalance;
				//System.out.println("" + newBalance);
				Statement statement3 = _connection.createStatement();
				ResultSet interestSet = statement3.executeQuery("UPDATE Accounts SET balance = " + newBalance + " where accountID = \'" + accId + "\'");
				ResultSet transIds = statement3.executeQuery( "SELECT nvl(max(transactionId), 0) as maxTransId FROM Transactions");
				if (!transIds.next())
				{
					return "1 No transactions but weird bug";
				}
				int transactionId = transIds.getInt("maxTransId")+1;
				String insertStatement = "INSERT INTO Transactions VALUES (?,?,?,?,?,?,?)";
				executeTransaction(insertStatement, transactionId, accId, null, "interest", averageBalance*interestRate, 0);


			}

			ResultSet addInterestDate = statement.executeQuery("Insert into InterestAdded values (\'" + formatDateToSQL(currentDate) + "\')");
					return "0";
			}
		catch( Exception e )
		{
				System.err.println( e.getMessage() );
				return "1";
		}
}

public boolean verifyPin(String pin){
	String hashedPin = hashPin(pin);
	try (Statement statement = _connection.createStatement()){
					ResultSet account = statement.executeQuery("Select C.PIN from Customers C WHERE C.taxID = \'" + currentTaxID + "\'");
		while (account.next()){
			if (account.getString(1).replaceAll("\\s+","").equals(hashedPin)){
				return true;
			}
		}
		return false;
		}
		catch( Exception e )
		{
				System.err.println( e.getMessage() );
				return false;
		}
}

public boolean setPIN(String oldPIN, String newPIN)
{
	if (!verifyPin(oldPIN))
	{
		System.out.println("Verification failed. PIN is not changed.");
		return false;
	}
	String hashedPin = hashPin(newPIN);
	//System.out.println(hashedPin);

	try (Statement statement = _connection.createStatement()){
		statement.executeQuery("UPDATE Customers SET PIN = \'" + hashedPin + "\' WHERE taxID = \'" + currentTaxID + "\'");
		System.out.println("PIN successfully changed.");
		return true;
	}
	catch( Exception e )
	{
			System.err.println( e.getMessage() );
			return false;
	}
}

public String changeInterestRate(AccountType accountType, double newRate){
		String type = "";
		switch (accountType)
		{
			case INTEREST_CHECKING:
				type = "InterestChecking";
				break;
			case SAVINGS:
				type = "Savings";
				break;
			default:
				System.out.println("Wrong account type.");
				return "1";
		}
		if (newRate < 0){
			System.out.println("Invalid rate.");
		    return "1";
		}
		try (Statement statement = _connection.createStatement()){
		    ResultSet resultSet = statement.executeQuery("Update InterestRates SET rate = " + newRate + " WHERE accountType = \'" + type + "\'");
		    return "0";
		}
		catch( Exception e )
		{
		    System.err.println( e.getMessage() );
		    return "1";
		}
	}

	private String hashPin(String pin){
		String combined = currentTaxID + pin;
		return Integer.toString(combined.hashCode());
	}

	private String formatDateToSQL(Date inputDate){
		return inputDate.getDate() + "-" + monthStr[inputDate.getMonth()] + "-" + (inputDate.getYear()+1900);
	}

	private boolean isLastOfMonth(){
		//TODO: check for leap years?
		try (Statement statement = _connection.createStatement()){
		    ResultSet resultSet = statement.executeQuery("SELECT * FROM CurrentDate");
		    int month = 0;
		    int day = 0;
		    while(resultSet.next()){
					month = resultSet.getDate(1).getMonth();
					day = resultSet.getDate(1).getDate();
		    }
		    if (month == 1 && day == 28){
						return true;
		    } else if ((month == 3 || month == 5 || month == 8 || month == 10) && day == 30){
						return true;
		    } else if (day == 31){
						return true;
		    }
		    return false;
		}
		catch( Exception e )
		{
		    System.err.println( e.getMessage() );
		    return false;
		}
	}

    private Date getCurrentDate(){
        try (Statement statement = _connection.createStatement()){
            ResultSet resultSet = statement.executeQuery("Select * from CurrentDate");
            while(resultSet.next()){
                return resultSet.getDate(1);
            }
        }
        catch( Exception e )
        {
            System.err.println( e.getMessage() );
            return null;
        }
        return null;
    }

		//checks that the two accounts have the current customer as an owner
		private boolean isOwnerOf(String account, String id)
		{
			try (Statement statement = _connection.createStatement()){
					ResultSet resultSet = statement.executeQuery("SELECT * FROM Owns WHERE taxID = \'" + id + "\' AND accountID = \'" + account + "\'");
					if (!resultSet.next())
					{
						return false;
					}
					return true;
			}
			catch( Exception e )
			{
					System.err.println( e.getMessage() );
					return false;
			}
		}

		//checks if the amount deducted is valid
		//amount exceeds balance: invalid transaction, returns 0
		//amount equals balance or leaves $0.01: valid transaction but the account is closed, returns 1
		//otherwise: deducting amount is a valid transaction does not trigger any special cases, returns 2
		private int isValidTransaction(String id, double amount)
		{
			String type = "";
			try (Statement statement = _connection.createStatement()){
				ResultSet account = statement.executeQuery( "SELECT balance, accountType FROM Accounts WHERE accountID = \'" + id + "\'");
				if( !account.next() )
				{
					System.out.println("Account is invalid.");
					return 0;
				}
				if (account.getDouble("balance") - amount < 0) //TODO: not sure if getDouble works, check
				{
					System.out.println("Not enough balance in the account.");
					return 0;
				}
				else if (account.getDouble("balance") - amount <= 0.01)
				{
					type = account.getString("accountType").replaceAll("\\s+","");
					statement.executeQuery("UPDATE Accounts SET isOpen = 0 WHERE accountId = \'" + id + "\'");
					if (!account.getString("accountType").trim().equals("Pocket"))
					{
						statement.executeQuery("UPDATE Accounts SET isOpen = 0 WHERE accountId IN (SELECT pocketID FROM PocketOwner WHERE ownerID = \'" + id + "\')");
					}
					statement.executeQuery("DELETE FROM Transactions WHERE accountID = \'" + id + "\'");
					return 1;
				}
				return 2;
			}
			catch( Exception e )
			{
					System.err.println( e.getMessage() );
					return 0;
			}
		}

		private boolean verifyAccountType(String id, String type)
		{
			//System.out.println("Verifying account type: " + id + " " + type + ".");
			try( Statement statement = _connection.createStatement() ){
		    ResultSet resultSet = statement.executeQuery("SELECT accountType FROM Accounts WHERE accountID = \'" + id + "\'");
		    if(resultSet.next() && resultSet.getString(1).replaceAll("\\s+","").equals(type)){
					return true;
		    }
		    return false;
			}
			catch( Exception e )
			{
			    System.err.println( e.getMessage() );
			    return false;
			}
		}

		//TODO: maybe change return type to int? bc there's 3 cases
		private boolean isFirstTransaction(String id)
		{
			int transactionId = 0;

			try (Statement statement = _connection.createStatement())
			{
					ResultSet resultSet = statement.executeQuery("SELECT * FROM Transactions WHERE AccountID = \'" + id + "\'");
					if (!resultSet.next())
					{
						return true;
					}
					return false;
			}
			catch( Exception e )
			{
					System.err.println( e.getMessage() );
					return false;
			}
		}

		private void executeTransaction(String statement, int transId, String account1, String account2, String type, double amt, int checkNo)
		{
			//System.out.println("executing transaction");
	    try{
	    	Statement statement2 = _connection.createStatement();
				ResultSet day = statement2.executeQuery("SELECT * FROM CurrentDate");
				Date currDate = new Date(2011, 3, 1);
				if (day.next())
					currDate = day.getDate("currentdate");

				PreparedStatement pstmt = _connection.prepareStatement(statement);
				pstmt.setInt(1, transId);
				pstmt.setString(2, account1);
				pstmt.setString(3, account2);
				pstmt.setDate(4, currDate);
				pstmt.setString(5, type);
				pstmt.setFloat(6, (float)amt);
				pstmt.setInt(7, checkNo);
				pstmt.execute();
	    } catch (Exception e){
					System.err.println(e.getMessage());
	    }
	}
}
