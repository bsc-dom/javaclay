
/**
 * @file AccountNotRegisteredInContract.java
 * 
 * @date Oct 5, 2012
 */
package es.bsc.dataclay.exceptions.logicmodule.contractmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ContractID;

/**
 * This class represents the exception produced in ContractManager when an account is not registered in a contract and it should
 * be.
 * 
 */
public final class AccountNotRegisteredInContract extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = 5792235614293507644L;

	/**
	 * Exception produced when an account pretends to be registered in it, but it already was.
	 * @param accountID
	 *            the id of the account
	 * @param contractID
	 *            the id of the contract
	 */
	public AccountNotRegisteredInContract(final AccountID accountID, final ContractID contractID) {
		super(ERRORCODE.ACCOUNT_NOT_REGISTERED_IN_CONTRACT, "Account " + accountID + " is not registered in contract "
				+ contractID, false);
	}
}
