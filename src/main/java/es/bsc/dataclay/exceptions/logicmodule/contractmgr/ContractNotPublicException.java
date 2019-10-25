
/**
 * @file ContractNotPublicException.java
 * 
 * @date Oct 5, 2012
 */
package es.bsc.dataclay.exceptions.logicmodule.contractmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.ContractID;

/**
 * This class represents the exception produced in ContractManager when a contract is expected to be public and it is not.
 * 
 */
public final class ContractNotPublicException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = 6514510674094925771L;

	/**
	 * Exception produced when a contract is expected to be public and it is not.
	 * @param contractID
	 *            ID of the contract
	 */
	public ContractNotPublicException(final ContractID contractID) {
		super(ERRORCODE.CONTRACT_NOT_PUBLIC, "Contract " + contractID + " is not active", false);
	}
}
