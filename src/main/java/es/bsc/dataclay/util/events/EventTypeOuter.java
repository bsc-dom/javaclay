
package es.bsc.dataclay.util.events;

import java.util.ResourceBundle;

/**
 * This class represents event types.
 */
public final class EventTypeOuter {

	/** Properties. */
	private static ResourceBundle props = null;

	/** Index of the property that defines the ID of the event. */
	private static final int ID_PROP = 0;
	/** Index of the property that defines the priority of the event. */
	private static final int PRIORITY_PROP = 1;

	/**
	 * Utility classes should have private constructor.
	 */
	private EventTypeOuter() {

	}

	/**
	 * Init properties of the properties file
	 */
	static {
		if (props == null) {
			props = ResourceBundle.getBundle("es.bsc.dataclay.properties.event_type");
		}
	}

	/**
	 * This enumeration represent all possible types of events that can occur in DataClay.
	 */
	public enum EventTypeEnum {

		/** An object was persisted. */
		PERSISTED_OBJ,

		/** An object was deleted. */
		DELETED_OBJ, 
		
		/** An object was updated. */
		UPDATED_OBJ;

		/** Event type priority. */
		private Integer priority = null;
		/** Event ID. */
		private Integer eventID = null;

		/**
		 * Init properties of the properties file
		 */
		private void init() {
			final String[] properties = props.getString(this.name()).split(",");
			final Integer mID = Integer.valueOf(properties[ID_PROP]);
			final Integer prID = Integer.valueOf(properties[PRIORITY_PROP]);
			setEventID(mID);
			setPriority(prID);

		}

		/**
		 * Get the EventType::priority
		 * @return the priority
		 */
		public int getPriority() {
			if (priority == null) {
				init();
			}
			return priority;
		}

		/**
		 * Set the EventType::priority
		 * @param newpriority
		 *            the priority to set
		 */
		public void setPriority(final int newpriority) {
			this.priority = newpriority;
		}

		/**
		 * Get the EventType::eventID
		 * @return the eventID
		 */
		public int getEventID() {
			if (eventID == null) {
				init();
			}
			return eventID;
		}

		/**
		 * Set the EventType::eventID
		 * @param neweventID
		 *            the eventID to set
		 */
		public void setEventID(final int neweventID) {
			this.eventID = neweventID;
		}

		/**
		 * @breif Get event from ID
		 * @param id
		 *            ID of event
		 * @return Event type
		 */
		public static EventTypeEnum getFromID(final int id) {
			for (EventTypeEnum type : EventTypeEnum.values()) {
				if (type.getEventID() == id) {
					return type;
				}
			}
			return null;
		}

	}
}
