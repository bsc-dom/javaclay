
/**
 * @file CleanExecutionClassesDirException.java
 * 
 * @date May 12, 2014
 */
package es.bsc.dataclay.exceptions.dataservice;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;

/**
 * This class represents the exceptions produced in DataService module when cannot remove the execution classes directory.
 */
public class CleanExecutionClassesDirException extends DataClayException {

	/** Serial Version UID. */
	private static final long serialVersionUID = -9077897799005622698L;

	/**
	 * This exception is produced when an error occurs during the removal of execution classes directory.
	 * @param errorMessage
	 *            Error message produced.
	 */
	public CleanExecutionClassesDirException(final String errorMessage) {
		super(ERRORCODE.CLEAN_EXEC_CLASSES_DIR_ERROR, "Error produced while cleaning execution classes directory: "
				+ errorMessage, false);
	}

}
