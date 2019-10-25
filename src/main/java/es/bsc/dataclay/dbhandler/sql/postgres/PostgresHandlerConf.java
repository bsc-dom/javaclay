
package es.bsc.dataclay.dbhandler.sql.postgres;

import java.io.Serializable;

import es.bsc.dataclay.dbhandler.DBHandler;
import es.bsc.dataclay.dbhandler.DBHandlerConf;

/**
 * This class represents the Configuration needed to instantiate a Postgres Database.
 * 
 * @see CommonHandler class
 * 
 */
public final class PostgresHandlerConf extends DBHandlerConf implements Serializable {
	/** Serial version UID. */
	private static final long serialVersionUID = -3812225875953407435L;
	/** Host. */
	private String host;
	/** Port. */
	private int port;
	/** Connection username. */
	private String user;
	/** Connection password. */
	private String password;
	/** Name of the database. */
	private String dbname;

	/** Exception prefix msg to be checked in case of duplicates */
	public final static String DUPLICATE_ERROR_PREFIX_MSG = "ERROR: duplicate key value";
	public final static String MISSING_VALUE = "ERROR: no value specified for parameter";

	/**
	 * Instantiates a new configuration for Postgres.
	 * 
	 * @param newhost
	 *            Host name
	 * @param newport
	 *            Port
	 * @param newuser
	 *            user name
	 * @param newpassword
	 *            password
	 * @param newdbname
	 *            db name
	 */
	public PostgresHandlerConf(final String newhost, final int newport,
			final String newuser, final String newpassword, final String newdbname) {
		this.setHost(newhost);
		this.setPort(newport);
		this.setUser(newuser);
		this.setPassword(newpassword);
		this.setDbname(newdbname);
	}

	/**
	 * Empty constructor for YAML.
	 */
	public PostgresHandlerConf() {

	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param newhost
	 *            the host to set
	 */
	public void setHost(final String newhost) {
		this.host = newhost;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param newport
	 *            the port to set
	 */
	public void setPort(final int newport) {
		this.port = newport;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param newuser
	 *            the user to set
	 */
	public void setUser(final String newuser) {
		this.user = newuser;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param newpassword
	 *            the password to set
	 */
	public void setPassword(final String newpassword) {
		this.password = newpassword;
	}

	/**
	 * @return the dbname
	 */
	public String getDbname() {
		return dbname;
	}

	/**
	 * @param newdbname
	 *            the dbname to set
	 */
	@Override
	public void setDbname(final String newdbname) {
		this.dbname = newdbname.replace("-", "");
	}

	@Override
	public DBHandler getDBHandler() {
		return new PostgresHandler(this);
	}

}
