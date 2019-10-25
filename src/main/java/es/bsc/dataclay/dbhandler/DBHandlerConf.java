
/**
 * @file DbHandlerConf.java
 * @date Oct 25, 2012
 */
package es.bsc.dataclay.dbhandler;

import java.io.Serializable;

/**
 * This class represents the Configuration needed to instatiate any DbHandler.
 * 
 */
public abstract class DBHandlerConf implements Serializable {

	/** Serial version UID. */
	private static final long serialVersionUID = -5753230759347035529L;

	/**
	 * Get DB handler
	 * 
	 * @return DB handler.
	 */
	public abstract DBHandler getDBHandler();

	/**
	 * Set DB to work with.
	 * 
	 * @param newdbname
	 *            DB name
	 */
	public abstract void setDbname(final String newdbname);
}
