
/**
 * @file BackendAlreadyExistsException.java
 * 
 * @date May 28, 2013
 */
package es.bsc.dataclay.exceptions.metadataservice;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.management.metadataservice.ExecutionEnvironment;

/**
 * This class represents the exceptions produced in MetaDataService module when you try to add a new Storage Location that
 * already exists in the database.
 * 
 */
public class ExecutionEnvironmentAlreadyExistsException extends DataClayException {
	/** Serial version UID. */
	private static final long serialVersionUID = -1748086330980141969L;

	/**
	 * This exception is produced when a Storage Location with ID provided already exists
	 * @param newExecutionEnvironment
	 *            specs of the Execution Environment
	 */
	public ExecutionEnvironmentAlreadyExistsException(final ExecutionEnvironment newExecutionEnvironment) {
		super(ERRORCODE.EXECUTION_ENVIRONMENT_ALREADY_EXIST,
				"Execution Environment " + newExecutionEnvironment.toString() + " already exists", true);
	}

}
