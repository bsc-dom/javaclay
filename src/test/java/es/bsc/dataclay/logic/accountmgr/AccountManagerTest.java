
/**
 * @file AccountManagerTest.java
 * @date Sep 3, 2012
 * @author dgasull
 */
package es.bsc.dataclay.logic.accountmgr;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import es.bsc.dataclay.exceptions.logicmodule.accountmgr.AccountAlreadyExistException;
import es.bsc.dataclay.exceptions.logicmodule.accountmgr.AccountNotExistException;
import es.bsc.dataclay.exceptions.logicmodule.accountmgr.InvalidCredentialsException;
import es.bsc.dataclay.logic.accountmgr.AccountManager;
import es.bsc.dataclay.logic.accountmgr.AccountManagerDB;
import es.bsc.dataclay.test.AbstractManagerTest;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.CredentialID;
import es.bsc.dataclay.util.management.accountmgr.Account;
import es.bsc.dataclay.util.management.accountmgr.AccountRole;
import es.bsc.dataclay.util.management.accountmgr.PasswordCredential;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * This class tests the functions of the Accountmanager.
 */
public final class AccountManagerTest extends AbstractManagerTest {

	/** Account Manager instance tested. */
	private AccountManager aman;

	/** A normal Account used in the tests. */
	private Account newAccount;

	/** A normal Credential used in the tests. */
	private PasswordCredential newCredential;
	/** An admin Account used in the tests. */
	private Account adminAccount;
	/** Account specifications used in the tests. */
	private PasswordCredential adminCredential;
	/** DbHandler used in tests. */
	@Mock
	private AccountManagerDB testdb;

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Override
	@Before
	public void before() {
		super.before();

		final AccountManagerDB adb = new AccountManagerDB(dataSource);
		adb.dropTables();

		aman = new AccountManager(dataSource);
		testdb = aman.getDbHandler();

		newCredential = new PasswordCredential("newAccount");
		newCredential.setDataClayID(new CredentialID());
		newAccount = new Account("newAccount", AccountRole.NORMAL_ROLE, newCredential);
		newAccount.setDataClayID(new AccountID());
		adminCredential = new PasswordCredential("newAccountAdmin");
		adminCredential.setDataClayID(new CredentialID());
		adminAccount = new Account("newAccountAdmin", AccountRole.ADMIN_ROLE, adminCredential);
		adminAccount.setDataClayID(new AccountID());
	}

	@Override
	@After
	public void after() throws Exception {
		// Drop tables
		testdb.dropTables();
		testdb.close();
		super.after();
	}

	/**
	 * Test method for AccountManager::newAccount(AccountID, Credential, String, AccountRole, Credential)
	 * @pre The functions DbHandler::existsObjectByID(java.lang.Object) and DbHandler::store(java.lang.Object) must be tested and
	 *      correct.
	 * @post Test the creation and storage of a new Account. \n If the function throws an error the test fails. \n If after the
	 *       creation and storage we query the database for the Account and we obtain that it does not exists, the test fails \n
	 *       Otherwise the test is passed.
	 */
	@Test
	public void testNewAccount() {

		// Firstly, we create an admin account and we store it
		// to the database of accounts.
		// testdb.store(adminCredential);
		testdb.store(adminAccount);

		// Check it exists
		testdb.existsAccountByID(adminAccount.getDataClayID());

		// Now we test the function.
		// If an exception is thrown, the test fails
		aman.newAccount(adminAccount.getDataClayID(), adminCredential, newAccount);
		final AccountID newID = newAccount.getDataClayID();

		// Since we use the same database we need to open it again
		// to verify that the Account exists in the database by
		// calling the DbHandler directly.
		final Account curAccount = testdb.getByID(newID);
		assertTrue(curAccount != null);

		// Check the id of the object
		assertTrue(curAccount.getDataClayID().equals(newID));

		// Check fields
		assertTrue(curAccount.getCredential().equals(newCredential));
		assertTrue(curAccount.getRole().equals(newAccount.getRole()));
		assertTrue(curAccount.getUsername().equals(newAccount.getUsername()));
	}

