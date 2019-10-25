
/**
 * @file QualitativeFeature.java
 * @date Oct 10, 2012
 */
package es.bsc.dataclay.util.management.classmgr.features;

/**
 * This class represents the Qualitative Features of a backend.
 * 
 */
public abstract class QualitativeFeature extends Feature {

	/**
	 * Creates an empty qualitative feature
	 * @note This function does not generate a QualitativeRegistryID. It is necessary for the queries by example used in db4o.
	 */
	public QualitativeFeature() {

	}

	/**
	 * QualitativeFeature constructor
	 * @param theFeatureType
	 *            Type of the feature
	 * @post Creates a new QualitativeFeature of type provided.
	 */
	public QualitativeFeature(final FeatureType theFeatureType) {
		super(theFeatureType);
	}

}
