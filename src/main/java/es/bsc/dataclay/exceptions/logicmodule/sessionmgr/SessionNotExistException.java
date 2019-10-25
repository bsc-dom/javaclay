
package es.bsc.dataclay.exceptions.logicmodule.sessionmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.SessionID;

/**
 * This class represents the exceptions produced in Session Manager module when a Session does not belong to specific account.
 */
public class SessionNotExistException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = -1248984339277754091L;

	/**
	 * This exception is produced when a Session does not exist
	 * @param sessionID
	 *            ID of the Session
	 */
	public SessionNotExistException(final SessionID sessionID) {
		super(ERRORCODE.SESSION_NOT_EXISTS, "Session " + sessionID + " does not exist.", false);
	}
}
