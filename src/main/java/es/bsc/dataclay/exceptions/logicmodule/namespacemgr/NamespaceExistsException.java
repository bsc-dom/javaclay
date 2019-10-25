
/**
 * @file NamespaceExistsException.java
 * 
 * @date Oct 3, 2012
 */
package es.bsc.dataclay.exceptions.logicmodule.namespacemgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;

/**
 * This class represents the exceptions produced in NamespaceManager module when a Namespace already exists.
 * 
 */
public class NamespaceExistsException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = -4209493110289926623L;

	/**
	 * Exceptions produced when namespace identified by name provided already exists
	 * @param namespaceName
	 *            Name of the namespace
	 */
	public NamespaceExistsException(final String namespaceName) {
		super(ERRORCODE.NAMESPACE_EXISTS, "Namespace: " + namespaceName + " already exists", false);
	}
}
