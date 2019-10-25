
/**
 * @file Implementation.java
 * 
 * @date Sep 26, 2012
 */
package es.bsc.dataclay.util.management.classmgr.python;

import java.util.List;
import java.util.Map;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.util.management.classmgr.AccessedImplementation;
import es.bsc.dataclay.util.management.classmgr.AccessedProperty;
import es.bsc.dataclay.util.management.classmgr.Implementation;
import es.bsc.dataclay.util.management.classmgr.PrefetchingInformation;
import es.bsc.dataclay.util.management.classmgr.Type;
import es.bsc.dataclay.util.management.classmgr.features.QualitativeFeature;
import es.bsc.dataclay.util.management.classmgr.features.QuantitativeFeature;
import es.bsc.dataclay.util.management.classmgr.features.Feature.FeatureType;

/**
 * This class represents a Python Implementation.
 * 
 */
public final class PythonImplementation extends Implementation {

	/** The method implementation for remote executions. */
	private String code;

	/**
	 * Creates an empty PythonImplementation
	 */
	public PythonImplementation() {
		super();
	}

	/**
	 * 
	 * PythonImplementation Specification constructor with provided specifications
	 * @param newposition
	 *            Position of the implementation
	 * @param newaccessedProperties
	 *            PropertyID of the properties accessed by the Implementation
	 * @param newaccessedImplementations
	 *            IDs of the implementations accessed by the Implementation
	 * @param newincludes
	 *            Includes of the implementation
	 * @param newPrefetchingInfo
	 *            An object of PrefetchingInformation
	 * @param newReqQuantitativeFeatures
	 *            Quantitative features required to execute the code
	 * @param newReqQualitativeFeatures
	 *            Qualitative features required to execute the code
	 * @param newnamespace
	 *            Namespace of the class containing the implementation
	 * @param newclassName
	 *            Class name of the class containing the implementation
	 * @param newopSignature
	 *            Signature of the operation
	 * @param newcode
	 *            Code
	 * @post Creates a new ImplementationSpec with provided specifications and generates a new ImplementationID.
	 */
	// CHECKSTYLE:OFF
	public PythonImplementation(final int newposition,
			final List<AccessedProperty> newaccessedProperties,
			final List<AccessedImplementation> newaccessedImplementations,
			final List<Type> newincludes,
			final PrefetchingInformation newPrefetchingInfo,
			final Map<FeatureType, QuantitativeFeature> newReqQuantitativeFeatures,
			final Map<FeatureType, QualitativeFeature> newReqQualitativeFeatures,
			final String newnamespace, final String newclassName,
			final String newopSignature,
			final String newcode) {
		// CHECKSTYLE:ON
		super(newposition, newaccessedProperties, newaccessedImplementations, newincludes,
				newPrefetchingInfo, newReqQuantitativeFeatures, newReqQualitativeFeatures,
				newnamespace, newclassName, newopSignature);
		this.setCode(newcode);

	}

	@Override
	public Langs getLanguage() {
		return Langs.LANG_PYTHON;
	}

	/**
	 * Get the PythonImplementation::code
	 * @return the code
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * Set the PythonImplementation::code
	 * @param newcode
	 *            the code to set
	 */
	public void setCode(final String newcode) {
		this.code = newcode;
	}

}
