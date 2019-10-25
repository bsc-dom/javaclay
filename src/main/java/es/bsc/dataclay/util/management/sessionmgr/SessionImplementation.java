
/**
 * @file SessionImplementation.java
 * @date May 31, 2013
 */
package es.bsc.dataclay.util.management.sessionmgr;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.NamespaceID;

/**
 * This class represents an implementation in a Session.
 * 
 */
public final class SessionImplementation implements Serializable {

	/** ID. */
	private UUID id;
	/** Serial version UID. */
	private static final long serialVersionUID = 4509651755128816900L;
	/** ID of the implementation. */
	private ImplementationID implementationID;
	/** ID of the namespace of the implementation. */
	private NamespaceID namespaceID;
	/** ID of the responsible of the namespace of the implementation. */
	private AccountID respAccountID;

	/**
	 * Empty constructor for specification of requirements while validating sessions
	 */
	public SessionImplementation() {

	}

	/**
	 * SessionImplementation Constructor
	 * @param newimplementationID
	 *            ID of the implementation
	 * @param newnamespaceID
	 *            ID of the namespace of the implementation
	 * @param newrespAccountID
	 *            ID of the responsible of the namespace of the implementation
	 */
	public SessionImplementation(final ImplementationID newimplementationID, final NamespaceID newnamespaceID,
			final AccountID newrespAccountID) {
		this.setImplementationID(newimplementationID);
		this.setNamespaceID(newnamespaceID);
		this.setRespAccountID(newrespAccountID);
	}

	/**
	 * Get the SessionImplementation::implementationID
	 * @return the implementationID
	 */

	public ImplementationID getImplementationID() {
		return implementationID;
	}

	/**
	 * Set the SessionImplementation::implementationID
	 * @param newimplementationID
	 *            the implementationID to set
	 */
	public void setImplementationID(final ImplementationID newimplementationID) {
		this.implementationID = newimplementationID;
	}

	/**
	 * Get the SessionImplementation::namespaceID
	 * @return the namespaceID
	 */

	public NamespaceID getNamespaceID() {
		return namespaceID;
	}

	/**
	 * Set the SessionImplementation::namespaceID
	 * @param newnamespaceID
	 *            the namespaceID to set
	 */
	public void setNamespaceID(final NamespaceID newnamespaceID) {
		this.namespaceID = newnamespaceID;
	}

	/**
	 * Get the SessionImplementation::respAccountID
	 * @return the respAccountID
	 */

	public AccountID getRespAccountID() {
		return respAccountID;
	}

	/**
	 * Set the SessionImplementation::respAccountID
	 * @param newrespAccountID
	 *            the respAccountID to set
	 */
	public void setRespAccountID(final AccountID newrespAccountID) {
		this.respAccountID = newrespAccountID;
	}

	/**
	 * This operation allows to compare this object with other object.
	 * @param t
	 *            Object to compare
	 * @return If the object is the same, returns TRUE. FALSE, otherwise.
	 */
	@Override
	public boolean equals(final Object t) {
		if (t instanceof SessionImplementation) {
			final SessionImplementation other = (SessionImplementation) t;
			return other.getImplementationID().equals(this.getImplementationID())
					&& other.getNamespaceID().equals(this.getNamespaceID());
		}
		return false;
	}

	/**
	 * This operation allows to compare with other Object by setting its Java default Hashcode to a constant. It is
	 *        necessary to override the equality function.
	 * @return the hashcode
	 * @see equals(Object)
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.implementationID);
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
