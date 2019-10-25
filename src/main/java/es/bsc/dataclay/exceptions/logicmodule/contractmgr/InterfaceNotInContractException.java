
/**
 * @file InterfaceNotInContractException.java
 * 
 * @date Oct 5, 2012
 */
package es.bsc.dataclay.exceptions.logicmodule.contractmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.InterfaceID;

/**
 * This class represents the exception produced in ContractManager when a contract cannot access a specific interface.
 * 
 */
public final class InterfaceNotInContractException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = -4028881264452819708L;

	/**
	 * Exception produced when a contract cannot access a specific interface
	 * @param interfaceID
	 *            ID of the interface
	 * @param contractID
	 *            ID of the contract
	 */
	public InterfaceNotInContractException(final InterfaceID interfaceID, final ContractID contractID) {
		super(ERRORCODE.INTERFACE_NOT_IN_CONTRACT, "The interface " + interfaceID + " is not in contract " + contractID, false);
	}
}
