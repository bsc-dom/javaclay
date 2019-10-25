
/**
 * @file ObjectAlreadyRegisteredException.java
 * 
 * @date May 28, 2013
 */
package es.bsc.dataclay.exceptions.logicmodule.notificationmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.EventObjsMeetConditionID;

/**
 * This class represents the exceptions produced in NotificationManager module when some EventObjsMeetConditionspecified is
 * already registered.
 * 
 */
public class EventObjsMeetConditionAlreadyRegisteredException extends DataClayException {

	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = -2594001241559608856L;

	/**
	 * This exception is produced when some EventObjsMeetCondition is already registered
	 * @param id
	 *            ID of the event
	 */
	public EventObjsMeetConditionAlreadyRegisteredException(final EventObjsMeetConditionID id) {
		super(ERRORCODE.EVENT_OBJS_MEET_CONDITION_ALREADY_REGISTERED,
				"Event objs meet condition " + id.toString() + " is already registered", false);
	}

}
