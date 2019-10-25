
/**
 * @file DataContractNotPublicException.java
 * 
 * @date Mar 3, 2014
 */
package es.bsc.dataclay.exceptions.logicmodule.datacontractmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.DataContractID;

/**
 * This class represents the exception produced in DataContractManager when a datacontract is expected to be public and it is
 * not.
 * 
 */
public final class DataContractNotPublicException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = -6041519064321169888L;

	/**
	 * Exception produced when a datacontract is expected to be public and it is not.
	 * @param datacontractID
	 *            ID of the datacontract
	 */
	public DataContractNotPublicException(final DataContractID datacontractID) {
		super(ERRORCODE.DATACONTRACT_NOT_PUBLIC, "DataContract " + datacontractID + " is not public", false);
	}
}
