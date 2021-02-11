
package es.bsc.dataclay.logic.accountmgr;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.exceptions.logicmodule.accountmgr.AccountAlreadyExistException;
import es.bsc.dataclay.exceptions.logicmodule.accountmgr.AccountNotExistException;
import es.bsc.dataclay.exceptions.logicmodule.accountmgr.InvalidCredentialsException;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.CredentialID;
import es.bsc.dataclay.util.management.AbstractManager;
import es.bsc.dataclay.util.management.accountmgr.Account;
import es.bsc.dataclay.util.management.accountmgr.AccountRole;
import es.bsc.dataclay.util.management.accountmgr.PasswordCredential;
import es.bsc.dataclay.util.structs.LruCache;
import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteDataSource;

/**
 * This class is responsible to manage system accounts: add, remove and modify.
 */
public final class AccountManager extends AbstractManager {

	/** Logger. */
	private static final Logger logger = LogManager.getLogger("managers.AccountManager");

	/** Db of Accounts. */
	private final AccountManagerDB accountDB;

	/** Account cache. */
	private final LruCache<AccountID, Account> accountCache = new LruCache<>(
			Configuration.Flags.MAX_ENTRIES_ACCOUNT_MANAGER_CACHE.getIntValue());

	/**
	 * Instantiates an Account Manager that uses the Backend configuration provided. (which backend to use, how to use it).
	 * @param managerName
	 *            Manager/service name.
	 * @post Creates an Account manager and initializes the accountDB.
	 */
	public AccountManager(final SQLiteDataSource dataSource) {
		super(dataSource);
		this.accountDB = new AccountManagerDB(dataSource);
		accountDB.createTables();
	}

	/**
	 * Method that registers a new account
	 * @param adminAccountID
	 *            an admin account
	 * @param adminCredential
	 *            the credentials of the admin account
	 * @param newaccount
	 *            the info of the new account
	 * @return New account ID
	 */
	public AccountID newAccount(final AccountID adminAccountID, final PasswordCredential adminCredential,
			final Account newaccount) {

		// Validate admin account
		if (!validateAccount(adminAccountID, adminCredential, AccountRole.ADMIN_ROLE)) {
			throw new InvalidCredentialsException("Invalid admin credentials");
		}

		if (accountDB.existsAccountByName(newaccount.getUsername())) {
			throw new AccountAlreadyExistException(newaccount.getUsername());
		}

		// Create new account, if ID is not set (Admin case or others has static ID) create it.
		if (newaccount.getDataClayID() == null) {
			newaccount.setDataClayID(new AccountID());
		}
		if (newaccount.getCredential().getDataClayID() == null) {
			newaccount.getCredential().setDataClayID(new CredentialID());
		}
		accountDB.store(newaccount);

		accountCache.put(newaccount.getDataClayID(), newaccount);
		logger.info("Created Account {} with ID {} and password {}",
				newaccount.getUsername(), newaccount.getDataClayID(), adminCredential);
		return newaccount.getDataClayID();

	}

	/**
	 * Retrieves the list of users registered in the system
	 * @param adminAccountID
	 *            an admin account
	 * @param adminCredential
	 *            the credentials of the admin account
	 * @return The IDs of the users registered in the system
	 */
	public HashSet<AccountID> getAccountList(final AccountID adminAccountID, final PasswordCredential adminCredential) {
		// Validate admin account
		if (!validateAccount(adminAccountID, adminCredential, AccountRole.ADMIN_ROLE)) {
			throw new InvalidCredentialsException("Invalid admin credentials");
		}

		// Verify if there is no other account with same name
		final List<Account> res = accountDB.getAllNormalAccounts();

		final HashSet<AccountID> result = new HashSet<>();
		if (result != null) {
			for (final Account curAccount : res) {
				result.add(curAccount.getDataClayID());
			}
		}
		return result;
	}

	/**
	 * Method that validates the provided account with the given credentials and considering also its role
	 * @param accountID
	 *            the account id to be validated
	 * @param credential
	 *            the credential of the account
	 * @param role
	 *            the role this account has
	 * @return true if the account exists and has the provided credentials and role, false otherwise.
	 */
	public boolean validateAccount(final AccountID accountID, final PasswordCredential credential, final AccountRole role) {

		Account result = accountCache.get(accountID);
		if (result == null) {
			result = accountDB.getByID(accountID);
			if (result == null) {
				throw new AccountNotExistException(accountID);
			}
			accountCache.put(accountID, result);
		}
		if (result.getRole() != role) {
			throw new AccountNotExistException(accountID);
		}

		// Now we look for the Credential associated to the account
		// identified by the CredentialID in the account.
		final PasswordCredential accountCredential = result.getCredential();

		// If the credentials are equal, return true.
		final boolean boolResult = credential.equals(accountCredential);
		return boolResult;
	}

	/**
	 * Method that validates the provided account with the given credentials
	 * @param accountID
	 *            the account id to be validated
	 * @param credential
	 *            the credential of the account
	 * @return true if the account exists and has the provided credentials, false otherwise.
	 */
	public boolean validateAccount(final AccountID accountID, final PasswordCredential credential) {

		// Retrieve the account
		Account result = accountCache.get(accountID);
		if (result == null) {
			result = accountDB.getByID(accountID);
			if (result == null) {
				throw new AccountNotExistException(accountID);
			}
			accountCache.put(accountID, result);
		}

		// Now we look for the Credential associated to the account
		// identified by the CredentialID in the account.
		final PasswordCredential accountCredential = result.getCredential();

		// If the credentials are equal, return true.
		return credential.equals(accountCredential);
	}

	/**
	 * Get the ID of the account with the name provided.
	 * @param name
	 *            Name of the account
	 * @return The ID of the account with the name provided
	 * @throws RemoteException
	 *             if the Account does not exist
	 */
	public AccountID getAccountID(final String name) {
		final Account res = accountDB.getByName(name);
		if (res == null) {
			throw new AccountNotExistException(name);
		}
		return res.getDataClayID();
	}

	/**
	 * Get the the account with the id provided.
	 * @param id
	 *            ID of the account
	 * @return The Account specification
	 */
	public Account getAccount(final AccountID id) {
		return accountDB.getByID(id);
	}

	/**
	 * Method that checeks whether an account exists or not
	 * @param accountID
	 *            the account id to be checked
	 * @return true if the provided account eixsts, false otherwise
	 */
	public boolean existsAccount(final AccountID accountID) {

		if (accountCache.containsKey(accountID)) {
			return true;
		}
		final boolean exists = accountDB.existsAccountByID(accountID);
		return exists;
	}

	/**
	 * Method used for unit testing.
	 * @return The db handler reference of this manager.
	 */
	public AccountManagerDB getDbHandler() {
		return accountDB;
	}

	@Override
	public void cleanCaches() {
		this.accountCache.clear();
	}

}
