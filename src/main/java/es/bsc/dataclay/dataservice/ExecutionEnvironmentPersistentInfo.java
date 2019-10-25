
package es.bsc.dataclay.dataservice;

import java.io.Serializable;

import es.bsc.dataclay.util.ids.ExecutionEnvironmentID;
import es.bsc.dataclay.util.ids.StorageLocationID;

/**
 * Contains miscellanous Execution Environment information.
 */
public final class ExecutionEnvironmentPersistentInfo implements Serializable {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 4730439425094331242L;
	
	/** Execution environment ID. */
	private ExecutionEnvironmentID executionEnvironmentID;

	/** Storage location ID. */
	private StorageLocationID storageLocationID;
	
	/**
	 * PersistentCaches constructor
	 * @param theexecutionEnvironmentID ID of execution environment
	 * @param thestorageLocationID ID of storage location
	 */
	public ExecutionEnvironmentPersistentInfo( 
			final ExecutionEnvironmentID theexecutionEnvironmentID, 
			final StorageLocationID thestorageLocationID) { 
		this.setExecutionEnvironmentID(theexecutionEnvironmentID);
		this.setStorageLocationID(thestorageLocationID);
	}

	/**
	 * Gets executionEnvironmentID.
	 * @return the executionEnvironmentID
	 */
	public ExecutionEnvironmentID getExecutionEnvironmentID() {
		return executionEnvironmentID;
	}


	/**
	 * Sets executionEnvironmentID
	 * @param theexecutionEnvironmentID the executionEnvironmentID to set
	 */
	public void setExecutionEnvironmentID(final ExecutionEnvironmentID theexecutionEnvironmentID) {
		this.executionEnvironmentID = theexecutionEnvironmentID;
	}


	/**
	 * Gets storageLocationID.
	 * @return the storageLocationID
	 */
	public StorageLocationID getStorageLocationID() {
		return storageLocationID;
	}


	/**
	 * Sets storageLocationID
	 * @param thestorageLocationID the storageLocationID to set
	 */
	public void setStorageLocationID(final StorageLocationID thestorageLocationID) {
		this.storageLocationID = thestorageLocationID;
	}
	
}
