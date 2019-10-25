
package es.bsc.dataclay.util.management.classmgr.java;

import java.util.UUID;

import es.bsc.dataclay.util.management.classmgr.LanguageDependantTypeInfo;

/**
 * This class represents Java dependant information for a Type.
 * 
 * @version 0.1
 */
public final class JavaTypeInfo implements LanguageDependantTypeInfo {

	/** ID. */
	private UUID id;

	/**
	 * Creates an empty JavaTypeInfo
	 * @see
	 */
	public JavaTypeInfo() {

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
