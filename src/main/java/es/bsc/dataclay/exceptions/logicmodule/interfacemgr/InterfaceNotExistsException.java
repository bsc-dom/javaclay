
/**
 * @file InterfaceNotExistsException.java
 * 
 * @date Oct 15, 2012
 */
package es.bsc.dataclay.exceptions.logicmodule.interfacemgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.InterfaceID;

/**
 * This class represents the exception occurred when an interface is not found.
 * 
 */
public class InterfaceNotExistsException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = -1599661041254921898L;

	/**
	 * Exception produced when an interface is not found
	 * @param interfaceID
	 *            id of the interface
	 */
	public InterfaceNotExistsException(final InterfaceID interfaceID) {
		super(ERRORCODE.INTERFACE_NOT_EXIST, "Interface " + interfaceID + " does not exist", false);
	}
}
