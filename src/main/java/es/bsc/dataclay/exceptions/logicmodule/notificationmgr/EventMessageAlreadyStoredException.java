
/**
 * @file ObjectAlreadyRegisteredException.java
 * 
 * @date May 28, 2013
 */
package es.bsc.dataclay.exceptions.logicmodule.notificationmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.EventMessageID;

/**
 * This class represents the exceptions produced in NotificationManager module when some event message specified is already
 * registered.
 * 
 */
public class EventMessageAlreadyStoredException extends DataClayException {

	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = -3683007897148349094L;

	/**
	 * This exception is produced when some event message is already in disk
	 * @param id
	 *            ID of the event
	 */
	public EventMessageAlreadyStoredException(final EventMessageID id) {
		super(ERRORCODE.EVENT_LISTENER_ALREADY_REGISTERED, "Event message " + id.toString() + " is already in disk", false);
	}

}
