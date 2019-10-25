
/**
 * @file Feature.java
 * @date OCt 8, 2012
 */
package es.bsc.dataclay.util.management.classmgr.features;

/**
 * This class implements a Feature.
 * 
 */

public class Feature  {

	/**
	 * Enum that represents the possible types of features considering also their hierarchy.
	 */
	public enum FeatureType {

		/** Quantitative features. */
		QUANTITATIVE(),
		/** Number of CPUs. */
		CPU(QUANTITATIVE),
		/** Memory available. */
		MEMORY(QUANTITATIVE),
		/** Qualitative features. */
		QUALITATIVE(),
		/** Architecture (x86, x64). */
		ARCHITECTURE(QUALITATIVE),
		/** Programming language. */
		LANGUAGE(QUALITATIVE);

		/** Parent type. Each feature belongs to a parent type that can be qualitative or quantitative. */
		private FeatureType parent;

		/**
		 * Feature Type constructor.
		 */
		FeatureType() {
			this.parent = null;
		}

		/**
		 * Feature Type constructor with parent type.
		 * @param theParent
		 *            The parent type
		 */
		FeatureType(final FeatureType theParent) {
			this.setParent(theParent);
		}

		/**
		 * Get the FeatureType::parent
		 * @return the parent
		 */
		public FeatureType getParent() {
			return this.parent;
		}

		/**
		 * Set the FeatureType::parent
		 * @param theParent
		 *            the parent to set
		 */
		public void setParent(final FeatureType theParent) {
			if (theParent == null) {
				throw new IllegalArgumentException("If a parent is set it cannot be null");
			}
			this.parent = theParent;
		}
	}

	/** Feature type. */
	private FeatureType featureType;

	/**
	 * Creates an empty feature
	 * @note This function does not generate a QualitativeRegistryID. It is necessary for the queries by example used in db4o.
	 */
	public Feature() {
	}

	/**
	 * Feature constructor
	 * @param theFeatureType
	 *            Type of the feature
	 * @post Creates a new Feature of type provided.
	 */
	public Feature(final FeatureType theFeatureType) {
		this.setType(theFeatureType);
	}

	/**
	 * Get the type of this Feature
	 * @return Feature::featureType of container Feature.
	 */
	public final FeatureType getType() {
		return this.featureType;
	}

	/**
	 * Set the Feature::featureType of this Feature
	 * @param theFeatureType
	 *            Type to set
	 */
	public final void setType(final FeatureType theFeatureType) {
		if (theFeatureType == null) {
			throw new IllegalArgumentException("Feature type cannot be null");
		}
		this.featureType = theFeatureType;
	}

}
