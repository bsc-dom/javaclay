
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
 * This class represents the exceptions produced in NotificationManager module when some event message is not registered.
 * 
 */
public class EventMessageNotRegisteredException extends DataClayException {

	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 5233261097319859200L;

	/**
	 * This exception is produced when some event message is not registered
	 * @param id
	 *            ID of the event
	 */
	public EventMessageNotRegisteredException(final EventMessageID id) {
		super(ERRORCODE.EVENT_MESSAGE_NOT_REGISTERED, "Event message " + id.toString() + " not registered", false);
	}

}
