
/**
 * @file ContractNotExistException.java
 * 
 * @date Oct 5, 2012
 */
package es.bsc.dataclay.exceptions.logicmodule.contractmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.ContractID;

/**
 * This class represents the exception produced in ContractManager when a contract does not exist.
 * 
 */
public final class ContractNotExistException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = -4701498530774901347L;

	/**
	 * Exception produced when a contract does not exist .
	 * @param contractID
	 *            the id of the contract
	 */
	public ContractNotExistException(final ContractID contractID) {
		super(ERRORCODE.CONTRACT_NOT_EXIST, "Contract " + contractID + " does not exist.", false);
	}
}
