
/**
 * @file AccountHasNoDataContractWithProvider.java
 * 
 * @date Mar 7, 2014
 */
package es.bsc.dataclay.exceptions.logicmodule.datacontractmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.DataSetID;

/**
 * This class represents the exception produced in DataContractManager when an account is not registered in a contract and it
 * should be.
 * 
 */
public final class AccountHasNoDataContractWithProvider extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = -3212265848790028743L;

	/**
	 * Exception produced when an account pretends to be registered in it, but it already was.
	 * @param accountID
	 *            the id of the account
	 * @param dataSetIDofProvider
	 *            the id of the contract
	 */
	public AccountHasNoDataContractWithProvider(final AccountID accountID, final DataSetID dataSetIDofProvider) {
		super(ERRORCODE.ACCOUNT_HAS_NO_DATACONTRACT_WITH_PROVIDER, "Account " + accountID + " has no contract with provider "
				+ dataSetIDofProvider, false);
	}
}
