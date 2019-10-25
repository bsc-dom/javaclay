
package es.bsc.dataclay.util;

import javax.persistence.Id;

import es.bsc.dataclay.util.ids.ID;

/**
 * Abstraction for UUID in management objects.
 * @param <T> Type of the ID
 */
public abstract class MgrObject<T extends ID> {
	/** DataClay ID. */
	@Id
	private T dataClayID;

	/**
	 * Get dataClayID
	 * @return the dataClayID
	 */
	public final T getDataClayID() {
		return dataClayID;
	}

	/**
	 * Set dataClayID
	 * @param newdataClayID the dataClayID to set
	 */
	public final void setDataClayID(final T newdataClayID) {
		this.dataClayID = newdataClayID;
	}
}
