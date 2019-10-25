
/**
 * @file InterfaceNotImportedException.java
 * 
 * @date May 29, 2013
 */
package es.bsc.dataclay.exceptions.logicmodule.namespacemgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.InterfaceID;

/**
 * This class represents the exceptions produced in NamespaceManager module when interface is not imported.
 */
public class InterfaceNotImportedException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = 7384448430404353201L;

	/**
	 * Exceptions produced when interface in contract identified by IDs provided is not imported in the namespace specified.
	 * @param namespaceName
	 *            Name of the namespace
	 * @param interfaceID
	 *            ID of the interface
	 * @param contractID
	 *            ID of the contract
	 */
	public InterfaceNotImportedException(final String namespaceName, final InterfaceID interfaceID, final ContractID contractID) {
		super(ERRORCODE.INTERFACE_NOT_IMPORTED, "Interface " + interfaceID.toString() + " in contract " + contractID.toString()
				+ " not imported in Namespace " + namespaceName, false);
	}
}
