
/**
 * @file DataSetDoesNotExistException.java
 * 
 * @date Mar 3, 2014
 */

package es.bsc.dataclay.exceptions.logicmodule.datasetmgr;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ids.DataSetID;

/**
 * This class represents the exceptions produced in DataSetManager module when a DataSet does not exist.
 * 
 */
public class DataSetDoesNotExistException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = 2945486335743214140L;

	/**
	 * Exceptions produced when dataset identified by ID provided does not exist
	 * @param id
	 *            ID of the dataset
	 */
	public DataSetDoesNotExistException(final DataSetID id) {
		super(ERRORCODE.DATASET_NOT_EXIST, "The dataset " + id + " does not exist", false);
	}

	/**
	 * Exceptions produced when dataset identified by name provided does not exist
	 * @param datasetName
	 *            name of the dataset
	 */
	public DataSetDoesNotExistException(final String datasetName) {
		super(ERRORCODE.DATASET_NOT_EXIST, "The dataset " + datasetName + " does not exist", false);
	}
}
