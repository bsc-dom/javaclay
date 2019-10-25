
/**
 * @file ContractNotActiveException.java
 * 
 * @date Oct 5, 2012
 */
package es.bsc.dataclay.exceptions.logicmodule.contractmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.ContractID;

/**
 * This class represents the exception produced in ContractManager when a contract has to be used but it is not active.
 * 
 */
public final class ContractNotActiveException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = 2947467181748928834L;

	/**
	 * Exception produced when a contract has to be used but it already expired.
	 * @param contractID
	 *            the id of the contract
	 */
	public ContractNotActiveException(final ContractID contractID) {
		super(ERRORCODE.CONTRACT_NOT_ACTIVE, "Contract " + contractID + " is not active.", false);
	}
}
