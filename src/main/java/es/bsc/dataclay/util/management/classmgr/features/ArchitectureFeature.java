
/**
 * @file ArchitectureFeature.java
 * @date OCt 10, 2012
 */
package es.bsc.dataclay.util.management.classmgr.features;

import java.util.UUID;

import org.yaml.snakeyaml.Yaml;

import es.bsc.dataclay.util.yaml.CommonYAML;

/**
 * This class represents all Architecture Features (x86, ...).
 * 
 */
public final class ArchitectureFeature extends QualitativeFeature {

	/** ID. */
	private UUID id;

	/** Name of the architecture represented by this feature. */
	private String architectureName;

	/**
	 * Empty constructor for deserialization.
	 */
	public ArchitectureFeature() {

	}

	/**
	 * Constructor with architecture name
	 * @param archName
	 *            Name to be set
	 * @post Creates a new architecture with the provided name
	 */
	public ArchitectureFeature(final String archName) {
		super(FeatureType.ARCHITECTURE);
		if (archName == null || archName.trim().isEmpty()) {
			throw new IllegalArgumentException("Architecture specified is null or empty");
		}
		this.architectureName = archName;
	}

	@Override
	public String toString() {
		final Yaml yaml = CommonYAML.getYamlObject();
		return yaml.dump(this);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof ArchitectureFeature) {
			return this.architectureName.equals(((ArchitectureFeature) obj).getArchitectureName());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.architectureName.hashCode();
	}

	/**
	 * Get architectureName
	 * @return the architectureName
	 */
	public String getArchitectureName() {
		return architectureName;
	}

	/**
	 * Set architectureName
	 * @param newarchitectureName
	 *            the architectureName to set
	 */
	public void setArchitectureName(final String newarchitectureName) {
		this.architectureName = newarchitectureName;
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
