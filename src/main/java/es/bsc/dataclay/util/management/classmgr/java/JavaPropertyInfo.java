
package es.bsc.dataclay.util.management.classmgr.java;

import java.util.UUID;

import org.yaml.snakeyaml.Yaml;

import es.bsc.dataclay.util.management.classmgr.LanguageDependantPropertyInfo;
import es.bsc.dataclay.util.yaml.CommonYAML;

/**
 * This class represents Java dependant information for a Property.
 * 
 */
public final class JavaPropertyInfo implements LanguageDependantPropertyInfo {

	/** ID. */
	private UUID id;
	/** Access flags. */
	private int modifier;

	/**
	 * Creates an empty JavaPropertyInfo
	 * @see
	 */
	public JavaPropertyInfo() {

	}

	/**
	 * JavaPropertyInfo constructor
	 * @param newmodifier
	 *            Modifier flag of the property
	 */
	public JavaPropertyInfo(
			final int newmodifier) {
		this.setModifier(newmodifier);
	}

	/**
	 * Get the JavaPropertyInfo::modifier
	 * @return the modifier
	 */

	public int getModifier() {
		return modifier;
	}

	/**
	 * Set the JavaPropertyInfo::modifier
	 * @param newmodifier
	 *            the modifier to set
	 */
	public void setModifier(final int newmodifier) {
		this.modifier = newmodifier;
	}

	@Override
	public String toString() {
		final Yaml yaml = CommonYAML.getYamlObject();
		return yaml.dump(this);
	}

	@Override
	public int hashCode() {
		return this.modifier;

	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof JavaPropertyInfo) {
			final JavaPropertyInfo other = (JavaPropertyInfo) object;
			return this.modifier == other.getModifier();
		}
		return false;
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
