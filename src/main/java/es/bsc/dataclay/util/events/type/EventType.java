
package es.bsc.dataclay.util.events.type;

import es.bsc.dataclay.logic.api.LogicModuleAPI;
import es.bsc.dataclay.util.events.EventTypeOuter.EventTypeEnum;
import es.bsc.dataclay.util.events.message.EventMessage;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.management.accountmgr.PasswordCredential;

/** This class represents an event type. */
public abstract class EventType {

	//CHECKSTYLE:OFF
	/** Event Type enum. Used for priorities. */
	public EventTypeEnum eventTypeEnum;
	//CHECKSTYLE:ON
	
	/**
	 * Constructor used for deserialization.
	 */
	protected EventType() {

	}

	/**
	 * Constructor
	 * @param neweventTypeEnum
	 *            Event type enum
	 */
	protected EventType(final EventTypeEnum neweventTypeEnum) {
		this.eventTypeEnum = neweventTypeEnum;
	}

	/** 
	 * Initialize (if needed) IDs from specifications (example: className -> class ID) 
	 * @param accountID ID of account registering the Event Type 
	 * @param credential Credentials of account registering the Event Type
	 * @param namespace Namespace being used to register events. Used for registration of classes and ECAs at once.
	 * @param lm Reference to LogicModule to seek for IDs and so on. 
	 * 
	 */
	public abstract void init(AccountID accountID, PasswordCredential credential, String namespace, LogicModuleAPI lm);
	
	/**
	 * Checks if event received belongs to this event type.
	 * @param message
	 *            Event message
	 * @return TRUE if message belongs to this event type. FALSE otherwise.
	 */
	public abstract boolean checkIsEventType(final EventMessage message);

	/**
	 * Get priority of the Event Type
	 * @return Priority of the event. Used by Notification Manager for instance: store notifications before delete.
	 */
	public final int getPriority() {
		return this.eventTypeEnum.getPriority();
	}

	/**
	 * @return the eventTypeEnum
	 */
	public final EventTypeEnum getEventTypeEnum() {
		return eventTypeEnum;
	}

	/**
	 * @param theEventTypeEnum the eventTypeEnum to set
	 */
	public final void setEventTypeEnum(final EventTypeEnum theEventTypeEnum) {
		this.eventTypeEnum = theEventTypeEnum;
	}

}
