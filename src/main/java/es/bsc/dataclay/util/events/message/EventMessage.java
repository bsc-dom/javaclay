
package es.bsc.dataclay.util.events.message;

import es.bsc.dataclay.serialization.lib.SerializedParametersOrReturn;
import es.bsc.dataclay.util.Configuration;
import es.bsc.dataclay.util.events.EventMessageStateOuter.EventState;
import es.bsc.dataclay.util.events.type.EventType;
import es.bsc.dataclay.util.ids.EventMessageID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.yaml.CommonYAML;

/**
 * This event indicates that an object was persisted.
 */
public final class EventMessage implements Comparable<EventMessage> {

	/** Event message ID. */
	private EventMessageID id;

	/** ID of the object that produced the event. */
	private ObjectID producerObjectID;

	/** Accomplished event type. */
	private EventType eventType;

	/** Event message parameters. */
	private SerializedParametersOrReturn params;

	/** Event message state. */
	private EventState eventState;

	/**
	 * Current age for messages. Used to avoid messages that are never read due to low priority. When NotificationManager
	 * receives a message, an age is set. The age of an object is the current time in millis.
	 */
	private long messagePriorityAge;

	/**
	 * Constructor
	 * @param newproducerObjectID
	 *            ID of the object that produced the event
	 * @param neweventType
	 *            Event type accomplished by this event.
	 * @param newparams
	 *            the event message parameters (serialized)
	 */
	public EventMessage(
			final ObjectID newproducerObjectID, 
			final EventType neweventType,
			final SerializedParametersOrReturn newparams) {
		this.setId(new EventMessageID());
		this.setProducerObjectID(newproducerObjectID);
		/**
		 * Notification manager has a map of EventType -> Set of implementations to execute Every time an event arrives, this
		 * map is used and the function 'getEventType' is used to seek for impls.
		 */
		this.setEventType(neweventType);
		this.params = newparams;
		this.setEventState(EventState.SEND);
		this.setMessagePriorityAge(System.currentTimeMillis());
	}

	/**
	 * Used for deserialization
	 */
	public EventMessage() {

	}

	/**
	 * Get Event type
	 * @return Event type
	 */
	public EventType getEventType() {
		return eventType;
	}

	/**
	 * Set event type
	 * @param theEventType
	 *            Event type
	 */
	public void setEventType(final EventType theEventType) {
		this.eventType = theEventType;
	}

	/**
	 * Get the EventMessage::id
	 * @return the id
	 */
	public EventMessageID getId() {
		return id;
	}

	/**
	 * Set the EventMessage::id
	 * @param newid
	 *            the id to set
	 */
	public void setId(final EventMessageID newid) {
		this.id = newid;
	}

	@Override
	public String toString() {
		return CommonYAML.getYamlObject().dump(this);
	}

	@Override
	public int hashCode() {
		// Hashcode of this event is combination of event condition and message itself.
		// Actually not used since compareTo is implemented. But it's a good practice to have one.
		return new Long(this.messagePriorityAge).hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		/**
		 * Two messages are equals IF: 1 - They accomplish same conditions 2 - Parameters are equals
		 */
		if (obj == null) {
			return false;
		}
		if (obj instanceof EventMessage) {
			final EventMessage candidate = (EventMessage) obj;
			return candidate.getEventType().equals(this.getEventType())
					// && candidate.getParams().keySet().equals(this.getParams().keySet())
					&& candidate.getProducerObjectID().equals(this.getProducerObjectID());
		}
		return false;
	}

	@Override
	public int compareTo(final EventMessage other) {

		// return (int) (this.messagePriorityAge - other.getMessagePriorityAge());

		/**
		 * The natural ordering for a class C is said to be consistent with equals if and only if e1.compareTo(e2) == 0 has the
		 * same boolean value as e1.equals(e2) for every e1 and e2 of class C. Note that null is not an instance of any class,
		 * and e.compareTo(null) should throw a NullPointerException even though e.equals(null) returns false.
		 */

		if (other == null) {
			throw new NullPointerException();
		}

		// Return of this function, according to Java standars, are:
		// Positive integer: I have less priority than other
		// Negative integer: I have more priority than other
		// Zero: same priority or same object.

		// The Event Type has a priority
		int priorityDifference = this.getEventType().getPriority()
				- other.getEventType().getPriority();

		// If same priority then insertion-order
		if (priorityDifference == 0) {
			priorityDifference = (int) (this.messagePriorityAge - other.getMessagePriorityAge());
		}

		// If this object has LESS priority check if it's too old.
		if (priorityDifference > 0) {
			// Check if i'm too old
			// We consider a message is TOO old if it was received after a certain time.
			final long elapsedTime = System.currentTimeMillis() - this.messagePriorityAge;
			if (elapsedTime > Configuration.Flags.MAX_TIME_OLD_EVENT_MSGS.getLongValue()) {
				// I have a little bit more priority than the other.
				priorityDifference = other.getEventType().getPriority() - 1;
			}
		}

		return priorityDifference;

	}

	/**
	 * Get the EventMessage::params
	 * @return the params
	 */
	public SerializedParametersOrReturn getParams() {
		return this.params;
	}

	/**
	 * Set the EventMessage::params
	 * @param newparams
	 *            the params to set
	 */
	public void setParams(final SerializedParametersOrReturn newparams) {
		this.params = newparams;
	}

	/**
	 * Get the EventMessage::messagePriorityAge
	 * @return the messagePriorityAge
	 */
	public long getMessagePriorityAge() {
		return messagePriorityAge;
	}

	/**
	 * Set the EventMessage::messagePriorityAge
	 * @param newmessagePriorityAge
	 *            the messagePriorityAge to set
	 */
	public void setMessagePriorityAge(final long newmessagePriorityAge) {
		this.messagePriorityAge = newmessagePriorityAge;
	}

	/**
	 * Get the EventMessage::producerObjectID
	 * @return the producerObjectID
	 */
	public ObjectID getProducerObjectID() {
		return producerObjectID;
	}

	/**
	 * Set the EventMessage::producerObjectID
	 * @param newproducerObjectID
	 *            the producerObjectID to set
	 */
	public void setProducerObjectID(final ObjectID newproducerObjectID) {
		this.producerObjectID = newproducerObjectID;
	}

	/**
	 * Get the EventMessage::eventState
	 * @return the eventState
	 */
	public EventState getEventState() {
		return eventState;
	}

	/**
	 * Set the EventMessage::eventState
	 * @param neweventState
	 *            the eventState to set
	 */
	public void setEventState(final EventState neweventState) {
		this.eventState = neweventState;
	}

}
