
package es.bsc.dataclay.dbhandler.sql.sqlite;

import java.io.File;

import es.bsc.dataclay.dbhandler.DBHandler;
import es.bsc.dataclay.dbhandler.DBHandlerConf;
import es.bsc.dataclay.util.Configuration;

public class SQLiteHandlerConfig extends DBHandlerConf {

	private static final long serialVersionUID = -2609239598328535411L;

	private String folder;

	private boolean inMemory = false;

	private String dbName;

	public SQLiteHandlerConfig() {}

	public SQLiteHandlerConfig(final String path, final String newdbname, boolean inMemory) {
		this.dbName = newdbname;
		this.inMemory = inMemory;
		this.folder = path;
	}

	/**
	 * Gets folder.
	 * 
	 * @return the folder
	 */
	public String getFolder() {
		return folder;
	}


	@Override
	public DBHandler getDBHandler() {
		// create directory
		new File(getFolder()).mkdirs();
		return new SQLiteHandler(this);
	}

	public String getDbPath() {
		return folder + '/' + dbName;
	}

	public boolean isInMemory() {
		return inMemory;
	}

	@Override
	public String toString() {
		return "SQLiteHandlerConfig [dbPath = " + getDbPath() + "]";
	}

	/**
	 * @param newdbname
	 *            the dbname to set
	 */
	@Override
	public void setDbname(final String newdbname) {
		this.dbName = newdbname;
	}
}
