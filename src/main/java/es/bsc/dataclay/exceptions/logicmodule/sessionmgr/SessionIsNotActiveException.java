
package es.bsc.dataclay.exceptions.logicmodule.sessionmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.SessionID;

/**
 * This class represents the exceptions produced in Session Manager module when a Session is not active.
 */
public class SessionIsNotActiveException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = 3838558908152081562L;

	/**
	 * This exception is produced when a Session is not active
	 * @param sessionID
	 *            ID of the Session
	 */
	public SessionIsNotActiveException(final SessionID sessionID) {
		super(ERRORCODE.SESSION_IS_NOT_ACTIVE, "Session " + sessionID.toString() + " is not active", true);
	}

}
