
/**
 * @file EnrichmentInfo.java
 * 
 * @date Oct 22, 2013
 * 
 */

package es.bsc.dataclay.util.info;

import java.util.Map;
import java.util.Set;

import es.bsc.dataclay.util.ids.ImplementationID;
import es.bsc.dataclay.util.ids.OperationID;
import es.bsc.dataclay.util.ids.PropertyID;
import es.bsc.dataclay.util.management.classmgr.Operation;
import es.bsc.dataclay.util.management.classmgr.Property;

/**
 * This class represents the info created during an enrichment process.
 */
public final class EnrichmentInfo {

	/** Enriched properties' info. */
	private Map<String, Property> properties;
	/** Enriched operations' info. */
	private Map<String, Operation> operations;
	/** Enriched properties indexed by ID. */
	private Map<PropertyID, String> propertiesByID;
	/** Enriched operations indexed by ID. */
	private Map<OperationID, String> operationsByID;
	/** For each operationID that was already present in the original metaclass, the set of implementations that enrich them. */
	private Map<OperationID, Set<ImplementationID>> enrichmentsOfExistentOperations;

	/**
	 * For deserialization purposes.
	 */
	public EnrichmentInfo() {

	}

	/**
	 * EnrichmentInfo constructor with provided specifications and IDs
	 * @param newProperties
	 *            info of the enriched (new) properties
	 * @param newOperations
	 *            info of the enriched (new) operations
	 * @param newPropertiesByID
	 *            enriched properties indexed by ID
	 * @param newOperationsByID
	 *            enriched operations indexed by ID
	 * @param theEnrichmentsOfExistentOperations
	 *            implementations that enrich operations that were already present in class
	 */
	public EnrichmentInfo(final Map<String, Property> newProperties,
			final Map<String, Operation> newOperations, final Map<PropertyID, String> newPropertiesByID,
			final Map<OperationID, String> newOperationsByID,
			final Map<OperationID, Set<ImplementationID>> theEnrichmentsOfExistentOperations) {
		this.setProperties(newProperties);
		this.setOperations(newOperations);
		this.setPropertiesByID(newPropertiesByID);
		this.setOperationsByID(newOperationsByID);
		this.setEnrichmentsOfExistentOperations(theEnrichmentsOfExistentOperations);
	}

	/**
	 * Get the EnrichmentInfo::properties
	 * @return the properties
	 */
	public Map<String, Property> getProperties() {
		return properties;
	}

	/**
	 * Set the EnrichmentInfo::properties
	 * @param newproperties
	 *            the properties to set
	 */
	public void setProperties(final Map<String, Property> newproperties) {
		this.properties = newproperties;
	}

	/**
	 * Get the EnrichmentInfo::operations
	 * @return the operations
	 */
	public Map<String, Operation> getOperations() {
		return operations;
	}

	/**
	 * Set the EnrichmentInfo::operations
	 * @param newoperations
	 *            the operations to set
	 */
	public void setOperations(final Map<String, Operation> newoperations) {
		this.operations = newoperations;
	}

	/**
	 * Get the EnrichmentInfo::propertiesByID
	 * @return the propertiesByID
	 */
	public Map<PropertyID, String> getPropertiesByID() {
		return propertiesByID;
	}

	/**
	 * Set the EnrichmentInfo::propertiesByID
	 * @param newpropertiesByID
	 *            the propertiesByID to set
	 */
	public void setPropertiesByID(final Map<PropertyID, String> newpropertiesByID) {
		this.propertiesByID = newpropertiesByID;
	}

	/**
	 * Get the EnrichmentInfo::operationsByID
	 * @return the operationsByID
	 */
	public Map<OperationID, String> getOperationsByID() {
		return this.operationsByID;
	}

	/**
	 * Set the EnrichmentInfo::operationsByID
	 * @param newoperationsByID
	 *            the operationsByID to set
	 */
	public void setOperationsByID(final Map<OperationID, String> newoperationsByID) {
		this.operationsByID = newoperationsByID;
	}

	/**
	 * Get the EnrichmentInfo::enrichmentsOfExistentOperations
	 * @return the newenrichmentsOfExistentOperations
	 */
	public Map<OperationID, Set<ImplementationID>> getEnrichmentsOfExistentOperations() {
		return enrichmentsOfExistentOperations;
	}

	/**
	 * Set the EnrichmentInfo::enrichmentsOfExistentOperations
	 * @param newenrichmentsOfExistentOperations
	 *            the enrichmentsOfExistentOperations to set
	 */
	public void setEnrichmentsOfExistentOperations(
			final Map<OperationID, Set<ImplementationID>> newenrichmentsOfExistentOperations) {
		this.enrichmentsOfExistentOperations = newenrichmentsOfExistentOperations;
	}
}
