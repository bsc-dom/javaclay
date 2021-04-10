
package es.bsc.dataclay.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.commonruntime.DataClayRuntime;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/** This class represents all metadata (tags) of an object. */
public final class DataClayObjectMetaData {

	/** Indicates if debug is enabled. */
	protected static final boolean DEBUG_ENABLED = Configuration.isDebugEnabled();

	/**
	 * @pierlauro sept. 2018 TODO eliminte this creepy, anti-OO and inefficient design with plenty of maps: create a class Tag
	 *            containing ObjectID, MetaClassID, ExecutionEnvironmentID, etc. and just keep a set/map of Tag here
	 */

	/** Map tag to OID. */
	private Map<Integer, ObjectID> oids;

	/** Map tag to class id. */
	private Map<Integer, MetaClassID> classIDs;

	/** Map tag to hint id. */
	private Map<Integer, ExecutionEnvironmentID> hints;

	/** Number of references pointing to object. */
	private int numRefs;

	/** Object alias. */
	private String alias;

	/** Indicates if object is read only. */
	private boolean isReadOnly;

	/** ID of original object in case of new version. */
	private ObjectID originalObjectID;

	/** ID of root object location (versions, replicas) - first object. */
	private ExecutionEnvironmentID rootLocation = null;

	/** ID of origin object location (replicas) : origin of the replica. */
	private ExecutionEnvironmentID originLocation = null;

	/** IDs of locations this object was replicated to. */
	private Set<ExecutionEnvironmentID> replicaLocations = ConcurrentHashMap.newKeySet();

	/**
	 * Empty constructor for deserialization.
	 */
	public DataClayObjectMetaData() {

	}

	/**
	 * Constructor
	 * @param thealias Alias of the object or null if none
	 * @param theisReadOnly indicates if object is read only
	 * @param newmapOids
	 *            Map tag to OID.
	 * @param newmapClassIDs
	 *            Map tag to class id.
	 * @param newmapHints
	 *            Map tag to hint id.
	 * @param newnumRefs
	 *            Number of references pointing to object.
	 * @param theoriginalObjectID Original object id in case of new version
	 * @param therootLocation root location of the original object or null if this is original
	 * @param theoriginLocation origin location of the object in case of replica
	 * @param thereplicaLocations IDs of locations this replica-object was replicated to
	 */
	public DataClayObjectMetaData(final String thealias, final boolean theisReadOnly, final Map<Integer, ObjectID> newmapOids,
			final Map<Integer, MetaClassID> newmapClassIDs, final Map<Integer, ExecutionEnvironmentID> newmapHints,
			final int newnumRefs,  final ObjectID theoriginalObjectID,
								  final ExecutionEnvironmentID therootLocation,
								  final ExecutionEnvironmentID theoriginLocation,
								  final Set<ExecutionEnvironmentID> thereplicaLocations) {
		this.setAlias(thealias);
		this.setIsReadOnly(theisReadOnly);
		this.setOids(newmapOids);
		this.setClassIDs(newmapClassIDs);
		this.setHints(newmapHints);
		this.setNumRefs(newnumRefs);
		this.originalObjectID = theoriginalObjectID;
		this.setRootLocation(therootLocation);
		this.setOriginLocation(theoriginLocation);
		// IMPORTANT: clone to avoid modifications of metadata affect already serialized objects
		if (thereplicaLocations != null) {
			this.replicaLocations.addAll(thereplicaLocations);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(alias, oids, classIDs, hints);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof DataClayObjectMetaData) {

			final DataClayObjectMetaData candidate = (DataClayObjectMetaData) obj;
			return oids.equals(candidate.getOids()) && classIDs.equals(candidate.getClassIDs())
					&& hints.equals(candidate.getHints());
		} else {
			return false;
		}
	}

	/**
	 * @return the oids
	 */
	public Map<Integer, ObjectID> getOids() {
		return oids;
	}

	/**
	 * @param theoids
	 *            the oids to set
	 */
	public void setOids(final Map<Integer, ObjectID> theoids) {
		this.oids = theoids;
	}

	/**
	 * @return the classIDs
	 */
	public Map<Integer, MetaClassID> getClassIDs() {
		return classIDs;
	}

	/**
	 * @param theclassIDs
	 *            the classIDs to set
	 */
	public void setClassIDs(final Map<Integer, MetaClassID> theclassIDs) {
		this.classIDs = theclassIDs;
	}

	/**
	 * @return the hints
	 */
	public Map<Integer, ExecutionEnvironmentID> getHints() {
		return hints;
	}

	/**
	 * @param thehints
	 *            the hints to set
	 */
	public void setHints(final Map<Integer, ExecutionEnvironmentID> thehints) {
		this.hints = thehints;
	}

	/**
	 * @param tag
	 *            the tag to update
	 * @param hint
	 *            the new tag's hint
	 */
	public void setHint(final Integer tag, final ExecutionEnvironmentID hint) {
		this.hints.put(tag, hint);
	}

	/**
	 * Get object id from tag
	 * 
	 * @param tag
	 *            tag
	 * @return Object id
	 */
	public ObjectID getObjectID(final Integer tag) {
		return this.oids.get(tag);
	}

	/**
	 * Get class id from tag
	 * 
	 * @param tag
	 *            tag
	 * @return Class id
	 */
	public MetaClassID getMetaClassID(final Integer tag) {
		return this.classIDs.get(tag);
	}

