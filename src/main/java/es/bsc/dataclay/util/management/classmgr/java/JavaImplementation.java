
/**
 * @file Implementation.java
 *
 * @date Sep 26, 2012
 */
package es.bsc.dataclay.util.management.classmgr.java;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.yaml.snakeyaml.Yaml;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.util.management.classmgr.AccessedImplementation;
import es.bsc.dataclay.util.management.classmgr.AccessedProperty;
import es.bsc.dataclay.util.management.classmgr.Implementation;
import es.bsc.dataclay.util.management.classmgr.PrefetchingInformation;
import es.bsc.dataclay.util.management.classmgr.Type;
import es.bsc.dataclay.util.management.classmgr.features.QualitativeFeature;
import es.bsc.dataclay.util.management.classmgr.features.QuantitativeFeature;
import es.bsc.dataclay.util.management.classmgr.features.Feature.FeatureType;
import es.bsc.dataclay.util.yaml.CommonYAML;

/**
 * This class represents a Java Implementation.
 */
public final class JavaImplementation extends Implementation {

	/**
	 * Creates an empty JavaImplementation
	 */
	public JavaImplementation() {
		super();
	}

	/**
	 *
	 * Java Implementation Specification constructor with provided specifications
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
	 * @param newopNameAndDescriptor
	 *            Name and Descriptor of the operation
	 * @post Creates a new ImplementationSpec with provided specifications and generates a new ImplementationID.
	 */
	// CHECKSTYLE:OFF
	public JavaImplementation(final int newposition,
			final List<AccessedProperty> newaccessedProperties,
			final List<AccessedImplementation> newaccessedImplementations,
			final List<Type> newincludes,
			final PrefetchingInformation newPrefetchingInfo,
			final Map<FeatureType, QuantitativeFeature> newReqQuantitativeFeatures,
			final Map<FeatureType, QualitativeFeature> newReqQualitativeFeatures,
			final String newnamespace, final String newclassName,
			final String newopNameAndDescriptor) {
		// CHECKSTYLE:ON
		super(newposition, newaccessedProperties, newaccessedImplementations, newincludes,
				newPrefetchingInfo, newReqQuantitativeFeatures, newReqQualitativeFeatures,
				newnamespace, newclassName, newopNameAndDescriptor);
	}

	@Override
	public Langs getLanguage() {
		return Langs.LANG_JAVA;
	}

	@Override
	public String toString() {
		final Yaml yaml = CommonYAML.getYamlObject();
		return yaml.dump(this);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.getNamespace(), this.getClassName(), this.getOpNameAndDescriptor(), this.getPosition());
	}

	@Override
	public boolean equals(final Object object) {
		if (object instanceof JavaImplementation) {
			final JavaImplementation other = (JavaImplementation) object;
			return this.getNamespace().equals(other.getNamespace())
					&& this.getClassName().equals(other.getClassName())
					&& this.getOpNameAndDescriptor().equals(other.getOpNameAndDescriptor())
					&& this.getPosition() == other.getPosition();
		}
		return false;
	}

}
