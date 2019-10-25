
/**
 * @file InterfaceAlreadyImportedException.java
 * 
 * @date May 29, 2013
 */
package es.bsc.dataclay.exceptions.logicmodule.namespacemgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.InterfaceID;

/**
 * This class represents the exceptions produced in NamespaceManager module when trying to improt an interface that is already
 * imported.
 */
public class InterfaceAlreadyImportedException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = -1468558935587648578L;

	/**
	 * Exceptions produced when interface in contract identified by IDs provided was already imported
	 * @param namespaceName
	 *            Name of the namespace
	 * @param interfaceID
	 *            ID of the interface
	 * @param contractID
	 *            ID of the contract
	 */
	public InterfaceAlreadyImportedException(final String namespaceName, final InterfaceID interfaceID, final ContractID contractID) {
		super(ERRORCODE.INTERFACE_ALREADY_IMPORTED, "Interface " + interfaceID.toString() + " in contract "
				+ contractID.toString() + " already imported in Namespace " + namespaceName, false);
	}
}
