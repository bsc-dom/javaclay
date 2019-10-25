
/**
 * @file StorageLocation.java
 * @date May 23, 2013
 */
package es.bsc.dataclay.util.management.metadataservice;

import es.bsc.dataclay.util.MgrObject;
import es.bsc.dataclay.util.ids.StorageLocationID;

/**
 * This class represents the information of a StorageLocation.
 */
public final class StorageLocation extends MgrObject<StorageLocationID> {

	/** Host name of the Storage Location. */
	private String hostname;
	/** Name of the DataService (shared with the ExecutionEnvironments). */
	private String name;
	/** Port of the StorageLocation. */
	private int storageTCPPort;

	/**
	 * Empty constructor of the StorageLocation
	 */
	public StorageLocation() {

	}

	/**
	 * StorageLocation specification constructor
	 * 
	 * @param newhostname
	 *            Host name of the Storage Location
	 * @param newname
	 *            Name of the Storage Location
	 * @param newtcpport
	 *            TCP Port of the Storage Location
	 * @warning Beware! This function auto-initializes the internal ID field (just
	 *          like all MgrObject). If you require it to be null, either use the
	 *          zero-parameter constructor or proceed with a setDataClayID(null).
	 */
	public StorageLocation(final String newhostname, final String newname, final int newtcpport) {
		this.setDataClayID(new StorageLocationID());
		this.setHostname(newhostname);
		this.setName(newname);
		this.setStorageTCPPort(newtcpport);
	}

	/**
	 * Get the StorageLocation::hostname
	 * 
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * Set the StorageLocation::hostname
	 * 
	 * @param newhostname
	 *            the hostname to set
	 */
	public void setHostname(final String newhostname) {
		this.hostname = newhostname;
	}

	/**
	 * Get the StorageLocation::name
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the StorageLocation::name
	 * 
	 * @param newname
	 *            the name to set
	 */
	public void setName(final String newname) {
		this.name = newname;
	}

	/**
	 * Get the StorageLocation::storageTCPPort
	 * 
	 * @return the port
	 */

	public int getStorageTCPPort() {
		return storageTCPPort;
	}

	/**
	 * Set the StorageLocation::storageTCPPort
	 * 
	 * @param newstoragePort
	 *            the port to set
	 */
	public void setStorageTCPPort(final int newstoragePort) {
		this.storageTCPPort = newstoragePort;
	}

	@Override
	public String toString() {
		return "[SL/" + name + "/" + hostname + ":" + storageTCPPort + "]";

	}

	/**
	 * This operation allows to compare this object with other object.
	 * 
	 * @param t
	 *            Object to compare
	 * @return If the object is the same, returns TRUE. FALSE, otherwise.
	 */
	@Override
	public boolean equals(final Object t) {
		if (t instanceof StorageLocation) {
			final StorageLocation other = (StorageLocation) t;
			return other.getHostname().equals(this.getHostname()) && other.getName().equals(this.getName())
					&& other.getStorageTCPPort() == this.getStorageTCPPort();
		}
		return false;
	}

	/**
	 * This operation allows to compare with other Object by setting its Java
	 * default Hashcode to a constant. It is necessary to override the equality
	 * function.
	 * 
	 * @return the hashcode
	 */
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
}
