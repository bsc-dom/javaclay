
package es.bsc.dataclay.util.management.classmgr.java;

import java.util.UUID;

import org.yaml.snakeyaml.Yaml;

import es.bsc.dataclay.util.management.classmgr.LanguageDependantAnnotationInfo;
import es.bsc.dataclay.util.yaml.CommonYAML;

/**
 * This class represents Java dependant information for an Annotation.
 */
public final class JavaAnnotationInfo implements LanguageDependantAnnotationInfo {

	/** ID. */
	private UUID id;
	/** Access flags. */
	private boolean visible;

	/**
	 * Creates an empty JavaAnnotationInfo
	 * @see
	 */
	public JavaAnnotationInfo() {

	}

	/**
	 * JavaAnnotationInfo constructor
	 * @param newVisible
	 *            Visible flag of the Annotation
	 */
	public JavaAnnotationInfo(final boolean visible) {
		this.setVisible(visible);
	}

	/**
	 * Get the JavaAnnotationInfo::Visible
	 * @return the Visible
	 */

	public boolean getVisible() {
		return visible;
	}

	/**
	 * Set the JavaAnnotationInfo::Visible
	 * @param newVisible
	 *            the Visible to set
	 */
	public void setVisible(final boolean visible) {
		this.visible = visible;
	}

	@Override
	public String toString() {
		final Yaml yaml = CommonYAML.getYamlObject();
		return yaml.dump(this);
	}

	@Override
	public int hashCode() {
		return this.visible ? 1 : 0;
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof JavaAnnotationInfo) {
			final JavaAnnotationInfo other = (JavaAnnotationInfo) object;
			return this.visible == other.getVisible();
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
