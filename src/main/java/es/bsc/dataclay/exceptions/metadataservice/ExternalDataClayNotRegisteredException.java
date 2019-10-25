
package es.bsc.dataclay.exceptions.metadataservice;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.DataClayInstanceID;

/**
 * This class represents the exceptions produced in MetaDataService module when an external dataClay specified is not registered
 */
public class ExternalDataClayNotRegisteredException extends DataClayException {

	private static final long serialVersionUID = 4980518877820279384L;

	/**
	 * This exception is produced when an external dataClay with ID provided is not registered
	 * @param dataClayID
	 *            ID of the external dataClay instance
	 */
	public ExternalDataClayNotRegisteredException(final DataClayInstanceID dataClayID) {
		super(ERRORCODE.EXTERNAL_DATACLAY_NOT_REGISTERED, "ExternalDataClay " + dataClayID.toString() + " does not exist ", true);
	}

	/**
	 * This exception is produced when an external dataClay with name provided is not registered
	 * @param dataClayName
	 *            name of the external dataClay instance
	 */
	public ExternalDataClayNotRegisteredException(final String dataClayName) {
		super(ERRORCODE.EXTERNAL_DATACLAY_NOT_REGISTERED, "ExternalDataClay " + dataClayName + " does not exist ", true);
	}
	
	/**
	 * This exception is produced when an external dataClay with name provided is not registered
	 * @param host
	 *            host of the external dataClay instance
	 * @param port port of external dataclay
	 */
	public ExternalDataClayNotRegisteredException(final String host, final Integer port) {
		super(ERRORCODE.EXTERNAL_DATACLAY_NOT_REGISTERED, "ExternalDataClay " + host
				+ ":" + port + " does not exist ", true);
	}
}
