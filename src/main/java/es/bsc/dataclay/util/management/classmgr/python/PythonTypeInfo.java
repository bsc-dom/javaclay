
package es.bsc.dataclay.util.management.classmgr.python;

import java.util.UUID;

import es.bsc.dataclay.util.management.classmgr.LanguageDependantTypeInfo;

/**
 * This class represents Python dependant information for a Type.
 * 
 * @version 0.1
 */
public final class PythonTypeInfo implements LanguageDependantTypeInfo {

	/** ID. */
	private UUID id;
	/** Signature of the Type. */
	private String signature;

	/**
	 * Creates an empty PythonTypeInfo
	 * @see
	 */
	public PythonTypeInfo() {

	}

	/**
	 * PythonTypeInfo Constructor with provided signature
	 * @param newsignature
	 *            the signature to be set
	 */
	public PythonTypeInfo(final String newsignature) {
		this.setSignature(newsignature);
	}

	@Override
	public int hashCode() {
		return this.signature.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof PythonTypeInfo) {
			final PythonTypeInfo other = (PythonTypeInfo) obj;
			if (this.signature != null) {
				if (!this.signature.equals(other.getSignature())) {
					return false;
				}
			} else {
				if (other.getSignature() != null) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Set the Type::signature
	 * @param newsignature
	 *            the signature to be set
	 */
	public void setSignature(final String newsignature) {
		if (newsignature == null) {
			throw new IllegalArgumentException("Signature cannot be null");
		}
		this.signature = newsignature;
	}

	/**
	 * Get the Type::signature
	 * @return the current signature
	 */
	public String getSignature() {
		return signature;
	}

	@Override
	public String toString() {
		return "PythonTypeInfo(signature = " + signature + ")";
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
