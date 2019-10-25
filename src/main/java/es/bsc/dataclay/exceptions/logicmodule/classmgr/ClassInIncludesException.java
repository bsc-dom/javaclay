
/**
 * @file ClassInIncludesException.java
 * 
 * @date Jun 15, 2013
 */
package es.bsc.dataclay.exceptions.logicmodule.classmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.NamespaceID;

/**
 * This class represents the exceptions produced in ClassManager module when you a class is included by some property, operation
 * or implementation and must not.
 * 
 */
public class ClassInIncludesException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = -1536123350185716470L;

	/**
	 * This exception is produced when a property, operation or implementation includes the class
	 * @param namespaceID
	 *            ID of the namespace
	 * @param className
	 *            Name of the class
	 */
	public ClassInIncludesException(final NamespaceID namespaceID, final String className) {
		super(ERRORCODE.CLASS_INCLUDED, "Some property, operation or implementation" + " includes the class " + className
				+ " in namespace " + namespaceID.getId().toString(), false);
	}

}
