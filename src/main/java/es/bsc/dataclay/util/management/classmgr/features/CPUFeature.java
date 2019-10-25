
/**
 * @file CPUFeature.java
 * @date OCt 10, 2012
 */

package es.bsc.dataclay.util.management.classmgr.features;

import java.util.UUID;

import org.yaml.snakeyaml.Yaml;

import es.bsc.dataclay.util.yaml.CommonYAML;

/**
 * This class represents all CPU Features (amount of cpus,...).
 * 
 */
public final class CPUFeature extends QuantitativeFeature {

	/** ID. */
	private UUID id;

	/** Amount of CPUs in the resource. */
	private int amount;

	/**
	 * Empty constructor for deserialization.
	 */
	public CPUFeature() {

	}

	/**
	 * Creates a new CPUFeature with the given amount of CPUs
	 * @param amountOfCPUs
	 *            number of CPUs in the resource
	 */
	public CPUFeature(final int amountOfCPUs) {
		super(FeatureType.CPU);
		setAmount(amountOfCPUs);
	}

	/**
	 * @return the amount of CPUs
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @param newAmt
	 *            the number of CPUs to be set
	 */
	public void setAmount(final int newAmt) {
		if (newAmt <= 0) {
			throw new IllegalArgumentException("Bad amount of CPUs specified: " + newAmt + "(must be > 0)");
		}
		this.amount = newAmt;
	}

	@Override
	public int compareTo(final QuantitativeFeature o) {
		return this.amount - ((CPUFeature) o).getAmount();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof CPUFeature) {
			return this.amount == ((CPUFeature) obj).getAmount();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return amount;
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
