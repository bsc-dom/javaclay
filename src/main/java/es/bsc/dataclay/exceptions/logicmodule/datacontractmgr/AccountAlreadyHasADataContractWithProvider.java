
/**
 * @file AccountAlreadyHasADataContractWithProvider.java
 * 
 * @date Mar 7, 2014
 */
package es.bsc.dataclay.exceptions.logicmodule.datacontractmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.DataSetID;

/**
 * This class represents the exception produced in DataContractManager when an account already has a data contract with a
 * specific dataset.
 * 
 */
public final class AccountAlreadyHasADataContractWithProvider extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = -3316340896078278981L;

	/**
	 * Exception produced when an account pretends to be registered in it, but it already was.
	 * @param accountID
	 *            the id of the account
	 * @param dataSetIDofProvider
	 *            the id of the contract
	 */
	public AccountAlreadyHasADataContractWithProvider(final AccountID accountID, final DataSetID dataSetIDofProvider) {
		super(ERRORCODE.ACCOUNT_ALREADY_HAS_A_DATACONTRACT_WITH_PROVIDER, "Account " + accountID
				+ " already has a contract with provider " + dataSetIDofProvider, false);
	}
}