	/**
	 * @brief Test method for AccountManager::newAccount(AccountID, Credential, String, AccountRole, Credential)
	 * @author dgasull
	 * @pre The function DbHandler::store(java.lang.Object) must be tested and correct.
	 * @post Test that if we do not provide an admin account to the creation function of new Account an error is thrown: If the
	 *       function throws an AccountManagerException the test pass. Otherwise the test fails. \n
	 */
	@Test(expected = AccountNotExistException.class)
	public void testNewAccountWrongAdmin() {
		/**
		 * The account provided must be admin and it is not
		 */
		aman.newAccount(newAccount.getDataClayID(), newCredential, newAccount);
	}

	@Test(expected = AccountAlreadyExistException.class)
	public void testNewAccountAlreadyExist() {
		// Firstly, we create an admin account and we store it
		// to the database of accounts.
		// testdb.store(adminCredential);
		testdb.store(adminAccount);

		// Check it exists
		testdb.existsAccountByID(adminAccount.getDataClayID());

		aman.newAccount(adminAccount.getDataClayID(), adminCredential, newAccount);
		aman.newAccount(adminAccount.getDataClayID(), adminCredential, newAccount);
	}

	/**
	 * @brief Test method for AccountManager::newAccount(AccountID, Credential, String, AccountRole, Credential)
	 * @author dgasull
	 * @pre The function DbHandler::store(java.lang.Object) must be tested and correct.
	 * @post Test that if we provide an admin account but a wrong credential to the creation function of new Account an error is
	 *       thrown: \n If the function throws an AccountManagerException the test pass. Otherwise the test fails. \n
	 */
	@Test(expected = InvalidCredentialsException.class)
	public void testNewAccountWrongCredential() {
		// Store admin account
		testdb.store(adminAccount);

		// Check it exists
		assertTrue(testdb.existsAccountByID(adminAccount.getDataClayID()));

		// Use manager
		aman.newAccount(adminAccount.getDataClayID(), newCredential, newAccount);

	}

	/**
	 * @brief Test method for AccountManager::getAccountID(String)
	 * @pre The function DbHandler::store(java.lang.Object) must be tested and correct.
	 * @post Test that an Account can be retrieved by using the username. \n If the function throws an error the test fails. \n If
	 *       the function returns false the test fails since before we make sure that the account exists. Otherwise the test pass.
	 *       \n
	 */
	@Test
	public void testGetAccountID() {
		// Store an account
		testdb.store(newAccount);

		// Check it exists
		assertTrue(testdb.existsAccountByID(newAccount.getDataClayID()));

		// Now we call the function to test to verify that it should
		// return true since the account already exists in the system.
		assertTrue((newAccount.getDataClayID()).equals(aman.getAccountID(newAccount.getUsername())));
	}

	/**
	 * @brief Test method for AccountManager::getAccountList(AccountID, Credential)
	 * @pre The function DbHandler::store(java.lang.Object) must be tested and correct.
	 * @post Test that a list of Accounts can be retrieved with admin credentials. \n If the function throws an error the test
	 *       fails. \n If the function returns false the test fails since before we make sure that the account exists. Otherwise the
	 *       test pass. \n
	 */
	@Test
	public void testGetAccountList() {
		// We store admin account an one normal account into the database of accounts
		testdb.store(adminAccount);

		testdb.store(newAccount);

		// Check it exists
		assertTrue(testdb.existsAccountByID(newAccount.getDataClayID()));

		// Now we call the function to test to verify that it should
		// return the normal account among the list of existent accounts
		final HashSet<AccountID> accountsIDs = aman.getAccountList(adminAccount.getDataClayID(), adminCredential);
		assertTrue(accountsIDs.size() == 1);
		assertTrue((newAccount.getDataClayID()).equals(accountsIDs.iterator().next()));
	}

