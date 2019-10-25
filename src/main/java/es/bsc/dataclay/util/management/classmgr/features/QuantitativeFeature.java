
/**
 * @file QuantitativeFeature.java
 * @date Oct 10, 2012
 */
package es.bsc.dataclay.util.management.classmgr.features;

/**
 * This class represents the Quantitative Features of a backend.
 * 
 */
public abstract class QuantitativeFeature extends Feature implements Comparable<QuantitativeFeature> {

	/**
	 * Creates an empty quantitative feature
	 * @note This function does not generate a QualitativeRegistryID. It is necessary for the queries by example used in db4o.
	 */
	public QuantitativeFeature() {

	}

	/**
	 * QuantitativeFeature constructor
	 * @param theFeatureType
	 *            Type of the feature
	 * @post Creates a new QuantitativeFeature of type provided.
	 */
	public QuantitativeFeature(final FeatureType theFeatureType) {
		super(theFeatureType);
	}
}
