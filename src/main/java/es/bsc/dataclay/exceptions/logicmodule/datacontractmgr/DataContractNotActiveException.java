
/**
 * @file DataContractNotActiveException.java
 * 
 * @date Oct 5, 2012
 */
package es.bsc.dataclay.exceptions.logicmodule.datacontractmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.DataContractID;

/**
 * This class represents the exception produced in DataContractManager when a datacontract has to be used but it is not active.
 * 
 */
public final class DataContractNotActiveException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = 2624569877369276087L;

	/**
	 * Exception produced when a datacontract has to be used but it is not active.
	 * @param datacontractID
	 *            the id of the datacontract
	 */
	public DataContractNotActiveException(final DataContractID datacontractID) {
		super(ERRORCODE.DATACONTRACT_NOT_ACTIVE, "DataContract " + datacontractID + " is not active.", false);
	}
}
