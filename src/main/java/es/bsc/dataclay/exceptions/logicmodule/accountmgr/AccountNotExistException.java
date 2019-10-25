
/**
 * @file AccountNotExistException.java
 *
 * @date Sep 17, 2012
 */
package es.bsc.dataclay.exceptions.logicmodule.accountmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.AccountID;

/**
 * This class represents the exceptions produced in AccountManager module when a new Account to validate does not exist.
 * 
 */
public class AccountNotExistException extends DataClayException {

	/** Serial Version UID. */
	private static final long serialVersionUID = 7883194356713397384L;

	/**
	 * This Exception is called when and account with ID provided does not exist
	 * @param accountID
	 *            ID of the account
	 */
	public AccountNotExistException(final AccountID accountID) {
		super(ERRORCODE.ACCOUNT_NOT_EXIST, "Account with ID " + accountID.getId().toString() + " does not exist", false);
	}

	/**
	 * This Exception is called when and account with name provided does not exist
	 * @param accountName
	 *            Name of the account
	 */
	public AccountNotExistException(final String accountName) {
		super(ERRORCODE.ACCOUNT_NOT_EXIST, "Account with name " + accountName + " does not exist", false);
	}
}
