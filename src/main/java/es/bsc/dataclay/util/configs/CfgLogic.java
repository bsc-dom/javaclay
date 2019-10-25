
/**
 * @file CfgLogic.java
 * 
 * @date Oct 25, 2012
 */

package es.bsc.dataclay.util.configs;

/**
 * This class represents the configuration used by Logic Module (ports of all managers, ...).
 * 
 */
public final class CfgLogic {

	/** Logic module name. */
	private final String logicModuleName;
	/** Logic module port. */
	private final int logicModuleTCPPort;
	/** Logic host name. */
	private final String logicModuleHostName;
	/** In memory */
	private final boolean inMemory;
	/** IPs to be send to clients when information of a registered EE/SL is required. Can be null.*/
	private final String exposedIPForClient;
	
	/**
	 * CfgLogic constructor
	 * 
	 */
	public CfgLogic(final String logicModuleName, final String logicModuleHostName, 
			final int logicModuleTCPPort, final boolean inMemory, 
			final String theexposedIPForClient) {
		this.logicModuleName = logicModuleName;
		this.logicModuleHostName = logicModuleHostName;
		this.logicModuleTCPPort = logicModuleTCPPort;
		this.inMemory = inMemory;
		this.exposedIPForClient = theexposedIPForClient;
	}

	/**
	 * @return the logicModuleTCPPort
	 */
	public int getLogicModuleTCPPort() {
		return logicModuleTCPPort;
	}

	/**
	 * @return the logicModuleHostName
	 */
	public String getLogicModuleHostName() {
		return logicModuleHostName;
	}

	/**
	 * @return the logicModuleName
	 */
	public String getLogicModuleName() {
		return logicModuleName;
	}

	public boolean isInMemory() {
		return inMemory;
	}

	/**
	 * Gets exposedIPForClient.
	 * @return the exposedIPForClient
	 */
	public String getExposedIPForClient() {
		return exposedIPForClient;
	}
}
