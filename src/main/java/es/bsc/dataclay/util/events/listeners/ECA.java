
package es.bsc.dataclay.util.events.listeners;

import java.util.Objects;

import es.bsc.dataclay.util.events.message.EventMessage;
import es.bsc.dataclay.util.events.type.EventType;
import es.bsc.dataclay.util.ids.ECAID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.yaml.CommonYAML;

/**
 * This class represents an Event-Condition-Action specification.
 */
public final class ECA implements Comparable<ECA> {

	/** Event listener id. */
	private ECAID id;

	/** Indicates the target class of objects that will execute the action if the event occurs and filter method succeeds. */
	private MetaClassID targetClass;

	/** Indicates event type. */
	private EventType eventType;

	/** Indicates the operation that must be executed in the producer when that event occurs. */
	private OperationID filterMethod;

	/** Signature of filter method. For ECAs defined in classes. Class containing method is the one with ECAs defined. */
	private String filterMethodSignature;
	
	/** Indicates the operation that must be executed when that event occurs. */
	private OperationID action;

	/** Signature of action method. For ECAs defined in classes. Class containing method is the one with ECAs defined. */
	private String actionSignature;
	
	/**
	 * Constructor
	 * @param neweventType
	 *            Indicates event type
	 * @param newtargetClass
	 *            Indicates class of objects that are going to be target of the action.
	 * @param newfilterMethod
	 *            Indicates the implementation that must be executed in the producer when that event occurs
	 * @param newaction
	 *            Indicates the implementation that must be executed when that event occurs
	 */
	public ECA(final EventType neweventType,
			final MetaClassID newtargetClass,
			final OperationID newfilterMethod, final OperationID newaction) {
		this.setId(new ECAID());
		this.setTargetClass(newtargetClass);
		this.setEventType(neweventType);
		this.setFilterMethod(newfilterMethod);
		this.setAction(newaction);
	}
	
	
	/**
	 * Constructor for ECAs defined in classes 
	 * @param neweventType
	 *            Indicates event type
	 * @param newfilterMethodSignatureInThisClass
	 *            Indicates the implementation that must be executed in the producer when that event occurs
	 * @param newactionSingatureInThisClass
	 *            Indicates the implementation that must be executed when that event occurs
	 */
	public ECA(final EventType neweventType,
			final String newfilterMethodSignatureInThisClass, final String newactionSingatureInThisClass) {
		this.setId(new ECAID());
		this.setEventType(neweventType);
		this.setFilterMethodSignature(newfilterMethodSignatureInThisClass);
		this.setActionSignature(newactionSingatureInThisClass);
	}

	/**
	 * Used for deserialization
	 */
	public ECA() {

	}

	/**
	 * Get event type
	 * @return Event Type
	 */
	public EventType getEventType() {
		return eventType;
	}

	/**
	 * Set event type
	 * @param newEventType
	 *            Event type
	 */
	public void setEventType(final EventType newEventType) {
		this.eventType = newEventType;
	}

	/**
	 * Get the EventListener::action
	 * @return the action
	 */
	public OperationID getAction() {
		return action;
	}

	/**
	 * Set the EventListener::action
	 * @param newaction
	 *            the action to set
	 */
	public void setAction(final OperationID newaction) {
		this.action = newaction;
	}

	/**
	 * Get the EventListener::filterMethod
	 * @return the filterMethod
	 */
	public OperationID getFilterMethod() {
		return filterMethod;
	}

	/**
	 * Set the EventListener::filterMethod
	 * @param newfilterMethod
	 *            the filterMethod to set
	 */
	public void setFilterMethod(final OperationID newfilterMethod) {
		this.filterMethod = newfilterMethod;
	}

	/**
	 * Get the EventListenerImpl::id
	 * @return the id
	 */
	public ECAID getId() {
		return id;
	}

	/**
	 * Set the EventListenerImpl::id
	 * @param newid
	 *            the id to set
	 */
	public void setId(final ECAID newid) {
		this.id = newid;
	}

	/**
	 * Checks if message accomplishes event type
	 * @param message
	 *            Message to check.
	 * @return TRUE if message accomplishes event type
	 */
	public boolean checkIsEventType(final EventMessage message) {
		return this.eventType.checkIsEventType(message);
	}

	@Override
	public String toString() {
		return CommonYAML.getYamlObject().dump(this);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.eventType);
	}

	@Override
	public boolean equals(final Object obj) {
		/**
		 * Two ECAs are equals IF: They accomplish same event type, filter method and action
		 */
		if (obj == null) {
			return false;
		}
		if (obj instanceof ECA) {
			final ECA candidate = (ECA) obj;

			if (this.getFilterMethod() == null && candidate.getFilterMethod() != null) {
				return false;
			}
			if (candidate.getFilterMethod() == null && this.getFilterMethod() != null) {
				return false;
			}

			return candidate.getEventType().equals(this.getEventType())
					&& candidate.getAction().equals(this.getAction());
		}
		return false;
	}

	/**
	 * The natural ordering for a class C is said to be consistent with equals if and only if e1.compareTo(e2) == 0 has the same
	 * boolean value as e1.equals(e2) for every e1 and e2 of class C. Note that null is not an instance of any class, and
	 * e.compareTo(null) should throw a NullPointerException even though e.equals(null) returns false.
	 */
	@Override
	public int compareTo(final ECA other) {
		if (other == null) {
			throw new NullPointerException();
		}

		// Return of this function, according to Java standars, are:
		// Positive integer: LESS priority
		// Negative integer: MORE priority
		// Zero: same priority or same object.

		// The Event Type has a priority
		return other.getEventType().getPriority() - this.getEventType().getPriority();
	}

	/**
	 * Returns the ID of target class
	 * @return the id of target class
	 */
	public MetaClassID getTargetClass() {
		return targetClass;
	}

	/**
	 * Sets the ID of target class
	 * @param newTargetClass
	 *            new ID for targetClass
	 */
	public void setTargetClass(final MetaClassID newTargetClass) {
		this.targetClass = newTargetClass;
	}

	/**
	 * @return the filterMethodSignature
	 */
	public String getFilterMethodSignature() {
		return filterMethodSignature;
	}

	/**
	 * @param newfilterMethodSignature the filterMethodSignature to set
	 */
	public void setFilterMethodSignature(final String newfilterMethodSignature) {
		this.filterMethodSignature = newfilterMethodSignature;
	}

	/**
	 * @return the actionSignature
	 */
	public String getActionSignature() {
		return actionSignature;
	}

	/**
	 * @param newactionSignature the actionSignature to set
	 */
	public void setActionSignature(final String newactionSignature) {
		this.actionSignature = newactionSignature;
	}
}
