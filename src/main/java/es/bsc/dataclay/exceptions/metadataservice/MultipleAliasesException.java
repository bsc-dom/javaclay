
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
public class MultipleAliasesException extends DataClayException {

	/** Serial version UID. */
	private static final long serialVersionUID = 3916887391484457843L;

	/**
	 * This exception is produced when a second alias is added to an object.
	 * 
	 * @param alias
	 *            current alias of the object
	 * @param newAlias
	 *            new alias of the object
	 */
	public MultipleAliasesException(final String alias, final String newAlias) {
		super(ERRORCODE.MULTIPLE_ALIASES, "Cannot assign alias '" + newAlias + "' to object that already has an alias ('" + alias + "')", true);
	}
}
