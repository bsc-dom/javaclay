
/**
 * @file MemoryFeature.java
 * @date OCt 10, 2012
 */

package es.bsc.dataclay.util.management.classmgr.features;

import java.util.UUID;

import org.yaml.snakeyaml.Yaml;

import es.bsc.dataclay.util.yaml.CommonYAML;

/**
 * This class represents all Memory Features (capacity,...).
 * 
 */
public final class MemoryFeature extends QuantitativeFeature {

	/** ID. */
	private UUID id;
	/** Amount of CPUs in the resource. */
	private int capacityInMB;

	/**
	 * Empty constructor for deserialization.
	 */
	public MemoryFeature() {

	}

	/**
	 * Creates a new MemoryFeature with the given capacity
	 * @param megabytes
	 *            capacity in megabytes
	 */
	public MemoryFeature(final int megabytes) {
		super(FeatureType.MEMORY);
		this.setCapacityInMB(megabytes);
	}

	/**
	 * @return the capacity of the memory
	 */
	public int getCapacityInMB() {
		return capacityInMB;
	}

	/**
	 * @param newCapacityInMb
	 *            the capacity to be set
	 */
	public void setCapacityInMB(final int newCapacityInMb) {
		if (newCapacityInMb <= 0) {
			throw new IllegalArgumentException("Bad capacity in MB : " + newCapacityInMb + " (must be > 0)");
		}
		this.capacityInMB = newCapacityInMb;
	}

	@Override
	public int compareTo(final QuantitativeFeature o) {
		return this.capacityInMB - ((MemoryFeature) o).getCapacityInMB();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof MemoryFeature) {
			return this.capacityInMB == ((MemoryFeature) obj).getCapacityInMB();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return capacityInMB;
	}

	@Override
	public String toString() {
		final Yaml yaml = CommonYAML.getYamlObject();
		return yaml.dump(this);
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