	/**
	 * Get hint from tag
	 * 
	 * @param tag
	 *            tag
	 * @return hint
	 */
	public ExecutionEnvironmentID getHint(final Integer tag) {
		return this.hints.get(tag);
	}

	/**
	 * Modify oids
	 * 
	 * @param oidsMapping
	 *            Oids mapping
	 */
	public void modifyOids(final Map<ObjectID, ObjectID> oidsMapping) {
		for (final Entry<Integer, ObjectID> curTagEntry : this.oids.entrySet()) {
			final Integer tag = curTagEntry.getKey();
			final ObjectID oldOID = curTagEntry.getValue();
			final ObjectID newOID = oidsMapping.get(oldOID);
			if (newOID != null) {
				if (DEBUG_ENABLED) {
					DataClayObject.getLib();
					DataClayRuntime.LOGGER
							.debug("[==ModifyIDs==] Setting objectID " + newOID + " instead of " + oldOID);
				}
				this.oids.put(tag, newOID);
				/*if (hintsMapping != null) {
					final ExecutionEnvironmentID newHint = hintsMapping.get(newOID);
					if (newHint != null) {
						if (DEBUG_ENABLED) {
							DataClayObject.getLib();
							DataClayRuntime.LOGGER.debug("[==ModifyIDs==] Setting Hint "
									+ DataClayObject.getLib().getDSNameOfHint(newHint) + " with oid " + newOID);
						}

						this.hints.put(tag, newHint);
					}
				}*/
			}
		}

	}


	/**
	 * Modify hints
	 *
	 * @param hintsMapping
	 *            Hints of objects
	 */
	public void modifyHints(final Map<ObjectID, ExecutionEnvironmentID> hintsMapping) {
		for (final Entry<Integer, ObjectID> curTagEntry : this.oids.entrySet()) {
			final Integer tag = curTagEntry.getKey();
			final ObjectID oid = curTagEntry.getValue();
			final ExecutionEnvironmentID newHint = hintsMapping.get(oid);
			if (newHint != null) {
				this.hints.put(tag, newHint);
			}
		}

	}

	/**
	 * @return the numRefs
	 */
	public int getNumRefs() {
		return numRefs;
	}

	/**
	 * @param thenumRefs
	 *            the numRefs to set
	 */
	public void setNumRefs(final int thenumRefs) {
		this.numRefs = thenumRefs;
	}

	/**
	 *
	 * @return root location of the object or null if current is root
	 */
	public ExecutionEnvironmentID getRootLocation() {
		return rootLocation;
	}

	/**
	 * Set root location of the object
	 * @param rootLocation root location to set
	 */
	public void setRootLocation(ExecutionEnvironmentID rootLocation) {
		this.rootLocation = rootLocation;
	}

	/**
	 *
	 * @return origin location of the object or null if current is origin
	 */
	public ExecutionEnvironmentID getOriginLocation() {
		return originLocation;
	}

	/**
	 * Set origin location of the object
	 * @param originLocation origin location to set
	 */
	public void setOriginLocation(ExecutionEnvironmentID originLocation) {
		this.originLocation = originLocation;
	}

	/**
	 * Get all replica locations
	 * @return Replica locations
	 */
	public Set<ExecutionEnvironmentID> getReplicaLocations() {
		return replicaLocations;
	}

	/**
	 * Set replica locations
	 * @param replicaLocations replica locations to set
	 */
	public void setReplicaLocations(Set<ExecutionEnvironmentID> replicaLocations) {
		this.replicaLocations = replicaLocations;
	}

	/**
	 * Get alias
	 * @return the alias of the object
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * Set the alias of the object
	 * @param alias the alias of the object
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String toString() { 
		StringBuilder strb = new StringBuilder();
		strb.append("{\n");
		strb.append("alias = " + this.alias + "\n");
		strb.append("numRefs = " + this.numRefs + "\n");
		strb.append("isReadOnly = " + this.isReadOnly + "\n");
		strb.append("oids = " + this.oids + "\n");
		strb.append("classIDs = " + this.classIDs + "\n");
		strb.append("hints = " + this.hints + "\n");
		strb.append("originalObjectID = " + this.originalObjectID + "\n");
		strb.append("rootLocation = " + this.rootLocation + "\n");
		strb.append("originLocation = " + this.originLocation + "\n");
		strb.append("replicaLocations = " + this.replicaLocations + "\n");
		strb.append("}\n");
		return strb.toString();
	}

	/**
	 * Get original object ID if versioned
	 * @return Original object ID
	 */
	public ObjectID getOriginalObjectID() {
		return this.originalObjectID;
	}

	/**
	 * Set original object ID
	 * @param origObjectID original object ID to set
	 */
	public void setOriginalObjectID(ObjectID origObjectID) {
		this.originalObjectID = origObjectID;
	}

	/**
	 * Get if object is read only
	 * @return Boolean indicating if object is read only or not
	 */
	public boolean getIsReadOnly() {
		return this.isReadOnly;
	}

	/**
	 * set if object is read only
	 * @param newisReadOnly  Boolean indicating if object is read only or not
	 */
	public void setIsReadOnly(boolean newisReadOnly) {
		this.isReadOnly = newisReadOnly;
	}
}
