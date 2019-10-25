
/**
 * @file SessionNotExistException.java
 * 
 * @date Mar 5, 2014
 */
package es.bsc.dataclay.exceptions.logicmodule.sessionmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.DataClayInstanceID;

/**
 * This class represents the exceptions produced in Session Manager module when an external Session does not exist.
 * 
 */
public class SessionExternalNotExistException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = 7164014018768796850L;

	/**
	 * This exception is produced when an external Session does not exist
	 * @param sessionID
	 *            ID of the external Session
	 */
	public SessionExternalNotExistException(final DataClayInstanceID extDataClayID) {
		super(ERRORCODE.SESSION_NOT_EXISTS, "External session for dataClay " + extDataClayID + " does not exist.", false);
	}
}
