
/**
 * @file BackendNotExistException.java
 * 
 * @date May 28, 2013
 */
package es.bsc.dataclay.exceptions.metadataservice;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.management.metadataservice.ExecutionEnvironment;

/**
 * This class represents the exceptions produced in MetaDataService module when a backend specified does not exist.
 * 
 */
public class ExecutionEnvironmentNotExistException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = -1976450151347853350L;

	/**
	 * This exception is produced when a backend with ID provided does not exist
	 * @param backendID
	 *            ID of the backend
	 */
	public ExecutionEnvironmentNotExistException(final ExecutionEnvironmentID backendID) {
		super(ERRORCODE.EXECUTION_ENVIRONMENT_NOT_EXIST, "ExecutionEnvironment " + backendID.toString() + " does not exist ", true);
	}

	/**
	 * This exception is produced when a certain name does not correspont to any Execution Environment
	 * @param exeEnvName
	 *            Name of the not existant backend
	 */
	public ExecutionEnvironmentNotExistException(final String exeEnvName, final Langs exeEnvLang) {
		super(ERRORCODE.EXECUTION_ENVIRONMENT_NOT_EXIST, "ExecutionEnvironment named " + exeEnvName + " does not exist for language " + exeEnvLang, true);
	}

	/**
	 * This exception is produced when a certain name does not correspont to any Execution Environment
	 * @param exeEnvName
	 *            Name of the not existant backend
	 */
	public ExecutionEnvironmentNotExistException(final Langs exeEnvLang) {
		super(ERRORCODE.EXECUTION_ENVIRONMENT_NOT_EXIST, "There is no ExecutionEnvironment for language " + exeEnvLang, true);
	}

	/**
	 * This exception is produced when a backend with specifications does not exist
	 * @param backendProto
	 *            Specifications of the backend
	 */
	public ExecutionEnvironmentNotExistException(final ExecutionEnvironment backendProto) {
		super(ERRORCODE.EXECUTION_ENVIRONMENT_NOT_EXIST, "Backend " + backendProto.toString() + " does not exist ", true);
	}

}
