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
			try( ResultSet resultSet = statement.executeQuery( "SELECT owner, table_name FROM all_tables" ) )
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
		initializeSystem();
		dropTables();
		createTables();

		Scanner in = new Scanner(System.in);
    int choice = 0;
		boolean exit = false;

		int type = 0;
		AccountType accountType;
		double interestRate = 0.0;

		do {

			System.out.println("Choose one of the following options:");
			System.out.println("0: Show customer interface");
			System.out.println("1: Show bank teller interface");
			System.out.println("2: Set system date");
			System.out.println("3: Set interest rate");
			System.out.println("4: Exit application");
			choice = in.nextInt();

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
					changeInterestRate(accountType, interestRate);
					break;
				case 4:
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
			System.out.println("Choose one of the following options:");
			System.out.println("0: Make a deposit");
			System.out.println("1: Make a top-up");
			System.out.println("2: Withdraw money");
			System.out.println("3: Make a purchase");
			System.out.println("4: Make a transfer");
			System.out.println("5: Collect money from a pocket account");
			System.out.println("6: Wire money to another account");
			System.out.println("7: Pay a friend");
			System.out.println("8: Change PIN");
			System.out.println("9: Return to main menu");

			choice = in.nextInt();

			switch(choice)
			{
				case 0:
					System.out.print("Please input the ID of the account you would like to deposit to: ");
					account1 = in.nextLine();
					System.out.print("Please input the amount you would like to deposit: ");
					amount = in.nextDouble();
					System.out.println(deposit(account1, amount));
					break;
				case 1:
					System.out.print("Please input the ID of the account you would like to make a top-up to: ");
					account1 = in.nextLine();
					System.out.print("Please input the amount you would like to top-up: ");
					amount = in.nextDouble();
					System.out.println(topUp(account1, amount));
					break;
				case 2:
					System.out.print("Please input the ID of the account you would like to withdraw from: ");
					account1 = in.nextLine();
					System.out.print("Please input the amount you would like to withdraw: ");
					amount = in.nextDouble();
					System.out.println(withdrawal(account1, amount));
					break;
				case 3:
					System.out.print("Please input the ID of the account you would like to make a purchase with: ");
					account1 = in.nextLine();
					System.out.print("Please input the amount you would like to spend: ");
					amount = in.nextDouble();
					System.out.println(purchase(account1, amount));
					break;
				case 4:
					System.out.print("Please input the ID of the account you would like to transfer from: ");
					account1 = in.nextLine();
					System.out.print("Please input the ID of the account you would like to transfer to: ");
					account2 = in.nextLine();
					System.out.print("Please input the amount you would like to transfer: ");
					amount = in.nextDouble();
					System.out.println(transfer(account1, account2, amount));
					break;
				case 5:
					System.out.print("Please input the ID of the account you would like to collect from: ");
					account1 = in.nextLine();
					System.out.print("Please input the amount you would like to collect: ");
					amount = in.nextDouble();
					System.out.println(collect(account1, amount));
					break;
				case 6:
					System.out.print("Please input the ID of the account you would like to wire money from: ");
					account1 = in.nextLine();
					System.out.print("Please input the ID of the account you would like to send money to: ");
					account2 = in.nextLine();
					System.out.print("Please input the amount you would like to send: ");
					amount = in.nextDouble();
					System.out.println(wire(account1, account2, amount));
					break;
				case 7:
					System.out.print("Please input the ID of the account you would like to send money from: ");
					account1 = in.nextLine();
					System.out.print("Please input the ID of the account you would like to send money to: ");
					account2 = in.nextLine();
					System.out.print("Please input the amount you would like to send: ");
					amount = in.nextDouble();
					System.out.println(payFriend(account1, account2, amount));
					break;
				case 8:
					System.out.print("Please input your current PIN: ");
					oldPIN = in.nextLine();
					System.out.print("Please input your new PIN: ");
					newPIN = in.nextLine();
					setPIN(oldPIN, newPIN);
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

	private void runBankTellerInterface()
	{
		Scanner in = new Scanner(System.in);
		int choice = 0;
		boolean exit = false;
		String account = "";
		String tid = "";
		String newOwner = "";
		String linkedId = "";
		String name = "";
		String address = "";
		double initialBalance = 0.0;
		double amount = 0.0;
		int type = 0;
		AccountType accountType = null;

		do {
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
					System.out.print("Please input the ID of the account to generate a monthly statement for: ");
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
					//System.out.print("Please input the account owners on separate lines, starting with the primary owner (enter 0 to indicate that there are no more owners): ");
					System.out.print("Please input the primary account owner's tax ID: ");
					newOwner = in.nextLine();
					//TODO: figure out multiple owner stuff
					/*do {
					newOwner = in.nextLine();

				} while (!newOwner.equals("0"));*/
					System.out.print("Please input the initial balance: ");
					initialBalance = in.nextDouble();
					if (type == 3)
					{
						System.out.print("Please input the account ID you would like to link the pocket account with: ");
						linkedId = in.nextLine();
						System.out.println(createPocketAccount(account, linkedId, initialBalance, newOwner));
					}
					else
					{
						System.out.print("Please input the primary account owner's name: ");
						name = in.nextLine();
						System.out.print("Please input the primary account owner's address: ");
						address = in.nextLine();
						System.out.println(createCheckingSavingsAccount(accountType, account, initialBalance, newOwner, name, address));
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
			System.out.println("a");
			statement.executeQuery( "DROP TABLE Transactions" );
			System.out.println("b");
			statement.executeQuery( "DROP TABLE CurrentDate" );
			System.out.println("c");
			statement.executeQuery( "DROP TABLE InterestAdded" );
			System.out.println("d");
			statement.executeQuery( "DROP TABLE PocketOwner" );
			System.out.println("e");
			statement.executeQuery( "DROP TABLE Customers" );
			System.out.println("f");
			statement.executeQuery( "DROP TABLE Accounts" );
			System.out.println("g");
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
				String[] statements = new String[7];
				statements[0] = "CREATE TABLE Customers (taxID CHAR(9), name CHAR(20), address CHAR(50), PIN CHAR(50), PRIMARY KEY (taxID))";
				statements[1] = "CREATE TABLE Accounts (accountID CHAR(50), interestRate REAL, branchName CHAR(20), balance REAL, isOpen NUMBER(1,0), accountType CHAR(20), PRIMARY KEY (accountID))";
				statements[2] = "CREATE TABLE Owns (taxID CHAR(50), accountID CHAR(50), isPrimary NUMBER(1,0), PRIMARY KEY (taxID, accountID), FOREIGN KEY (taxID) REFERENCES Customers ON DELETE CASCADE, FOREIGN KEY (accountID) REFERENCES Accounts ON DELETE CASCADE)";
				statements[3] = "CREATE TABLE Transactions (transactionID INTEGER, accountID CHAR(50), otherAccount CHAR(50), transactionDate DATE, transactionType CHAR(20), amount REAL, checkNumber INTEGER, PRIMARY KEY (transactionID), FOREIGN KEY (accountID) REFERENCES Accounts ON DELETE CASCADE)";
				statements[4] = "CREATE TABLE CurrentDate (currentdate Date)";
				statements[5] = "CREATE TABLE InterestAdded (dateAdded DATE)";
				statements[6] = "CREATE TABLE PocketOwner(pocketID CHAR(50), ownerID CHAR(50), PRIMARY KEY(pocketID, ownerID), FOREIGN KEY (pocketID) REFERENCES Accounts (accountID) ON DELETE CASCADE, FOREIGN KEY (ownerID) REFERENCES Accounts (accountID) ON DELETE CASCADE)";
				for (int i = 0; i < statements.length; i++){
				    ResultSet resultSet = statement.executeQuery( statements[i] );
						System.out.println(i);
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
			try {
			    Statement statement = _connection.createStatement();
			    ResultSet resultSet = statement.executeQuery("UPDATE CurrentDate SET currentdate = '" + dateStr + "'");
					return "0";
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
		switch (accountType)
		{
			case INTEREST_CHECKING:
				interestRate = 0.03;
				type = "InterestChecking";
				break;
			case SAVINGS:
				interestRate = 0.048;
				type = "Savings";
				break;
			case STUDENT_CHECKING:
				interestRate = 0.0;
				type = "StudentChecking";
				break;
			default:
				System.out.println("Wrong account type.");
				return "1";
		}

		try (Statement statement = _connection.createStatement())
		{
			System.out.println("create_saving_checking0");
			counter = 1;
			ResultSet resultSet = statement.executeQuery("SELECT * FROM Customers WHERE taxID = \'" + tin + "\'");
			System.out.println("create_saving_checking1");
			counter = 2;
			if( !resultSet.next() )
			{
				createCustomer(id, tin, name, address);
				System.out.println("create_saving_checking2");
				counter = 3;
			}
			else
			{
				statement.executeQuery("INSERT INTO Owns VALUES (\'" + tin + "\',\'" + id + "\',1)");
				System.out.println("create_saving_checking3");
				counter = 4;
			}

    	statement.executeQuery("INSERT INTO Accounts VALUES (\'" + id + "\',"+ interestRate + ", null, " + initialBalance + ", 1, \'" + type + "\')"); //1 near the end means account is open
			statement.executeQuery("INSERT INTO Owns VALUES (\'" + tin + "\',\'" + id + "\', 1)");
			//TODO: not sure if the above line is necessary
			System.out.println("create_saving_checking4");
			deposit(id, initialBalance);
    	return "0";
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

		try (Statement statement = _connection.createStatement()) {

			ResultSet resultSet = statement.executeQuery( "SELECT * FROM Owns WHERE taxID = \'" + tin + "\' and accountID = \'" + linkedId + "\'");
			ResultSet resultSet2 = statement.executeQuery( "SELECT isOpen, accountType FROM Accounts WHERE accountID = \'" + linkedId + "\'" );
			if( !resultSet.next() || resultSet2.getInt(1) == 0 || resultSet2.getString(2).equals("Pocket"))
			{
				System.out.println("Linked account is invalid.");
				return "1";
			}
			else
			{
				ResultSet balance = statement.executeQuery( "SELECT balance FROM Accounts WHERE accountID = \'" + linkedId + "\'");
				if( !balance.next() )
				{
					System.out.println("Linked account is invalid.");
					return "1";
				}
				if (balance.getDouble(1) - (initialTopUp + 5) <= 0.01) //TODO: not sure if getDouble works, check
				{
					System.out.println("Not enough balance in the linked account.");
					return "1";
				}
			}

			statement.executeQuery("INSERT INTO Accounts VALUES (\'" + id + "\',"+ interestRate + ", null, 0.0, 1, \'Pocket\')"); //1 near the end means account is open
			statement.executeQuery("INSERT INTO Owns VALUES (\'" + tin + "\',\'" + id + "\', 1)");
			statement.executeQuery("INSERT INTO PocketOwner VALUES (\'" + id + "\',\'"+ linkedId + "\')"); //1 near the end means account is open

			topUp(id, initialTopUp);
			return "0";
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
		String pin = "1717";
		String hashedPin = hashPin(pin);
		try (Statement statement = _connection.createStatement()) {

        statement.executeQuery("INSERT INTO Customers VALUES (\'" + tin + "\',\'" + name + "\',\'" + address + "\',\'" + hashedPin + "\')");
				ResultSet resultSet = statement.executeQuery("SELECT * FROM Accounts WHERE accountID = \'" + accountId + "\'");
				if (resultSet.next())
				{
					if (resultSet.getInt("isOpen") == 1) //if the account exists, insert a relation into owns. otherwise, we should take care of this in the account creation.
        		statement.executeQuery("INSERT INTO Owns VALUES (\'" + tin + "\',\'" + accountId + "\', 0)");
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
			if (amount <= 0)
			{
				System.out.println("Amount to deposit is invalid.");
				return "1";
			}
			int transactionId = 0;

			try (Statement statement = _connection.createStatement()) {
					System.out.println("deposit 1");
					ResultSet transIds = statement.executeQuery( "SELECT nvl(max(transactionId), 0) AS maxTransId FROM Transactions");
					System.out.println("deposit 2");
					if (transIds.next())
						transactionId = transIds.getInt("maxTransId")+1;
					else
						return "1";

					System.out.println("deposit 3");
					ResultSet day = statement.executeQuery("SELECT * FROM CurrentDate");
					System.out.println("deposit 4");
					Date currDate = new Date(2011, 3, 1);
					if (day.next())
						currDate = day.getDate("currentdate");
					else
						return "1";
					System.out.println("deposit 5");
					statement.executeQuery("UPDATE Accounts SET balance = balance + " + Double.toString(amount) + " WHERE accountId = \'" + accountId + "\'");
					System.out.println("deposit 6");
					String insertStatement = "INSERT INTO Transactions VALUES (?,\'?\',\'?\',?,\'?\',?,?)";
					//String insertStatement = "INSERT INTO Transactions VALUES (" + transactionId + ",\'" + accountId + "\', null,?, \'deposit\', " + amount + ", null)";
					executeTransaction(insertStatement, transactionId, accountId, null, "deposit", amount, 0);
					/*PreparedStatement pstmt = _connection.prepareStatement(insertStatement);
					pstmt.setDate(1, currDate);
					pstmt.execute();*/
					//statement.executeQuery("INSERT INTO Transactions VALUES (" + transactionId + ",\'" + accountId + "\', null, " + currDate + ", \'deposit\', " + amount + ", null)");
					System.out.println("deposit 7");
					return "0";
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
			System.out.println("Amount to deposit is invalid.");
			return "1";
		}
		int transactionId = 0;
		String linkedId = "";
		boolean firstTransaction = false;
		double deductableAmount = amount;

		try (Statement statement = _connection.createStatement()) {
				ResultSet linkedAccount = statement.executeQuery("SELECT ownerID FROM PocketOwner WHERE pocketID = \'" + accountId + "\'");
				if( !linkedAccount.next() )
				{
					System.out.println("Pocket account id is invalid.");
					return "1";
				}
				linkedId = linkedAccount.getString(1); // TODO: check if it's 0 or 1
				firstTransaction = isFirstTransaction(accountId);
				if (firstTransaction)
				{
					deductableAmount += 5.00;
				}
				/*ResultSet balance = statement.executeQuery( "SELECT balance FROM Accounts WHERE accountID = \'" + linkedId + "\'");
				if( !balance.next() )
				{
					System.out.println("Linked account is invalid.");
					return "1";
				}
				if (balance.getDouble(1) - (amount+5) <= 0.01) //TODO: not sure if getDouble works, check
				{
					System.out.println("Not enough balance in the linked account.");
					return "1";
				}*/
				if (!isValidTransaction(linkedId, deductableAmount))
					return "1";

				ResultSet transIds = statement.executeQuery( "SELECT nvl(max(transactionId), 0) as maxTransId FROM Transactions");
				transactionId = transIds.getInt("maxTransId")+1;

				ResultSet day = statement.executeQuery("SELECT * FROM CurrentDate");
				Date currDate = new Date(2011, 3, 1);
				if (day.next())
					currDate = day.getDate("currentdate");
				else
					return "1";
				String insertStatement = "INSERT INTO Transactions VALUES (?,\'?\',\'?\',?,\'?\',?,?)";
				//PreparedStatement pstmt = _connection.prepareStatement(insertStatement);

				//ResultSet pastTransactions = statement.executeQuery("SELECT * FROM Transactions WHERE accountID = \'" + id + "\'");
				if( firstTransaction )
				{
					statement.executeQuery("UPDATE Accounts SET balance = balance - 5 WHERE accountId = \'" + linkedId + "\')");
					executeTransaction(insertStatement, transactionId, linkedId, null, "first_transaction_fee", 5.00, 0);
					/*pstmt.setInt(1, transactionId);
					pstmt.setString(2, linkedId);
					pstmt.setString(3, 0);
					pstmt.setDate(4, currDate);
					pstmt.setString(5, "first_transaction_fee");
					pstmt.setDouble(6, 5.00);
					pstmt.execute();*/
					transactionId++;
				}

				statement.executeQuery("UPDATE Accounts SET balance = balance - " + Double.toString(amount) + "WHERE accountId = \'" + linkedId + "\')");
				statement.executeQuery("UPDATE Accounts SET balance = balance + " + Double.toString(amount) + "WHERE accountId = \'" + accountId + "\')");
				//String insertStatement = "INSERT INTO Transactions VALUES (?,\'?\',\'?\',?,\'?\',?, null)";
				//String insertStatement2 = "INSERT INTO Transactions VALUES (" + transactionId + ",\'?\',\'?\',?, \'topup_rec\', " + amount + ", null)";
				//PreparedStatement pstmt = _connection.prepareStatement(insertStatement);

				executeTransaction(insertStatement, transactionId, linkedId, accountId, "topup_send", amount, 0);
				transactionId++;
				executeTransaction(insertStatement, transactionId, accountId, linkedId, "topup_rec", amount, 0);

				/*pstmt.setInt(1, transactionId);
				pstmt.setString(2, linkedId);
				pstmt.setString(3, accountId);
				pstmt.setDate(4, currDate);
				pstmt.setString(5, "topup_send");
				pstmt.setDouble(6, amount);
				pstmt.execute();

				transactionId++;
				pstmt.setInt(1, transactionId);
				pstmt.setString(2, accountId);
				pstmt.setString(3, linkedId);
				pstmt.setDate(4, currDate);
				pstmt.setString(5, "topup_rec");
				pstmt.setDouble(6, amount);
				pstmt.execute();*/
				/*statement.executeQuery("INSERT INTO Transactions VALUES (" + transactionId + ",\'" + linkedId + "\',\' "+ accountId + "\', " + currentDate + ", \'topup_send\', " + amount + ", null)");
				transactionId++;
				statement.executeQuery("INSERT INTO Transactions VALUES (" + transactionId + ",\'" + accountId + "\',\' " + linkedId + "\', " + currentDate + ", \'topup_rec\', " + amount + ", null)");
				*/
				return "0";
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
		//TODO: check for distinct account owners
		if (amount <= 0)
		{
			System.out.println("Amount to deposit is invalid.");
			return "1";
		}
		if (!verifyAccountType(from, "Pocket") || !verifyAccountType(to, "Pocket"))
		{
			System.out.println("At least one account is not a pocket account.");
			return "1";
		}

		int transactionId = 0;
		String linkedId = "";
		double deductableAmount = amount;
		double receivedAmount = amount;
		boolean fromFirstTransaction = false;
		boolean toFirstTransaction = false;

		try (Statement statement = _connection.createStatement()) {

				/*ResultSet fromAccount = statement.executeQuery( "SELECT balance FROM Accounts WHERE accountID = \'" + from + "\'");
				if( !fromAccount.next() )
				{
					System.out.println("Sender account is invalid.");
					return "1";
				}
				if (fromAccount.getDouble("balance") - amount < 0) //TODO: not sure if getDouble works, check
				{
					System.out.println("Not enough balance in the sender's account.");
					return "1";
				}
				else if (fromAccount.getDouble("balance") - amount <= 0.01)
				{
					statement.executeQuery("UPDATE Accounts SET isOpen = 0 WHERE accountId = \'" + from + "\')");
				}*/

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

				if (!isValidTransaction(from, deductableAmount))
					return "1";
				if (receivedAmount < 0.00)
				{
					if (!isValidTransaction(to, (-1)*receivedAmount))
						return "1";
				}

				ResultSet transIds = statement.executeQuery( "SELECT nvl(max(transactionId), 0) as maxTransId FROM Transactions");
				transactionId = transIds.getInt("maxTransId")+1;

				ResultSet day = statement.executeQuery("SELECT * FROM CurrentDate");
				Date currDate = new Date(2011, 3, 1);
				if (day.next())
					currDate = day.getDate("currentdate");
				else
					return "1";

				String insertStatement = "INSERT INTO Transactions VALUES (?,\'?\',\'?\',?,\'?\',?,?)";
				//PreparedStatement pstmt = _connection.prepareStatement(insertStatement);

				if( fromFirstTransaction )
				{
					statement.executeQuery("UPDATE Accounts SET balance = balance - 5 WHERE accountId = \'" + from + "\')");
					executeTransaction(insertStatement, transactionId, from, null, "first_transaction_fee", 5.00, 0);
					/*pstmt.setInt(1, transactionId);
					pstmt.setString(2, from);
					pstmt.setString(3, 0);
					pstmt.setDate(4, currDate);
					pstmt.setString(5, "first_transaction_fee");
					pstmt.setDouble(6, 5.00);
					pstmt.execute();*/
					transactionId++;
				}

				if( toFirstTransaction )
				{
					statement.executeQuery("UPDATE Accounts SET balance = balance - 5 WHERE accountId = \'" + to + "\')");
					executeTransaction(insertStatement, transactionId, to, null, "first_transaction_fee", 5.00, 0);
					transactionId++;
				}
				statement.executeQuery("UPDATE Accounts SET balance = balance - " + Double.toString(amount) + "WHERE accountId = \'" + to + "\')");
				statement.executeQuery("UPDATE Accounts SET balance = balance + " + Double.toString(amount) + "WHERE accountId = \'" + from + "\')");
				executeTransaction(insertStatement, transactionId, from, to, "payfriend_send", amount, 0);
				transactionId++;
				executeTransaction(insertStatement, transactionId, to, from, "payfriend_rec", amount, 0);

				/*pstmt.setInt(1, transactionId);
				pstmt.setString(2, from);
				pstmt.setString(3, to);
				pstmt.setDate(4, currDate);
				pstmt.setString(5, "payfriend_send");
				pstmt.setDouble(6, amount);
				pstmt.execute();

				transactionId++;
				pstmt.setInt(1, transactionId);
				pstmt.setString(2, to);
				pstmt.setString(3, from);
				pstmt.setDate(4, currDate);
				pstmt.setString(5, "payfriend_rec");
				pstmt.setDouble(6, amount);
				pstmt.execute();*/
				/*statement.executeQuery("UPDATE Accounts SET balance = balance + " + Double.toString(amount) + "WHERE accountId = \'" + to + "\')");
				statement.executeQuery("UPDATE Accounts SET balance = balance - " + Double.toString(amount) + "WHERE accountId = \'" + from + "\')");
				statement.executeQuery("INSERT INTO Transactions VALUES (" + transactionId + ",\'" + from + "\',\'"+ to + "\', " + currentDate + ", \'payfriend_send\', " + amount + ", null)");
				transactionId++;
				statement.executeQuery("INSERT INTO Transactions VALUES (" + transactionId + ",\'" + to + "\',\' " + from + "\', " + currentDate + ", \'payfriend_rec\', " + amount + ", null)");
				*/
				return "0";
			}
		catch( SQLException e )
		{
			System.err.println( e.getMessage() );
			return "1";
		}
	}

	public String purchase(String id, double amount)
	{
		try (Statement statement = _connection.createStatement()) {
			double deductableAmount = amount;
			if (amount <= 0)
			{
				System.out.println("Amount to spend is invalid.");
				return "1";
			}

			if (!verifyAccountType(id, "Pocket"))
			{
				System.out.println("Account is not a pocket account.");
				return "1";
			}
			if (isFirstTransaction(id))
				deductableAmount += 5.00;

			int transactionId = 0;
			if (isValidTransaction(id, deductableAmount))
			{
				ResultSet transIds = statement.executeQuery( "SELECT nvl(max(transactionId), 0) as maxTransId FROM Transactions");
				transactionId = transIds.getInt("maxTransId")+1;
				statement.executeQuery("UPDATE Accounts SET balance = balance - " + Double.toString(deductableAmount) + "WHERE accountId = \'" + id + "\')");
				String insertStatement = "INSERT INTO Transactions VALUES (?,\'?\',\'?\',?,\'?\',?,?)";
				executeTransaction(insertStatement, transactionId, id, null, "purchase", deductableAmount, 0);
				return "0";
			}
			return "1";
		}
		catch( Exception e )
		{
		    System.err.println( e.getMessage() );
		    return "1";
		}
	}

	public String collect(String id, double amount)
	{
		try (Statement statement = _connection.createStatement()) {
			double deductableAmount = amount;
			if (amount <= 0)
			{
				System.out.println("Amount to collect is invalid.");
				return "1";
			}

			if (!verifyAccountType(id, "Pocket"))
			{
				System.out.println("Account is not a pocket account.");
				return "1";
			}

			deductableAmount*=1.03; //add 3% fee

			if (isFirstTransaction(id))
				deductableAmount += 5.00;

			int transactionId = 0;
			if (isValidTransaction(id, deductableAmount))
			{
				ResultSet transIds = statement.executeQuery( "SELECT nvl(max(transactionId), 0) as maxTransId FROM Transactions");
				transactionId = transIds.getInt("maxTransId")+1;

				ResultSet accountId = statement.executeQuery("SELECT ownerID FROM PocketOwner WHERE pocketID = \'" + id + "\'");
				String mainId = accountId.getString(1);
				statement.executeQuery("UPDATE Accounts SET balance = balance - " + Double.toString(deductableAmount) + "WHERE accountId = \'" + id + "\')");
				statement.executeQuery("UPDATE Accounts SET balance = balance + " + Double.toString(amount) + "WHERE accountId IN \'" + mainId + "\'");
				String insertStatement = "INSERT INTO Transactions VALUES (?,\'?\',\'?\',?,\'?\',?,?)";
				executeTransaction(insertStatement, transactionId, id, mainId, "collect_send", deductableAmount, 0);
				transactionId++;
				executeTransaction(insertStatement, transactionId, mainId, id, "collect_rec", amount, 0);
				return "0";
			}
			return "1";
		}
		catch( Exception e )
		{
		    System.err.println( e.getMessage() );
		    return "1";
		}
	}

	public String withdrawal(String id, double amount)
	{
		try (Statement statement = _connection.createStatement()) {
			if (amount <= 0)
			{
				System.out.println("Amount to withdraw is invalid.");
				return "1";
			}

			if (verifyAccountType(id, "Pocket"))
			{
				System.out.println("Account is not a checkings or savings account.");
				return "1";
			}

			int transactionId = 0;

			if (isValidTransaction(id, amount))
			{
				ResultSet transIds = statement.executeQuery( "SELECT nvl(max(transactionId), 0) as maxTransId FROM Transactions");
				transactionId = transIds.getInt("maxTransId")+1;
				statement.executeQuery("UPDATE Accounts SET balance = balance - " + Double.toString(amount) + "WHERE accountId = \'" + id + "\')");
				String insertStatement = "INSERT INTO Transactions VALUES (?,\'?\',\'?\',?,\'?\',?,?)";
				executeTransaction(insertStatement, transactionId, id, null, "withdrawal", amount, 0);
				return "0";
			}
			return "1";
		}
		catch( Exception e )
		{
		    System.err.println( e.getMessage() );
		    return "1";
		}
	}

	public String wire(String from, String to, double amount)
	{
		//TODO: check for distinct account owners
		try (Statement statement = _connection.createStatement()) {
			double deductableAmount = amount;
			if (amount <= 0)
			{
				System.out.println("Amount to wire is invalid.");
				return "1";
			}

			if (verifyAccountType(from, "Pocket") || verifyAccountType(to, "Pocket"))
			{
				System.out.println("At least one account is not a checkings or savings account.");
				return "1";
			}

			deductableAmount*=1.02; //add 2% fee

			int transactionId = 0;

			if (isValidTransaction(from, deductableAmount))
			{
				ResultSet transIds = statement.executeQuery( "SELECT nvl(max(transactionId), 0) as maxTransId FROM Transactions");
				transactionId = transIds.getInt("maxTransId")+1;

				statement.executeQuery("UPDATE Accounts SET balance = balance - " + Double.toString(deductableAmount) + "WHERE accountId = \'" + from + "\')");
				statement.executeQuery("UPDATE Accounts SET balance = balance + " + Double.toString(amount) + "WHERE accountId =  \'" + to + "\'");
				String insertStatement = "INSERT INTO Transactions VALUES (?,\'?\',\'?\',?,\'?\',?,?)";
				executeTransaction(insertStatement, transactionId, from, to, "wire_send", deductableAmount, 0);
				transactionId++;
				executeTransaction(insertStatement, transactionId, to, from, "wire_rec", amount, 0);
				return "0";
			}
			return "1";
		}
		catch( Exception e )
		{
		    System.err.println( e.getMessage() );
		    return "1";
		}
	}

	public String transfer(String from, String to, double amount)
	{
		//TODO: check that the from and to accounts are not the same. should check for all transactions
		try (Statement statement = _connection.createStatement()) {
			double deductableAmount = amount;
			if (amount <= 0 || amount > 2000)
			{
				System.out.println("Amount to transfer is invalid.");
				return "1";
			}

			if (verifyAccountType(from, "Pocket") || verifyAccountType(to, "Pocket"))
			{
				System.out.println("At least one account is not a checkings or savings account.");
				return "1";
			}

			//TODO: check for ownership of accounts

			int transactionId = 0;

			if (isValidTransaction(from, amount))
			{
				ResultSet transIds = statement.executeQuery( "SELECT nvl(max(transactionId), 0) as maxTransId FROM Transactions");
				transactionId = transIds.getInt("maxTransId")+1;

				statement.executeQuery("UPDATE Accounts SET balance = balance - " + Double.toString(amount) + "WHERE accountId = \'" + from + "\')");
				statement.executeQuery("UPDATE Accounts SET balance = balance + " + Double.toString(amount) + "WHERE accountId =  \'" + to + "\'");
				String insertStatement = "INSERT INTO Transactions VALUES (?,\'?\',\'?\',?,\'?\',?,?)";
				executeTransaction(insertStatement, transactionId, from, to, "transfer_send", amount, 0);
				transactionId++;
				executeTransaction(insertStatement, transactionId, to, from, "transfer_rec", amount, 0);
				return "0";
			}
			return "1";
		}
		catch( Exception e )
		{
		    System.err.println( e.getMessage() );
		    return "1";
		}
	}

	public String writeCheck(String id, double amount)
	{
		try (Statement statement = _connection.createStatement()) {
			if (amount <= 0)
			{
				System.out.println("Amount to subtract is invalid.");
				return "1";
			}

			if (!verifyAccountType(id, "InterestChecking") && !verifyAccountType(id, "StudentChecking"))
			{
				System.out.println("Account is not a checkings account.");
				return "1";
			}

			int transactionId = 0;
			int checkNumber = 0;

			if (isValidTransaction(id, amount))
			{
				ResultSet transIds = statement.executeQuery( "SELECT nvl(max(transactionId), 0) as maxTransId FROM Transactions");
				transactionId = transIds.getInt("maxTransId")+1;
				ResultSet checkNumbers = statement.executeQuery( "SELECT nvl(max(checkNumber), 0) as maxCheckNumber FROM Transactions");
				checkNumber = checkNumbers.getInt("maxCheckNumber")+1;

				statement.executeQuery("UPDATE Accounts SET balance = balance - " + Double.toString(amount) + "WHERE accountId = \'" + id + "\')");
				String insertStatement = "INSERT INTO Transactions VALUES (?,\'?\',\'?\',?,\'?\',?,?)";
				executeTransaction(insertStatement, transactionId, id, null, "check", amount, checkNumber);
				return "0";
			}
			return "1";
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
		System.out.println("hi");
		try( Statement statement = _connection.createStatement() ){
	    ResultSet resultSet = statement.executeQuery("SELECT accountID FROM Accounts WHERE isOpen = 0");
	    String output = "0";
	    while(resultSet.next()){
				System.out.println("g");
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
            ResultSet resultSet = statement.executeQuery("Select O2.taxID, Sum(O2.AccTotal) From(Select O.taxID, O.accountID, T2.AccTotal From Owns O INNER JOIN (Select T.accountID, Sum(T.amount) as AccTotal from Transactions T where (T.transactionType = \'deposit\' or T.transactionType = \'transfer_rec\' or T.transactionType = \'wire_rec\') and T.transactionDate BETWEEN \'" + start + "\' AND \'" + end + "\' group by accountID) T2 ON O.accountID = T2.accountID) O2 Group By O2.taxID Having Sum(O2.AccTotal) > 10000");
            String output = "0";
            while (resultSet.next()){
                output += " " + resultSet.getString(1);
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
                String thisAccNum = accNumSet.getString(1);
                String accOutput = thisAccNum + "\n";
                String transactQuery = "Select * from Transactions T where T.accountID = \'" + thisAccNum + "\' and T.transactionDate between \'" + startDate + "\' and \'" + endDate + "\'";
                ResultSet transactionSet = statement.executeQuery(transactQuery);
                int colNum = transactionSet.getMetaData().getColumnCount();
                String finalBalance = showBalance(thisAccNum).substring(2);
                double initialBalance = Double.parseDouble(finalBalance);
                totalBalance += initialBalance;
                while (transactionSet.next()){
                    for (int i = 1; i <= colNum; i++){
                        accOutput += transactionSet.getString(i) + " ";
                    }
                    accOutput += "\n";
                    String transactionType = transactionSet.getString(5);
                    if (transactionType.substring(transactionType.length() - 4).equals("send") || transactionType.equals("withdrawal") || transactionType.equals("purchase") || transactionType.equals("check")){
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
                String thisAccNum = accNumSet.getString(1);
                output += thisAccNum;
                String accQuery = "Select A.isOpen from Accounts A where A.accountID = \'" + thisAccNum + "\'";
                ResultSet accSet = statement.executeQuery(accQuery);
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
            return "1";
        }
    }
	public String addInterest(){
		if (!isLastOfMonth()){
			return "1 is not last of month";
		}
		try (Statement statement = _connection.createStatement()){
			ResultSet interestDates = statement.executeQuery("Select * from InterestAdded");
			Date currentDate = getCurrentDate();
			while (interestDates.next()){
				if (interestDates.getDate(1).compareTo(currentDate) == 0){
					return "1 already added interest this month";
				}
			}
			ResultSet accountSet = statement.executeQuery("SELECT A.taxID, A.balance, A.interestRate from Accounts A where A.isOpen = 1 and A.interestRate != 0");
			Date firstDate = new Date(currentDate.getYear(), currentDate.getMonth(), 1);
			int totalDays = currentDate.getDate();
			while (accountSet.next()){
				double finalBalance = accountSet.getDouble(2);
				String accId = accountSet.getString(1);
				double interestRate = accountSet.getDouble(3)/12;
				double averageBalance = finalBalance;
				ResultSet transacts = statement.executeQuery("SELECT T.transactionDate, T.transactionType, T.amount FROM Transactions T WHERE T.accountID = \'" + accId + "\' and T.transactionDate >= \'" + formatDateToSQL(firstDate) + "\' and T.transactionDate <= \'" + formatDateToSQL(currentDate) + "\' order by T.transactionDate desc");
				int currentDays = totalDays;
				while (transacts.next()){
					double averageInfluence = transacts.getDouble(3)*(totalDays-currentDays);
					String transactionType = transacts.getString(2);
					if (transactionType.substring(transactionType.length() - 4).equals("send") || transactionType.equals("withdrawal") || transactionType.equals("purchase") || transactionType.equals("check") || transactionType.equals("first_transaction_fee")){
						averageBalance += averageInfluence;
					} else {
						averageBalance -= averageInfluence;
					}
				}
				double newBalance = averageBalance*interestRate + finalBalance;
				ResultSet interestSet = statement.executeQuery("UPDATE Accounts SET balance = " + newBalance + " where accountID = \'" + accId + "\'");
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
			if (account.getString(1).equals(hashedPin)){
				return true;
			}
			/*else {
				//return "1 incorrect pin";
				return false;
			}*/
		}
		//return "1 account not found";
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
		    ResultSet resultSet = statement.executeQuery("Update Accounts WHERE accountType = \'" + type + "\' SET interestRate = " + newRate);
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

		//checks if the amount deducted is valid
		//amount exceeds balance: invalid transaction
		//amount equals balance or leaves $0.01: valid transaction but the account is closed
		//otherwise: deducting amount is a valid transaction does not trigger any special cases
		private boolean isValidTransaction(String id, double amount)
		{
			try (Statement statement = _connection.createStatement()){
				ResultSet account = statement.executeQuery( "SELECT balance, accountType FROM Accounts WHERE accountID = \'" + id + "\'");
				if( !account.next() )
				{
					System.out.println("Account is invalid.");
					return false;
				}
				if (account.getDouble("balance") - amount < 0) //TODO: not sure if getDouble works, check
				{
					System.out.println("Not enough balance in the account.");
					return false;
				}
				else if (account.getDouble("balance") - amount <= 0.01)
				{
					statement.executeQuery("UPDATE Accounts SET isOpen = 0 WHERE accountId = \'" + id + "\')");
					if (!account.getString("accountType").equals("Pocket"))
					{
						statement.executeQuery("UPDATE Accounts SET isOpen = 0 WHERE accountId IN (SELECT pocketID FROM PocketOwner WHERE ownerID = \'" + id + "\')");
					}
					return true;
				}
				return true;
			}
			catch( Exception e )
			{
					System.err.println( e.getMessage() );
					return false;
			}
		}

		private boolean verifyAccountType(String id, String type)
		{
			System.out.println("Verifying account type");
			try( Statement statement = _connection.createStatement() ){
		    ResultSet resultSet = statement.executeQuery("SELECT accountType FROM Accounts WHERE accountID = \'" + id + "\'");
		    if(resultSet.next() && resultSet.getString(1).equals(type)){
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
			pstmt.setDouble(6, amt);
			pstmt.setInt(7, checkNo);
			pstmt.execute();
		    } catch (Exception e){
			System.err.println(e.getMessage());
		    }
	}
}
