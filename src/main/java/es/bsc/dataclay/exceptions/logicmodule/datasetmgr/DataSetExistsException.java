
/**
 * @file DataSetExistsException.java
 * 
 * @date Mar 3, 2014
 */
package es.bsc.dataclay.exceptions.logicmodule.datasetmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;

/**
 * This class represents the exceptions produced in DataSetManager module when a DataSet already exists.
 */
public class DataSetExistsException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = -3279647479553596849L;

	/**
	 * Exceptions produced when dataset identified by name provided already exists
	 * @param datasetName
	 *            Name of the dataset
	 */
	public DataSetExistsException(final String datasetName) {
		super(ERRORCODE.DATASET_EXISTS, "DataSet: " + datasetName + " already exists", false);
	}
}