	/**
	 * @brief Test method for AccountManager::existsAccount(AccountID)
	 * @author dgasull
	 * @pre The function DbHandler::store(java.lang.Object) must be tested and correct.
	 * @post Test that an Account exists in the database: \n If the function throws an error the test fails. \n If the function
	 *       returns false the test fails since before we make sure that the account exists. Otherwise the test pass. \n
	 */
	@Test
	public void testExistsAccount() {
		// Store an account
		testdb.store(newAccount);

		// Check it exists
		assertTrue(testdb.existsAccountByID(newAccount.getDataClayID()));

		// Now we call the function to test to verify that it should
		// return true since the account already exists in the system.
		assertTrue(aman.existsAccount(newAccount.getDataClayID()));
	}

	/**
	 * @brief Test method for AccountManager::existsAccount(AccountID)
	 * @author dgasull
	 * @post Test that the function returns false if we query an unexistant account: \n If the function throws an error the test
	 *       fails. \n If the function returns true the test fails since before we make sure that the account does not exist.
	 *       Otherwise the test pass. \n
	 */
	@Test
	public void testExistsAccountFails() {

		// Create a random ID and we call the function to test to verify that it should return false since the
		// account does not exists in the database.
		final AccountID someID = new AccountID();
		assertFalse(aman.existsAccount(someID));
	}

	/**
	 * @brief Test method forAccountManager::validateAccount(AccountID, Credential, AccountRole)
	 * @author dgasull
	 * @pre The function DbHandler::store(java.lang.Object) must be tested and correct.
	 * @post Test the validation of an Account (role, ID and credentials properly related and exist in the database). In this case
	 *       we validate an Admin Account: \n If the function throws an error the test fails. Otherwise the test pass. \n
	 */
	@Test
	public void testValidateAccountAdmin() {
		// Create an admin account
		testdb.store(adminAccount);

		// Check it exists
		assertTrue(testdb.existsAccountByID(adminAccount.getDataClayID()));

		// Now we call the function (with admin permissions)
		// to verify that the ADMIN account
		// that we created and stored is admin and exists in the database.
		aman.validateAccount(adminAccount.getDataClayID(), adminCredential, AccountRole.ADMIN_ROLE);

	}

	/**
	 * @brief Test method forAccountManager::validateAccount(AccountID, Credential, AccountRole)
	 * @author dgasull
	 * @pre The function DbHandler::store(java.lang.Object) must be tested and correct.
	 * @post Test the validation of an Account(role, ID and credentials properly related and exist in the database). In this case we
	 *       validate a normal Account: \n If the function throws an error the test fails. Otherwise the test pass. \n
	 */
	@Test
	public void testValidateAccountNormal() {
		// Create a normal account
		testdb.store(newAccount);

		// Check it exists
		assertTrue(testdb.existsAccountByID(newAccount.getDataClayID()));

		// Now we call the function WITHOUT admin permissions
		// to verify that the NORMAL account should
		// return true since the account is not admin.
		aman.validateAccount(newAccount.getDataClayID(), newCredential, AccountRole.NORMAL_ROLE);

	}

	/**
	 * @brief Test method forAccountManager::validateAccount(AccountID, Credential)
	 * @pre The function DbHandler::store(java.lang.Object) must be tested and correct.
	 * @post Test the validation of an Account (ID and credentials properly related and exist in the database). If the function
	 *       throws an error the test fails. Otherwise the test pass. \n
	 */
	@Test
	public void testValidateAccount() {
		// Create an admin account an a normal account
		testdb.store(adminAccount);

		testdb.store(newAccount);

		// Check it exists
		assertTrue(testdb.existsAccountByID(adminAccount.getDataClayID()));
		assertTrue(testdb.existsAccountByID(newAccount.getDataClayID()));

		// Now we call the function (with admin permissions)
		// to verify that the ADMIN account
		// that we created and stored is admin and exists in the database.
		aman.validateAccount(adminAccount.getDataClayID(), adminCredential);
		aman.validateAccount(newAccount.getDataClayID(), newCredential);
	}

