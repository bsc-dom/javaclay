
/**
 * @file Implementation.java
 *
 * @date Sep 26, 2012
 */
package es.bsc.dataclay.util.management.classmgr;

import java.util.List;
import java.util.Map;

import es.bsc.dataclay.communication.grpc.messages.common.CommonMessages.Langs;
import es.bsc.dataclay.util.MgrObject;
import es.bsc.dataclay.util.ids.AccountID;
import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.NamespaceID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.management.classmgr.features.QualitativeFeature;
import es.bsc.dataclay.util.management.classmgr.features.QuantitativeFeature;
import es.bsc.dataclay.util.management.classmgr.features.Feature.FeatureType;


/**
 * This class represents an Implementation.
 *
 */
public abstract class Implementation
extends MgrObject<ImplementationID> implements Comparable<Implementation> {

	// === YAML SPECIFICATION === //
	// Properties must be public for YAML parsing.
	//CHECKSTYLE:OFF
	/** Name of the registrator of the implementation. */
	private String responsibleAccountName;
	/** Name of namespace of the class of the operation. */
	private String namespace;
	/** Name of the class of the operation. */
	private String className;
	/** Name and descriptor of the operation. */
	private String opNameAndDescriptor;
	/** Position of implementation. */
	private int position;
	/** List of included classes. */
	private List<Type> includes;
	/** Accessed Properties. */
	private List<AccessedProperty> accessedProperties;
	/** Accessed Implementations. */
	private List<AccessedImplementation> accessedImplementations;
	/** Quantitative features required to execute this implementation. */
	private Map<FeatureType, QuantitativeFeature> requiredQuantitativeFeatures;
	/** Qualitative features required to execute this implementation. */
	private Map<FeatureType, QualitativeFeature> requiredQualitativeFeatures;

	//CHECKSTYLE:ON
	// ==== DYNAMIC FIELDS ==== //
	/** ID of the Operation containing the Implementation. */
	private OperationID operationID;
	/** ID of the Class containing the Implementation. */
	private MetaClassID metaClassID;
	/** Responsible of the implementation. */
	private AccountID responsibleAccountID;
	/** ID of the namespace in which the implementation has been created. */
	private NamespaceID namespaceID;
	/** Object containing all of the prefetching information. **/
	private PrefetchingInformation prefetchingInfo;

	/**
	 * Creates an empty Method
	 */
	public Implementation() {

	}

	/**
	 *
	 * Implementation Specification constructor with provided specifications
	 * @param newposition
	 * 			  Position of the implementation
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
	public Implementation(final int newposition,
			final List<AccessedProperty> newaccessedProperties,
			final List<AccessedImplementation> newaccessedImplementations,
			final List<Type> newincludes,
			final PrefetchingInformation newPrefetchingInfo,
			final Map<FeatureType, QuantitativeFeature> newReqQuantitativeFeatures,
			final Map<FeatureType, QualitativeFeature> newReqQualitativeFeatures,
			final String newnamespace, final String newclassName,
			final String newopNameAndDescriptor) {
		// CHECKSTYLE:ON
		this.setPosition(newposition);
		this.setPrefetchingInfo(newPrefetchingInfo);
		this.setRequiredQuantitativeFeatures(newReqQuantitativeFeatures);
		this.setRequiredQualitativeFeatures(newReqQualitativeFeatures);
		this.setIncludes(newincludes);
		this.setAccessedProperties(newaccessedProperties);
		this.setAccessedImplementations(newaccessedImplementations);
		this.setNamespace(newnamespace);
		this.setClassName(newclassName);
		this.setOpNameAndDescriptor(newopNameAndDescriptor);
	}

	/**
	 * Retrieve the language for this implementation.
	 * @return The Langs code for the specific implementation.
	 */
	public abstract Langs getLanguage();

	@Override
	public final int compareTo(final Implementation other) {
		return this.getPosition() - other.getPosition();
	}

	/**
	 * Get the Implementation::responsibleAccountName
	 * @return the responsibleAccountName
	 */
	public final String getResponsibleAccountName() {
		return this.responsibleAccountName;
	}

	/**
	 * Set the Implementation::responsibleAccountName
	 * @param newresponsibleAccountName the responsibleAccountName to set
	 */
	public final void setResponsibleAccountName(final String newresponsibleAccountName) {
		this.responsibleAccountName = newresponsibleAccountName;
	}

	/**
	 * Get the Implementation::namespace
	 * @return the namespace
	 */
	public final String getNamespace() {
		return this.namespace;
	}

	/**
	 * Set the Implementation::namespace
	 * @param newnamespace the namespace to set
	 */
	public final void setNamespace(final String newnamespace) {
		this.namespace = newnamespace;
	}

	/**
	 * Get the Implementation::className
	 * @return the className
	 */
	public final String getClassName() {
		return this.className;
	}

	/**
	 * Set the Implementation::className
	 * @param newclassName the className to set
	 */
	public final void setClassName(final String newclassName) {
		this.className = newclassName;
	}

	/**
	 * Get the Implementation::opNameAndDescriptor
	 * @return the opNameAndDescriptor
	 */
	public final String getOpNameAndDescriptor() {
		return this.opNameAndDescriptor;
	}

	/**
	 * Set the Implementation::opNameAndDescriptor
	 * @param newopNameAndDescriptor the opNameAndDescriptor to set
	 */
	public final void setOpNameAndDescriptor(final String newopNameAndDescriptor) {
		this.opNameAndDescriptor = newopNameAndDescriptor;
	}

	/**
	 * Get the Implementation::position
	 * @return the position
	 */
	public final int getPosition() {
		return this.position;
	}

	/**
	 * Set the Implementation::position
	 * @param newposition the position to set
	 */
	public final void setPosition(final int newposition) {
		this.position = newposition;
	}

	/**
	 * Get the Implementation::includes
	 * @return the includes
	 */
	public final List<Type> getIncludes() {
		return this.includes;
	}

	/**
	 * Set the Implementation::includes
	 * @param newincludes the includes to set
	 */
	public final void setIncludes(final List<Type> newincludes) {
		this.includes = newincludes;
	}

	/**
	 * Get the Implementation::accessedProperties
	 * @return the accessedProperties
	 */
	public final List<AccessedProperty> getAccessedProperties() {
		return this.accessedProperties;
	}

	/**
	 * Set the Implementation::accessedProperties
	 * @param newaccessedProperties the accessedProperties to set
	 */
	public final void setAccessedProperties(final List<AccessedProperty> newaccessedProperties) {
		this.accessedProperties = newaccessedProperties;
	}

	/**
	 * Get the Implementation::accessedImplementations
	 * @return the accessedImplementations
	 */
	public final List<AccessedImplementation> getAccessedImplementations() {
		return this.accessedImplementations;
	}

	/**
	 * Set the Implementation::accessedImplementations
	 * @param newaccessedImplementations the accessedImplementations to set
	 */
	public final void setAccessedImplementations(final List<AccessedImplementation> newaccessedImplementations) {
		this.accessedImplementations = newaccessedImplementations;
	}

	/**
	 * Get the Implementation::requiredQuantitativeFeatures
	 * @return the requiredQuantitativeFeatures
	 */
	public final Map<FeatureType, QuantitativeFeature> getRequiredQuantitativeFeatures() {
		return this.requiredQuantitativeFeatures;
	}

	/**
	 * Set the Implementation::requiredQuantitativeFeatures
	 * @param newrequiredQuantitativeFeatures the requiredQuantitativeFeatures to set
	 */
	public final void setRequiredQuantitativeFeatures(final Map<FeatureType, QuantitativeFeature> newrequiredQuantitativeFeatures) {
		this.requiredQuantitativeFeatures = newrequiredQuantitativeFeatures;
	}

	/**
	 * Get the Implementation::requiredQualitativeFeatures
	 * @return the requiredQualitativeFeatures
	 */
	public final Map<FeatureType, QualitativeFeature> getRequiredQualitativeFeatures() {
		return this.requiredQualitativeFeatures;
	}

	/**
	 * Set the Implementation::requiredQualitativeFeatures
	 * @param newrequiredQualitativeFeatures the requiredQualitativeFeatures to set
	 */
	public final void setRequiredQualitativeFeatures(final Map<FeatureType, QualitativeFeature> newrequiredQualitativeFeatures) {
		this.requiredQualitativeFeatures = newrequiredQualitativeFeatures;
	}

	/**
	 * Get the Implementation::operationID
	 * @return the operationID
	 */
	public final OperationID getOperationID() {
		return this.operationID;
	}

	/**
	 * Set the Implementation::operationID
	 * @param newoperationID the operationID to set
	 */
	public final void setOperationID(final OperationID newoperationID) {
		this.operationID = newoperationID;
	}

	/**
	 * Get the Implementation::metaClassID
	 * @return the metaClassID
	 */
	public final MetaClassID getMetaClassID() {
		return this.metaClassID;
	}

	/**
	 * Set the Implementation::metaClassID
	 * @param newmetaClassID the metaClassID to set
	 */
	public final void setMetaClassID(final MetaClassID newmetaClassID) {
		this.metaClassID = newmetaClassID;
	}

	/**
	 * Get the Implementation::responsibleAccountID
	 * @return the responsibleAccountID
	 */
	public final AccountID getResponsibleAccountID() {
		return this.responsibleAccountID;
	}

	/**
	 * Set the Implementation::responsibleAccountID
	 * @param newresponsibleAccountID the responsibleAccountID to set
	 */
	public final void setResponsibleAccountID(final AccountID newresponsibleAccountID) {
		this.responsibleAccountID = newresponsibleAccountID;
	}

	/**
	 * Get the Implementation::namespaceID
	 * @return the namespaceID
	 */
	public final NamespaceID getNamespaceID() {
		return this.namespaceID;
	}

	/**
	 * Set the Implementation::namespaceID
	 * @param newnamespaceID the namespaceID to set
	 */
	public final void setNamespaceID(final NamespaceID newnamespaceID) {
		this.namespaceID = newnamespaceID;
	}

	/**
	 * Get the Implementation::prefetchingInfo
	 * @return the prefetchingInfo
	 */
	public final PrefetchingInformation getPrefetchingInfo() {
		return this.prefetchingInfo;
	}

	/**
	 * Set the Implementation::prefetchingInfo
	 * @param newprefetchingInfo the prefetchingInfo to set
	 */
	public final void setPrefetchingInfo(final PrefetchingInformation newprefetchingInfo) {
		this.prefetchingInfo = newprefetchingInfo;
	}

}
