
/**
 * @file DataContractNotExistException.java
 * 
 * @date Mar 3, 2014
 */
package es.bsc.dataclay.exceptions.logicmodule.datacontractmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.DataContractID;

/**
 * This class represents the exception produced in DataContractManager when a datacontract does not exist.
 * 
 */
public final class DataContractNotExistException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = 6912792947592131814L;

	/**
	 * Exception produced when a datacontract does not exist .
	 * @param datacontractID
	 *            the id of the datacontract
	 */
	public DataContractNotExistException(final DataContractID datacontractID) {
		super(ERRORCODE.DATACONTRACT_NOT_EXIST, "DataContract " + datacontractID + " does not exist.", false);
	}
}
