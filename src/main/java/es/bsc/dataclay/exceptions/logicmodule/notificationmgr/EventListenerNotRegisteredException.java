
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
 * This class represents the exceptions produced in NotificationManager module when some event listener specified is not
 * registered.
 * 
 */
public class EventListenerNotRegisteredException extends DataClayException {

	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 1765438995813231017L;

	/**
	 * This exception is produced when some event listener is not registered
	 * @param id
	 *            ID of the event
	 */
	public EventListenerNotRegisteredException(final ECAID id) {
		super(ERRORCODE.EVENT_LISTENER_NOT_REGISTERED, "Event listener " + id.toString() + " not registered", false);
	}

}
