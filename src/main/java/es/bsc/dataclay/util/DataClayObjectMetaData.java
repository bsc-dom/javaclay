
package es.bsc.dataclay.util;

import java.util.Map;
import java.util.Map.Entry;

import es.bsc.dataclay.DataClayObject;
import es.bsc.dataclay.commonruntime.DataClayRuntime;
import es.bsc.dataclay.util.ids.DataClayInstanceID;
import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.MetaClassID;
import es.bsc.dataclay.util.ids.ObjectID;

import java.util.Objects;

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

	/** Map tag to external dataClay ID */
	private Map<Integer, DataClayInstanceID> extDataClayIDs;

	/** Number of references pointing to object. */
	private int numRefs;

	/**
	 * Empty constructor for deserialization.
	 */
	public DataClayObjectMetaData() {

	}

	/**
	 * Constructor
	 * 
	 * @param newmapOids
	 *            Map tag to OID.
	 * @param newmapClassIDs
	 *            Map tag to class id.
	 * @param newmapHints
	 *            Map tag to hint id.
	 * @param newextDataClayIDs
	 *            Map tag to external dataClay id.
	 * @param newnumRefs
	 *            Number of references pointing to object.
	 */
	public DataClayObjectMetaData(final Map<Integer, ObjectID> newmapOids,
			final Map<Integer, MetaClassID> newmapClassIDs, final Map<Integer, ExecutionEnvironmentID> newmapHints,
			final Map<Integer, DataClayInstanceID> newextDataClayIDs, final int newnumRefs) {
		this.setOids(newmapOids);
		this.setClassIDs(newmapClassIDs);
		this.setHints(newmapHints);
		this.setNumRefs(newnumRefs);
	}

	@Override
	public int hashCode() {
		return Objects.hash(oids, classIDs, hints);
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
	 * Get a specific dataClayID for a tag
	 * 
	 * @param tag
	 *            the tag of the dataClayID to be retrieved
	 * @return dataClayID corresponding to given tag
	 */
	public DataClayInstanceID getDataClayID(final Integer tag) {
		return this.extDataClayIDs.get(tag);
	}

	/**
	 * Modify oids
	 * 
	 * @param oidsMapping
	 *            Oids mapping
	 * @param hintsMapping
	 *            Hints of objects
	 */
	public void modifyOids(final Map<ObjectID, ObjectID> oidsMapping,
			final Map<ObjectID, ExecutionEnvironmentID> hintsMapping) {
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
				final ExecutionEnvironmentID newHint = hintsMapping.get(newOID);

				if (newHint != null) {
					if (DEBUG_ENABLED) {
						DataClayObject.getLib();
						DataClayRuntime.LOGGER.debug("[==ModifyIDs==] Setting Hint "
								+ DataClayObject.getLib().getDSNameOfHint(newHint) + " with oid " + newOID);
					}

					this.hints.put(tag, newHint);
				}
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

	public String toString() { 
		StringBuilder strb = new StringBuilder();
		strb.append("{\n");
		strb.append("numRefs = " + this.numRefs + "\n");
		strb.append("oids = " + this.oids + "\n");
		strb.append("classIDs = " + this.classIDs + "\n");
		strb.append("hints = " + this.hints + "\n");
		strb.append("extDataClayIDs = " + this.extDataClayIDs + "\n");
		strb.append("}\n");
		return strb.toString();
	}

}
