
/**
 * @file ObjectAlreadyRegisteredException.java
 * 
 * @date May 28, 2013
 */
package es.bsc.dataclay.exceptions.metadataservice;

import es.bsc.dataclay.exceptions.DataClayException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;

/**
 * This class represents the exceptions produced in MetaDataService module when some alias is already being used.
 * 
 */
public class AliasAlreadyInUseException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = 3916887391434457843L;

	/**
	 * This exception is produced when some alias is already being used.
	 * 
	 * @param alias
	 *            Alias of the object
	 */
	public AliasAlreadyInUseException(final String alias) {
		super(ERRORCODE.ALIAS_ALREADY_EXISTS, "There is an object with alias " + alias + " already registered", false);
	}
}
