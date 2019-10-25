
/**
 * @file ClassDeploymentException.java
 * 
 * @date May 12, 2014
 */
package es.bsc.dataclay.exceptions.dataservice;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;

/**
 * This class represents the exceptions produced in DataService module when cannot deploy a specific execution class.
 */
public class ClassDeploymentException extends DataClayException {

	/** Serial Version UID. */
	private static final long serialVersionUID = -7885267456925994780L;

	/**
	 * This exception is produced when an error occurs during the deployment of an execution class.
	 * @param className
	 *            Name of the class
	 * @param namespaceName
	 *            Namespace of the class
	 * @param errorMessage
	 *            Error message produced.
	 */
	public ClassDeploymentException(final String namespaceName, final String className, final String errorMessage) {
		super(ERRORCODE.CLASS_DEPLOYMENT_ERROR, "Error produced while deploying class: " + className
				+ " of namespace: " + namespaceName + ". Error: " + errorMessage, false);
	}

}
