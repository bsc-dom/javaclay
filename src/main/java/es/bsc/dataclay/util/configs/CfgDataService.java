
package es.bsc.dataclay.util.configs;

import es.bsc.dataclay.dbhandler.DBHandlerConf;

/**
 * This class represents the configuration used by Data Service.
 * 
 */
public final class CfgDataService {

	/** DataService name. */
	private final String name;
	/** DataService hostname. */
	private final String hostname;
	/** DataService TCP Port. */
	private final int tcpPort;
	/** DbHandler configuration. */
	private final DBHandlerConf dbHandlerConf;

	/**
	 * Creates a new CfgDataService with name and Database Handler specification provided
	 * @param newname
	 *            Hostname of the DataService
	 * @param newdbHandlerConf
	 *            Database Handler configuration
	 */
	public CfgDataService(final String newname, final DBHandlerConf newdbHandlerConf) {
		this.name = newname;
		this.dbHandlerConf = newdbHandlerConf;
		this.hostname = null;
		this.tcpPort = -1;
	}

	/**
	 * Creates a new CfgDataService with name, port, hostname and Database Handler specification provided
	 * @param newname
	 *            Name of the DataService
	 * @param newhostname
	 *            Hostname of the DataService
	 * @param newtcpPort
	 * 			  Port of TCP DataService
	 * @param newdbHandlerConf
	 *            Database Handler configuration
	 */
	public CfgDataService(final String newname, final String newhostname,  final int newtcpPort, 
			final DBHandlerConf newdbHandlerConf) {
		this.name = newname;
		this.hostname = newhostname;
		this.tcpPort = newtcpPort;
		this.dbHandlerConf = newdbHandlerConf;
	}

	/**
	 * Get the CfgDataService::name
	 * @return the port
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the CfgDataService::dbHandlerConf
	 * @return the dbHandlerConf
	 */

	public DBHandlerConf getDbHandlerConf() {
		return dbHandlerConf;
	}

	/**
	 * Get the CfgDataService::hostname
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * Get the CfgDataService::tcpPort
	 * @return the tcpPort
	 */
	public int getTcpPort() {
		return tcpPort;
	}
}
