
/**
 * @file InterfaceInContract.java
 * @date Oct 1, 2012
 */

package es.bsc.dataclay.util.management.contractmgr;

import java.util.Map;
import java.util.Map.Entry;

import es.bsc.dataclay.util.ids.InterfaceID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.management.interfacemgr.Interface;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * This class represents the association between a specific Interface and a certain contract.
 * 
 */
public final class InterfaceInContract {

	/** ID. */
	private UUID id;
	// === YAML SPECIFICATION === //
	// Properties must be public for YAML parsing.
	// CHECKSTYLE:OFF
	/** Interface added to this contract. */
	private Interface iface;
	/**
	 * Number of implementations (local and remote) for each operation in contract. If no implementation is specified, we get
	 * the first one for the operation.
	 */
	private Set<OpImplementations> implementationsSpecPerOperation;

	// ==== DYNAMIC FIELDS ==== //
	/** The id of the interface this interface in contract refers to. */
	public InterfaceID interfaceID;
	/** The accessible implementations of every operation of this interface in the contract. */
	public Map<OperationID, OpImplementations> accessibleImplementations;
	// CHECKSTYLE:ON

	/**
	 * Basic constructor for testing purposes
	 */
	public InterfaceInContract() {

	}

	/**
	 * Constructor to set the accessible methods.
	 * @param newiface
	 *            the interface in contract
	 * @param newimplementationsSpecPerOperation
	 *            Specifications of implementations
	 */
	public InterfaceInContract(final Interface newiface,
			final Set<OpImplementations> newimplementationsSpecPerOperation) {
		this.setIface(newiface);
		this.setInterfaceID(newiface.getDataClayID());
		this.setImplementationsSpecPerOperation(newimplementationsSpecPerOperation);
	}

	/**
	 * @return the interfaceID
	 */
	public InterfaceID getInterfaceID() {
		return interfaceID;
	}

	/**
	 * @param idOfTheInterface
	 *            the interfaceID to set
	 */
	public void setInterfaceID(final InterfaceID idOfTheInterface) {
		this.interfaceID = idOfTheInterface;
	}

	/**
	 * @return the accessibleImplementations
	 */
	public Map<OperationID, OpImplementations> getAccessibleImplementations() {
		return accessibleImplementations;
	}

	/**
	 * @param accessibleImplementationsFromThisInterrfaceInContract
	 *            the accessibleImplementations to set
	 */
	public void setAccessibleImplementations(
			final Map<OperationID, OpImplementations> accessibleImplementationsFromThisInterrfaceInContract) {
		if (accessibleImplementationsFromThisInterrfaceInContract != null) { // Yaml safety
			for (final Entry<OperationID, OpImplementations> curEntry : accessibleImplementationsFromThisInterrfaceInContract.entrySet()) {
				final OperationID opID = curEntry.getKey();
				final OpImplementations curImpls = curEntry.getValue();
				if (curImpls == null) {
					throw new IllegalArgumentException("No implementations provided for the op: " + opID);
				}
				if (curImpls.getLocalImplementationID() == null) {
					throw new IllegalArgumentException("Missing local implementation for the op: " + opID);
				}
				if (curImpls.getRemoteImplementationID() == null) {
					throw new IllegalArgumentException("Missing remote implementation for the op: " + opID);
				}
			}
		}
		this.accessibleImplementations = accessibleImplementationsFromThisInterrfaceInContract;
	}

	// CHECKSTYLE:ON
	// === DYNAMIC FIELDS === //

	/**
	 * Get the InterfaceInContract::implementationsSpecPerOperation
	 * @return the implementationsSpecPerOperation
	 */
	public Set<OpImplementations> getImplementationsSpecPerOperation() {
		return this.implementationsSpecPerOperation;
	}

	/**
	 * Set the InterfaceInContract::implementationsSpecPerOperation
	 * @param newimplementationsSpecPerOperation
	 *            the implementationsSpecPerOperation to set
	 */
	public void setImplementationsSpecPerOperation(final Set<OpImplementations> newimplementationsSpecPerOperation) {
		this.implementationsSpecPerOperation = newimplementationsSpecPerOperation;
	}

	/**
	 * Get iface
	 * @return the iface
	 */
	public Interface getIface() {
		return iface;
	}

	/**
	 * Set iface
	 * @param newiface
	 *            the iface to set
	 */
	public void setIface(final Interface newiface) {
		this.iface = newiface;
	}

	@Override
	public boolean equals(final Object ifaceInContract) {
		if (ifaceInContract instanceof InterfaceInContract) {
			final InterfaceInContract otherIface = (InterfaceInContract) ifaceInContract;
			return this.iface.equals(otherIface.iface)
					&& this.accessibleImplementations.equals(otherIface.accessibleImplementations);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.iface, this.accessibleImplementations);
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
