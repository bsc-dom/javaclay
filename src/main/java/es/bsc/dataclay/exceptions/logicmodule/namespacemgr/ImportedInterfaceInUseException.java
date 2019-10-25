
/**
 * @file ImportedInterfaceInUseException.java
 * 
 * @date May 29, 2013
 */
package es.bsc.dataclay.exceptions.logicmodule.namespacemgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.InterfaceID;

/**
 * This class represents the exceptions produced in NamespaceManager module when an imported interface is in use.
 */
public class ImportedInterfaceInUseException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = 1092709378137223669L;

	/**
	 * Exceptions produced when interface identified by ID provided is in use
	 * @param namespaceName
	 *            Name of the namespace
	 * @param interfaceID
	 *            ID of the interface
	 * @param contractID
	 *            ID of the contract
	 */
	public ImportedInterfaceInUseException(final String namespaceName, final InterfaceID interfaceID, final ContractID contractID) {
		super(ERRORCODE.IMPORTED_INTERFACE_IN_USE, "Interface " + interfaceID.toString() + " in contract "
				+ contractID.toString() + " in use in Namespace " + namespaceName, false);
	}
}
