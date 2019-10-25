
package es.bsc.dataclay.util.management.classmgr.python;

import java.util.UUID;

import es.bsc.dataclay.util.management.classmgr.LanguageDependantOperationInfo;

/**
 * This class represents Python dependant information for an Operation.
 * 
 */
public final class PythonOperationInfo implements LanguageDependantOperationInfo {

	/** ID. */
	private UUID id;

	/**
	 * Creates an empty PythonOperationInfo
	 * @see
	 */
	public PythonOperationInfo() {

	}

	/**
	 * @return the id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * @param theid
	 *            the id to set
	 */
	public void setId(final UUID theid) {
		this.id = theid;
	}

}
