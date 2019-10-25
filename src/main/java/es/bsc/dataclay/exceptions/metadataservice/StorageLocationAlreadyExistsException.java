
/**
 * @file BackendAlreadyExistsException.java
 * 
 * @date May 28, 2013
 */
package es.bsc.dataclay.exceptions.metadataservice;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.management.metadataservice.StorageLocation;

/**
 * This class represents the exceptions produced in MetaDataService module when you try to add a new Storage Location that
 * already exists in the database.
 * 
 */
public class StorageLocationAlreadyExistsException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = 6844394614456947827L;

	/**
	 * This exception is produced when a Storage Location with ID provided already exists
	 * @param newStorageLocation
	 *            specs of the Storage Location
	 */
	public StorageLocationAlreadyExistsException(final StorageLocation newStorageLocation) {
		super(ERRORCODE.STORAGE_LOCATION_ALREADY_EXIST, "Storage Location " + newStorageLocation.toString() + " already exists", false);
	}

}
