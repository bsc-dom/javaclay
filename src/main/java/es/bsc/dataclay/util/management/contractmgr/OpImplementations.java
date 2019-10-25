
/**
 * @file OpImplementations.java
 * @date Jun 10, 2013
 */

package es.bsc.dataclay.util.management.contractmgr;

import java.util.Objects;
import java.util.UUID;

import es.bsc.dataclay.util.ids.ImplementationID;

/**
 * Struct to keep information about the accessible implementations of an operation.
 * 
 */
public final class OpImplementations {

	/** ID. */
	private UUID id;

	// === YAML SPECIFICATION === //
	// Properties must be public for YAML parsing.
	// CHECKSTYLE:OFF
	/** Operation signature. */
	private String operationSignature;
	/** Local implementation number. */
	private int numLocalImpl;
	/** Remote implementation number. */
	private int numRemoteImpl;

	// ==== DYNAMIC FIELDS ==== //
	/** Local implementation ID. */
	private ImplementationID localImplementationID;
	/** Remove implementation ID. */
	private ImplementationID remoteImplementationID;
	// CHECKSTYLE:ON

	/**
	 * OpImplementations empty constructor
	 * @details Used in YAML construction.
	 */
	public OpImplementations() {

	}

	/**
	 * OpImplementations constructor
	 * @param newopSignature
	 *            Operation signature
	 * @param localImpl
	 *            Local implementation position.
	 * @param remoteImpl
	 *            Remove implementation position.
	 */
	public OpImplementations(final String newopSignature, final int localImpl, final int remoteImpl) {
		this.setOperationSignature(newopSignature);
		this.setNumLocalImpl(localImpl);
		this.setNumRemoteImpl(remoteImpl);
	}

	/**
	 * Get the OpImplementations::operationSignature
	 * @return the operationSignature
	 */
	public String getOperationSignature() {
		return this.operationSignature;
	}

	/**
	 * Set the OpImplementations::operationSignature
	 * @param newoperationSignature
	 *            the operationSignature to set
	 */
	public void setOperationSignature(final String newoperationSignature) {
		this.operationSignature = newoperationSignature;
	}

	/**
	 * Get the OpImplementations::localImplementationID
	 * @return the local implementation ID
	 */
	public ImplementationID getLocalImplementationID() {
		return localImplementationID;
	}

	/**
	 * Sets OpImplementations::localImplementationID
	 * @param localImplID
	 *            Local implementation ID to set
	 */
	public void setLocalImplementationID(final ImplementationID localImplID) {
		this.localImplementationID = localImplID;
	}

	/**
	 * Get OpImplementations::remoteImplementationID
	 * @return the remote implementation ID
	 */
	public ImplementationID getRemoteImplementationID() {
		return remoteImplementationID;
	}

	/**
	 * Set OpImplementations::remoteImplementationID
	 * @param remoteImplID
	 *            the remote implementation ID to set
	 */
	public void setRemoteImplementationID(final ImplementationID remoteImplID) {
		this.remoteImplementationID = remoteImplID;
	}

	/**
	 * Get the OpImplementations::numLocalImpl
	 * @return the numLocalImpl
	 */
	public int getNumLocalImpl() {
		return this.numLocalImpl;
	}

	/**
	 * Set the OpImplementations::numLocalImpl
	 * @param newnumLocalImpl
	 *            the numLocalImpl to set
	 */
	public void setNumLocalImpl(final int newnumLocalImpl) {
		this.numLocalImpl = newnumLocalImpl;
	}

	/**
	 * Get the OpImplementations::numRemoteImpl
	 * @return the numRemoteImpl
	 */
	public int getNumRemoteImpl() {
		return this.numRemoteImpl;
	}

	/**
	 * Set the OpImplementations::numRemoteImpl
	 * @param newnumRemoteImpl
	 *            the numRemoteImpl to set
	 */
	public void setNumRemoteImpl(final int newnumRemoteImpl) {
		this.numRemoteImpl = newnumRemoteImpl;
	}

	/**
	 * This operation allows to compare this object with other object.
	 * @param t
	 *            to compare
	 * @return If the object has the same implementations, returns TRUE. FALSE, otherwise.
	 */
	@Override
	public boolean equals(final Object t) {
		if (t instanceof OpImplementations) {
			final OpImplementations other = (OpImplementations) t;
			if (other.getLocalImplementationID() == null) {
				if (other.getRemoteImplementationID() == null) {
					return this.remoteImplementationID == null
							&& this.localImplementationID == null;
				} else {
					return this.localImplementationID == null
							&& other.getRemoteImplementationID().equals(this.remoteImplementationID);
				}
			}
			if (other.getRemoteImplementationID() == null) {
				if (other.getLocalImplementationID() == null) {
					return this.localImplementationID == null
							&& this.remoteImplementationID == null;
				} else {
					return this.remoteImplementationID == null
							&& other.getLocalImplementationID().equals(this.localImplementationID);
				}
			}
			return other.getLocalImplementationID().equals(this.localImplementationID)
					&& other.getRemoteImplementationID().equals(this.remoteImplementationID);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.operationSignature, this.numLocalImpl, this.numRemoteImpl);
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
