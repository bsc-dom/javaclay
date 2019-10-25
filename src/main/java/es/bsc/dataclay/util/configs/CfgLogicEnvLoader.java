package es.bsc.dataclay.util.configs;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.EnumMap;
import java.util.Map;

import es.bsc.dataclay.util.ProcessEnvironment;

/**
 * This class parses the environment variables and generates a Logic Module configuration.
 * 
 * @see CfgLogic
 * 
 */
public final class CfgLogicEnvLoader {
	/**
	 * CfgLogicParser constructor.
	 * @throws IllegalAccessException
	 *             Prevent instantiation
	 */
	private CfgLogicEnvLoader() throws IllegalAccessException {
		throw new IllegalAccessException("This class cannot be instantiated");
	}

	/**
	 * The configurable properties.
	 * 
	 */
	public enum ConfigLogicProperties {
		/** Logic module name. */
		LOGIC_MOD_NAME,
		/** Logic module tcp port environment variable name. */
		LOGIC_MOD_TCPPORT,
		/** Logic module host environment variable name. */
		LOGIC_MOD_HOSTNAME,
		/** IPs to be send to clients when information of a registered EE/SL is required. Can be null.*/
		EXPOSED_IP_FOR_CLIENT,
		/** In memory database. */
		LOGIC_MOD_IN_MEMORY
	}

	/** The names of the config tags. */
	public static final Map<ConfigLogicProperties, String> ENVVAR_NAMES;

	/**
	 * Association between keys and strings that the configuration file must contain. It means, to look for a Logic Module
	 *        Port, the parser must search "LogicModulePort".
	 */
	static {
		ENVVAR_NAMES = new EnumMap<>(ConfigLogicProperties.class);
		ENVVAR_NAMES.put(ConfigLogicProperties.LOGIC_MOD_TCPPORT, "LOGICMODULE_PORT_TCP");
		ENVVAR_NAMES.put(ConfigLogicProperties.LOGIC_MOD_HOSTNAME, "LOGICMODULE_HOST");
		ENVVAR_NAMES.put(ConfigLogicProperties.LOGIC_MOD_NAME, "LOGICMODULE_NAME");
		ENVVAR_NAMES.put(ConfigLogicProperties.LOGIC_MOD_IN_MEMORY, "LOGICMODULE_IN_MEMORY");
		ENVVAR_NAMES.put(ConfigLogicProperties.EXPOSED_IP_FOR_CLIENT, "EXPOSED_IP_FOR_CLIENT");

	}

	/**
	 * This method parses the configuration from the environment
	 */
	public static CfgLogic parseConfiguration() {

		final int logicport = new Integer(ProcessEnvironment.getInstance().get(CfgLogicEnvLoader.ENVVAR_NAMES.get(
				ConfigLogicProperties.LOGIC_MOD_TCPPORT)));
		String logicHostName = ProcessEnvironment.getInstance().get(CfgLogicEnvLoader.ENVVAR_NAMES.get(
				ConfigLogicProperties.LOGIC_MOD_HOSTNAME));
		if (logicHostName == null || logicHostName.isEmpty()) {
			try {
				logicHostName = InetAddress.getLocalHost().getHostAddress();
			} catch (final UnknownHostException e) {
				e.printStackTrace();
				// Fallback: localhost
				logicHostName = "127.0.0.1";
			}
		}
		String logicName = ProcessEnvironment.getInstance().get(CfgLogicEnvLoader.ENVVAR_NAMES.get(
				ConfigLogicProperties.LOGIC_MOD_NAME));
		if (logicName == null || logicName.isEmpty()) {
			logicName = "LM";
		}
		
		final String exposedIPForClient =  ProcessEnvironment.getInstance().get(CfgLogicEnvLoader.ENVVAR_NAMES.get(
				ConfigLogicProperties.EXPOSED_IP_FOR_CLIENT));
		final String inMemoryEnv = ProcessEnvironment.getInstance().get(ENVVAR_NAMES.get(ConfigLogicProperties.LOGIC_MOD_IN_MEMORY));
		final boolean inMemory = inMemoryEnv != null ? Boolean.parseBoolean(inMemoryEnv) : false;

		return new CfgLogic(logicName, logicHostName, logicport, inMemory, exposedIPForClient);
	}
}
