
/**
 * @file SessionOperation.java
 * @date May 31, 2013
 */
package es.bsc.dataclay.util.management.sessionmgr;

import java.io.Serializable;
import java.util.UUID;

import es.bsc.dataclay.util.ids.OperationID;

/**
 * This class represents an operation in a Session.
 * 
 */
public final class SessionOperation implements Serializable {

	/** ID. */
	private UUID id;
	/** Serial version UID. */
	private static final long serialVersionUID = 8315490393701070267L;
	/** ID of the operation. */
	private OperationID operationID;
	/** The session local implementation. */
	private SessionImplementation sessionLocalImplementation;
	/** The session remote implementation. */
	private SessionImplementation sessionRemoteImplementation;

	/**
	 * Empty constructor for specification of requirements while validating sessions
	 */
	public SessionOperation() {

	}

	/**
	 * SessionOperation constructor
	 * @param newoperationID
	 *            ID of the operation
	 * @param newsessionLocalImplementation
	 *            the session local implementation
	 * @param newsessionRemoteImplementation
	 *            the session remote implementation
	 */
	public SessionOperation(final OperationID newoperationID, final SessionImplementation newsessionLocalImplementation,
			final SessionImplementation newsessionRemoteImplementation) {
		this.setOperationID(newoperationID);
		this.setSessionLocalImplementation(newsessionLocalImplementation);
		this.setSessionRemoteImplementation(newsessionRemoteImplementation);
	}

	/**
	 * Get the SessionOperation::operationID
	 * @return the operationID
	 */

	public OperationID getOperationID() {
		return operationID;
	}

	/**
	 * Set the SessionOperation::operationID
	 * @param newoperationID
	 *            the operationID to set
	 */
	public void setOperationID(final OperationID newoperationID) {
		this.operationID = newoperationID;
	}

	/**
	 * Get the SessionOperation::sessionLocalImplementation
	 * @return the sessionLocalImplementation
	 */

	public SessionImplementation getSessionLocalImplementation() {
		return sessionLocalImplementation;
	}

	/**
	 * Set the SessionOperation::sessionLocalImplementation
	 * @param newsessionLocalImplementation
	 *            the sessionLocalImplementation to set
	 */
	public void setSessionLocalImplementation(final SessionImplementation newsessionLocalImplementation) {
		this.sessionLocalImplementation = newsessionLocalImplementation;
	}

	/**
	 * Get the SessionOperation::sessionRemoteImplementation
	 * @return the sessionRemoteImplementation
	 */

	public SessionImplementation getSessionRemoteImplementation() {
		return sessionRemoteImplementation;
	}

	/**
	 * Set the SessionOperation::sessionRemoteImplementation
	 * @param newsessionRemoteImplementation
	 *            the sessionRemoteImplementation to set
	 */
	public void setSessionRemoteImplementation(final SessionImplementation newsessionRemoteImplementation) {
		this.sessionRemoteImplementation = newsessionRemoteImplementation;
	}

	/**
	 * get id
	 * @return id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * Set id
	 * @param newid
	 *            the id
	 */
	public void setId(final UUID newid) {
		this.id = newid;
	}
}
