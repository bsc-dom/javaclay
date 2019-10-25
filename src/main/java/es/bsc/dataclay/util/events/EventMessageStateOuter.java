
package es.bsc.dataclay.util.events;

import java.util.ResourceBundle;

/**
 * This class represents event message state.
 */
public final class EventMessageStateOuter {

	/** Properties. */
	private static ResourceBundle props = null;

	/** Index of the property that defines the state of the event. */
	private static final int STATE_PROP = 0;

	/**
	 * Utility classes should have private constructor.
	 */
	private EventMessageStateOuter() {

	}

	/**
	 * Init properties of the properties file
	 */
	static {
		if (props == null) {
			props = ResourceBundle.getBundle("properties.event_state");
		}
	}

	/**
	 * This enumeration represent all possible states of events that can occur in DataClay.
	 */
	public enum EventState {

		/** Event was created and send but not received by Notification Manager yet. */
		SEND,

		/** Event is present in queue. */
		QUEUED,

		/** An event was executed and NotificationMgr is waiting for its return. */
		EXECUTING,

		/** Event contains an error. */
		ERROR;

		/** Event state id. */
		private Integer stateID = null;

		/**
		 * Init properties of the properties file
		 */
		private void init() {
			final String[] properties = props.getString(this.name()).split(",");
			final Integer mID = Integer.valueOf(properties[STATE_PROP]);
			setEventStateID(mID);
		}

		/**
		 * Set the EventState::stateID
		 * @param newstateID
		 *            the stateID to set
		 */
		public void setEventStateID(final int newstateID) {
			this.stateID = newstateID;
		}

		/**
		 * Get the EventState::stateID
		 * @return the stateID
		 */
		public int getEventStateID() {
			if (stateID == null) {
				init();
			}
			return stateID;
		}

		/**
		 * Get event state ID
		 * @param id
		 *            ID of event state
		 * @return Event state id
		 */
		public static EventState getFromID(final int id) {
			for (EventState type : EventState.values()) {
				if (type.getEventStateID() == id) {
					return type;
				}
			}
			return null;
		}
	}
}
