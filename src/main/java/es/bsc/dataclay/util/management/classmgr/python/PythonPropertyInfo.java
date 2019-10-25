
package es.bsc.dataclay.util.management.classmgr.python;

import java.util.UUID;

import es.bsc.dataclay.util.management.classmgr.LanguageDependantPropertyInfo;

/**
 * This class represents Python dependant information for a Property.
 * 
 */
public final class PythonPropertyInfo implements LanguageDependantPropertyInfo {

	/** ID. */
	private UUID id;

	/**
	 * Creates an empty PythonPropertyInfo
	 * @see
	 */
	public PythonPropertyInfo() {

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
