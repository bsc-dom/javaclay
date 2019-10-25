
/**
 * @file BackendNotExistException.java
 * 
 * @date May 28, 2013
 */
package es.bsc.dataclay.exceptions.metadataservice;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.StorageLocationID;
import es.bsc.dataclay.util.management.metadataservice.StorageLocation;

/**
 * This class represents the exceptions produced in MetaDataService module when a backend specified does not exist.
 * 
 */
public class StorageLocationNotExistException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = -7075663709157332521L;

	/**
	 * This exception is produced when a backend with ID provided does not exist
	 * @param backendID
	 *            ID of the backend
	 */
	public StorageLocationNotExistException(final StorageLocationID backendID) {
		super(ERRORCODE.STORAGE_LOCATION_NOT_EXIST, "StorageLocation " + backendID.toString() + " does not exist ", false);
	}

	/**
	 * This exception is produced when a certain name does not correspont to any Storage Location
	 * @param stLocName
	 *            Name of the not existant backend
	 */
	public StorageLocationNotExistException(final String stLocName) {
		super(ERRORCODE.STORAGE_LOCATION_NOT_EXIST, "StorageLocation named " + stLocName + " does not exist ", false);
	}

	/**
	 * This exception is produced when a backend with specifications does not exist
	 * @param backendProto
	 *            Specifications of the backend
	 */
	public StorageLocationNotExistException(final StorageLocation backendProto) {
		super(ERRORCODE.STORAGE_LOCATION_NOT_EXIST, "Backend " + backendProto.toString() + " does not exist ", false);
	}

}
