
/**
 * @file AccountAlreadyExistException.java
 * 
 * @date Sep 17, 2012
 */
package es.bsc.dataclay.exceptions.logicmodule.accountmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;

/**
 * This class represents the exceptions produced in AccountManager module when a new Account to validate does not exist.
 * 
 */
public class AccountAlreadyExistException extends DataClayException {

	/** Serial Version UID. */
	private static final long serialVersionUID = -1519898457173094560L;

	/**
	 * This Exception is called when and account with name provided already exist
	 * @param accountName
	 *            Name of the account
	 */
	public AccountAlreadyExistException(final String accountName) {
		super(ERRORCODE.ACCOUNT_EXISTS, "Account with name " + accountName + " already exist", false);
	}
}