	/**
	 * @brief Test method forAccountManager::validateAccount(AccountID, Credential, AccountRole)
	 * @author dgasull
	 * @pre The function DbHandler::store(java.lang.Object) must be tested and correct.
	 * @post Test the validation of an Account. Throws exception since we want to validate an admin Account with normal role
	 */
	@Test(expected = AccountNotExistException.class)
	public void testValidateAccountWrongAdminRole() {
		// Create an admin account
		testdb.store(adminAccount);

		// Check it exists
		assertTrue(testdb.existsAccountByID(adminAccount.getDataClayID()));

		// Use manager
		aman.validateAccount(adminAccount.getDataClayID(), adminCredential, AccountRole.NORMAL_ROLE);

	}

	/**
	 * @brief Test method forAccountManager::validateAccount(AccountID, Credential, AccountRole)
	 * @author dgasull
	 * @pre The function DbHandler::store(java.lang.Object) must be tested and correct.
	 * @post Test the validation of an Account throws an exception since we want to validate a normal Account with admin role
	 */
	@Test(expected = AccountNotExistException.class)
	public void testValidateAccountWrongNormalRole() {
		// Create a normal account
		testdb.store(newAccount);

		// Check it exists
		assertTrue(testdb.existsAccountByID(newAccount.getDataClayID()));

		// Use manager
		aman.validateAccount(newAccount.getDataClayID(), newCredential, AccountRole.ADMIN_ROLE);

	}

	/**
	 * @brief Test method forAccountManager::validateAccount(AccountID, Credential, AccountRole)
	 * @author dgasull
	 * @pre The function DbHandler::store(java.lang.Object) must be tested and correct.
	 * @post Test the validation of an Account. Gets false since we want to validate an admin Account with bad credentials
	 */
	@Test
	public void testValidateAccountWrongAdminCredential() {
		// Create an admin account
		testdb.store(adminAccount);

		// Check it exists
		assertTrue(testdb.existsAccountByID(adminAccount.getDataClayID()));

		// Use manager
		assertFalse(aman.validateAccount(adminAccount.getDataClayID(), newCredential, AccountRole.ADMIN_ROLE));

	}

	/**
	 * @brief Test method forAccountManager::validateAccount(AccountID, Credential, AccountRole)
	 * @author dgasull
	 * @pre The function DbHandler::store(java.lang.Object) must be tested and correct.
	 * @post Test the validation of an Account. Gets a false since we want to validate a normal Account with admin credentials
	 */
	@Test
	public void testValidateAccountWrongNormalCredential() {
		// Create a normal account
		testdb.store(newAccount);

		// Check it exists
		assertTrue(testdb.existsAccountByID(newAccount.getDataClayID()));

		// Use manager
		assertFalse(aman.validateAccount(newAccount.getDataClayID(), adminCredential, AccountRole.NORMAL_ROLE));

	}

	/**
	 * @brief Test method forAccountManager::validateAccount(AccountID, Credential)
	 * @author dgasull
	 * @pre The function DbHandler::store(java.lang.Object) must be tested and correct.
	 * @post Test the validation of an Account. Gets a false since we want to validate an account with wrong creedentials
	 */
	@Test
	public void testValidateAccountWrongCredential() {
		// Create a normal account
		testdb.store(newAccount);

		// Check it exists
		assertTrue(testdb.existsAccountByID(newAccount.getDataClayID()));

		// Use manager
		assertFalse(aman.validateAccount(newAccount.getDataClayID(), adminCredential));

	}

	/**
	 * @brief Test method for AccountManager::validateAccount(AccountID, Credential, AccountRole)
	 * @author dgasull
	 * @pre The function DbHandler::store(java.lang.Object) must be tested and correct.
	 * @post Test the validation of an Account throws an exception since we want to validate an Account that does not exist in the
	 *       database
	 */
	@Test(expected = AccountNotExistException.class)
	public void testValidateAccountWrongID() {
		// Create a normal account
		testdb.store(newAccount);

		// Check it exists
		assertTrue(testdb.existsAccountByID(newAccount.getDataClayID()));

		// Now we create a random ID and we call the function
		// to test to verify that it should return false since the
		// account does not exists in the database.
		final AccountID someID = new AccountID();
		aman.validateAccount(someID, newCredential, AccountRole.NORMAL_ROLE);

	}

}
