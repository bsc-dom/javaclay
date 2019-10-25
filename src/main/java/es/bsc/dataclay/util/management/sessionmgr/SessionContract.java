
/**
 * @file SessionContract.java
 * @date May 31, 2013
 */
package es.bsc.dataclay.util.management.sessionmgr;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

import es.bsc.dataclay.util.ids.ContractID;
import es.bsc.dataclay.util.ids.InterfaceID;

/**
 * This class represents a contract in a Session.
 * 
 */
public final class SessionContract implements Serializable {

	/** Serial version UID. */
	private static final long serialVersionUID = 1691598571325665067L;

	/** ID. */
	private UUID id;
	/** ID of the Contract. */
	private ContractID contractID;
	/** Interfaces of this contract in this session. */
	private Map<InterfaceID, SessionInterface> sessionInterfaces;

	/**
	 * Empty constructor for specification of requirements while validating sessions
	 */
	public SessionContract() {

	}

	/**
	 * Session contract constructor
	 * @param newcontractID
	 *            ID of the Contract
	 */
	public SessionContract(final ContractID newcontractID) {
		this.setContractID(newcontractID);
	}

	/**
	 * Get the SessionContract::contractID
	 * @return the contractID
	 */

	public ContractID getContractID() {
		return contractID;
	}

	/**
	 * Set the SessionContract::contractID
	 * @param newcontractID
	 *            the contractID to set
	 */
	public void setContractID(final ContractID newcontractID) {
		this.contractID = newcontractID;
	}

	/**
	 * Get the SessionContract::sessionInterfaces
	 * @return the sessionInterfaces
	 */

	public Map<InterfaceID, SessionInterface> getSessionInterfaces() {
		return sessionInterfaces;
	}

	/**
	 * Set the SessionContract::sessionInterfaces
	 * @param newsessionInterfaces
	 *            the sessionInterfaces to set
	 */
	public void setSessionInterfaces(final Map<InterfaceID, SessionInterface> newsessionInterfaces) {
		this.sessionInterfaces = newsessionInterfaces;
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
