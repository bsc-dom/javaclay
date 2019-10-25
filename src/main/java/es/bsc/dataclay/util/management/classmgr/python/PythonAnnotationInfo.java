
package es.bsc.dataclay.util.management.classmgr.python;

import java.util.UUID;

import es.bsc.dataclay.util.management.classmgr.LanguageDependantAnnotationInfo;

/**
 * This class represents Python dependant information for an Annotation.
 */
public final class PythonAnnotationInfo implements LanguageDependantAnnotationInfo {

	/** ID. */
	private UUID id;

	/**
	 * Creates an empty PythonAnnotationInfo
	 * @see
	 */
	public PythonAnnotationInfo() {

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
