
/**
 * @file NamespaceDoesNotExistException.java
 * 
 * @date Oct 3, 2012
 */

package es.bsc.dataclay.exceptions.logicmodule.namespacemgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.NamespaceID;

/**
 * This class represents the exceptions produced in NamespaceManager module when a Namespace does not exist.
 * 
 */
public class NamespaceDoesNotExistException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = 688793396782935374L;

	/**
	 * Exceptions produced when namespace identified by ID provided does not exist
	 * @param id
	 *            ID of the namespace
	 */
	public NamespaceDoesNotExistException(final NamespaceID id) {
		super(ERRORCODE.NAMESPACE_NOT_EXIST, "The namespace " + id + " does not exist", false);
	}

	/**
	 * Exceptions produced when namespace identified by name provided does not exist
	 * @param namespaceName
	 *            name of the namespace
	 */
	public NamespaceDoesNotExistException(final String namespaceName) {
		super(ERRORCODE.NAMESPACE_NOT_EXIST, "The namespace " + namespaceName + " does not exist", false);
	}
}
