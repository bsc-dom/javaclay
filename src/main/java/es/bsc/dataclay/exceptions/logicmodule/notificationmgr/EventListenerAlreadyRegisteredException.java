
/**
 * @file ObjectAlreadyRegisteredException.java
 * 
 * @date May 28, 2013
 */
package es.bsc.dataclay.exceptions.logicmodule.notificationmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.ECAID;

/**
 * This class represents the exceptions produced in NotificationManager module when some event listener specified is already
 * registered.
 * 
 */
public class EventListenerAlreadyRegisteredException extends DataClayException {

	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 1315261128696879911L;

	/**
	 * This exception is produced when some event listener is already registered
	 * @param id
	 *            ID of the event
	 */
	public EventListenerAlreadyRegisteredException(final ECAID id) {
		super(ERRORCODE.EVENT_LISTENER_ALREADY_REGISTERED, "Event listener " + id.toString() + " is already registered", false);
	}

}
