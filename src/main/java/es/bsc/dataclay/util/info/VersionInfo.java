
package es.bsc.dataclay.util.info;

/**
 * @file VersionInfo.java
 * 
 * @date Apr 23, 2014
 * 
 */

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.ObjectID;
import es.bsc.dataclay.util.management.metadataservice.MetaDataInfo;

/**
 * This class contains the info created for a version. It will be used to consolidate the version.
 */
public final class VersionInfo implements Serializable {
	/** Serial version UID. */
	private static final long serialVersionUID = 4863984800761279859L;

	/** ObjectID of the main versioned object. */
	private ObjectID versionOID;

	/** location ID in which the version is located. */
	private ExecutionEnvironmentID locID;

	/**
	 * Correspondence between the versionOIDs (key) and the originalOIDs (value) of the versioned object and its subobjects.
	 */
	private final Map<ObjectID, ObjectID> versionsMapping = new LinkedHashMap<>();
	/**
	 * Metadata of the original object. Needed in consolidate (to allow the app to delete original object before consolidating)
	 */
	private final Map<ObjectID, MetaDataInfo> originalMD = new HashMap<>();

	/**
	 * Get the objectID of the main versioned object
	 * 
	 * @return versionOID
	 */
	public ObjectID getVersionOID() {
		return this.versionOID;
	}

	/**
	 * Set the objectID of the main versioned object
	 * 
	 * @param newOID
	 *            OID to be set
	 */
	public void setVersionOID(final ObjectID newOID) {
		this.versionOID = newOID;
	}

	/**
	 * Add a new pair versionOID-originalOID to the mapping
	 * 
	 * @param newOID
	 *            The OID of the version
	 * @param originalOID
	 *            The original OID corresponding to versionOID
	 */
	public void addVersionedObject(final ObjectID newOID, final ObjectID originalOID) {
		this.versionsMapping.put(newOID, originalOID);
	}

	/**
	 * Set the versionOID-originalOID mapping
	 * 
	 * @param versionToOriginal
	 *            A map containing a set of versionOIDs and their correspondence to original OIDs
	 */
	public void setVersionsMapping(final Map<ObjectID, ObjectID> versionToOriginal) {
		if (!versionToOriginal.isEmpty()) {
			this.versionsMapping.putAll(versionToOriginal);
		}
	}

	/**
	 * Get the versionOID-originalOID mapping
	 * 
	 * @return The mapping of the versionInfo
	 */
	public Map<ObjectID, ObjectID> getVersionsMapping() {
		return this.versionsMapping;
	}

	/**
	 * Set the objectID-metadata mapping
	 * 
	 * @param newOriginalMD
	 *            A map containing the metadata of each ObjectID
	 */
	public void setOriginalMD(final Map<ObjectID, MetaDataInfo> newOriginalMD) {
		if (!newOriginalMD.isEmpty()) {
			this.originalMD.putAll(newOriginalMD);
		}
	}

	/**
	 * Get the objectID-metadata mapping
	 * 
	 * @return A map containing the metadata of each ObjectID
	 */
	public Map<ObjectID, MetaDataInfo> getOriginalMD() {
		return this.originalMD;
	}

	@Override
	public String toString() {
		return versionsMapping.toString();
	}

	/**
	 * @return the locID
	 */
	public ExecutionEnvironmentID getLocID() {
		return locID;
	}

	/**
	 * @param theLocID
	 *            the locID to set
	 */
	public void setLocID(final ExecutionEnvironmentID theLocID) {
		this.locID = theLocID;
	}

}
