
package es.bsc.dataclay.dataservice;

import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.ids.SessionID;

/** DataClay Lazy task. */
public final class LazyTask {

	/** Session that is going to run the task. */
	private SessionID sessionID;

	/** Object of the task. */
	private ObjectID objectID;

	/** ID of the task. */
	private ImplementationID implementationID;

	/** ID of the class in which the task is located. */
	private final MetaClassID classID;

	/**
	 * Constructor.
	 * 
	 * @param theSessionID
	 *            Session that is going to run the task.
	 * @param theObjectID
	 *            Object of the task.
	 * @param theImplementationID
	 *            ID of the task.
	 * @param theClassID
	 *            ID of the class in which the task is located.
	 */
	public LazyTask(final SessionID theSessionID, final ObjectID theObjectID,
			final ImplementationID theImplementationID, final MetaClassID theClassID) {
		this.sessionID = theSessionID;
		this.objectID = theObjectID;
		this.implementationID = theImplementationID;
		this.classID = theClassID;
	}

	/**
	 * @return the sessionID
	 */
	public SessionID getSessionID() {
		return sessionID;
	}

	/**
	 * @param thesessionID
	 *            the sessionID to set
	 */
	public void setSessionID(final SessionID thesessionID) {
		this.sessionID = thesessionID;
	}

	/**
	 * @return the objectID
	 */
	public ObjectID getObjectID() {
		return objectID;
	}

	/**
	 * @param theobjectID
	 *            the objectID to set
	 */
	public void setObjectID(final ObjectID theobjectID) {
		this.objectID = theobjectID;
	}

	/**
	 * @return the implementationID
	 */
	public ImplementationID getImplementationID() {
		return implementationID;
	}

	/**
	 * @param theimplementationID
	 *            the implementationID to set
	 */
	public void setImplementationID(final ImplementationID theimplementationID) {
		this.implementationID = theimplementationID;
	}

	/**
	 * @return the classID
	 */
	public MetaClassID getClassID() {
		return classID;
	}

}
