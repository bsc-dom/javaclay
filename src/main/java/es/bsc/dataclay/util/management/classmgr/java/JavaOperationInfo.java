
package es.bsc.dataclay.util.management.classmgr.java;

import java.util.UUID;

import es.bsc.dataclay.util.management.classmgr.LanguageDependantOperationInfo;

/**
 * This class represents Java dependant information for an Operation.
 * 
 */
public final class JavaOperationInfo implements LanguageDependantOperationInfo {

	/** ID. */
	private UUID id;
	/** Access flags. */
	private int modifier;

	/**
	 * Creates an empty JavaOperationInfo
	 * @see
	 */
	public JavaOperationInfo() {

	}

	/**
	 * JavaOperationInfo constructor
	 * @param newmodifier
	 *            Modifier flag of the operation
	 */
	public JavaOperationInfo(final int newmodifier) {
		this.setModifier(newmodifier);
	}

	/**
	 * Get the JavaOperationInfo::modifier
	 * @return the modifier
	 */

	public int getModifier() {
		return modifier;
	}

	/**
	 * Set the JavaOperationInfo::modifier
	 * @param newmodifier
	 *            the modifier to set
	 */
	public void setModifier(final int newmodifier) {
		this.modifier = newmodifier;
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
