
package es.bsc.dataclay.exceptions.logicmodule.sessionmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.DataClayInstanceID;

/**
 * This class represents the exceptions produced in Session Manager module when an external Session is not active.
 */
public class SessionExternalIsNotActiveException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = -5595210609359085321L;

	/**
	 * This exception is produced when a Session is not active
	 * @param sessionID
	 *            ID of the Session
	 */
	public SessionExternalIsNotActiveException(final DataClayInstanceID extDataClayID) {
		super(ERRORCODE.SESSION_IS_NOT_ACTIVE, "Session for dataClay " + extDataClayID.toString() + " is not active", true);
	}

}
