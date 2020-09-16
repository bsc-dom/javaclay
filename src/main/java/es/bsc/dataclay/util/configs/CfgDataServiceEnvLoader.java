
package es.bsc.dataclay.util.configs;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.EnumMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.bsc.dataclay.dbhandler.DBHandlerConf;
import es.bsc.dataclay.dbhandler.DBHandlerFactory.DBHandlerType;
import es.bsc.dataclay.dbhandler.sql.sqlite.SQLiteHandlerConfig;
import es.bsc.dataclay.exceptions.DataClayRuntimeException;
import es.bsc.dataclay.exceptions.ErrorDefs.ERRORCODE;
import es.bsc.dataclay.util.ProcessEnvironment;
import es.bsc.dataclay.util.Configuration.Flags;

/**
 * This class parses the environment variables and generates a DataService configuration.
 * 
 * @see CfgManager
 */
public final class CfgDataServiceEnvLoader {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger("CfgDataService");
	
	/** Name of the DBHandlerConf for NVRAM (external JAR, dynamically loaded when needed). */
	private static final String NVRAM_HANDLER_CONF_CLASS_NAME = "es.bsc.dataclay.dbhandler.nvram.NVRAMHandlerConf";

	/**
	 * Constructor
	 * @throws IllegalAccessException
	 *             Prevent instantiation
	 */
	private CfgDataServiceEnvLoader() throws IllegalAccessException {
		throw new IllegalAccessException("This class cannot be instantiated");
	}

	/**
	 * The configurable properties.
	 * 
	 */
	public enum ConfigDataServiceProperties {

		/** DataService name environment variable name. */
		DS_NAME(null),
		/** DataService hostname environment variable name. */
		DS_HOSTNAME(null),
		/** DataService TCP port environment variable name. */
		DS_TCPPORT(null),
		/** DbHandler type field name. */
		DBHANDLER_TYPE(null),
		/** DbHandler path field name. */
		DB_PATH(DBHANDLER_TYPE),
		/** DataService paraver tag. */
		DS_PARAVERTAG(null);

		/** DbHandler type. */
		private ConfigDataServiceProperties dbhandlerType = null;

		/**
		 * ConfigDataServiceProperties constructor
		 * @param newdbhandlerType
		 *            DbHandler type
		 */
		ConfigDataServiceProperties(final ConfigDataServiceProperties newdbhandlerType) {
			this.setDbhandlerType(newdbhandlerType);
		}

		/**
		 * Get the {@link #dbhandlerType}
		 * @return the dbhandlerType
		 */

		public ConfigDataServiceProperties getDbhandlerType() {
			return dbhandlerType;
		}

		/**
		 * Set the {@link #dbhandlerType}
		 * @param newdbhandlerType
		 *            the dbhandlerType to set
		 */
		public void setDbhandlerType(final ConfigDataServiceProperties newdbhandlerType) {
			this.dbhandlerType = newdbhandlerType;
		}
	}

	/** The names of the config tags. */
	public static final Map<ConfigDataServiceProperties, String> ENVVAR_NAMES;

	/**
	 * Association between keys and strings that the configuration file must contain. It means, to look for a manager Port,
	 *        the parser must search "DataServicePort".
	 */
	static {
		ENVVAR_NAMES = new EnumMap<>(ConfigDataServiceProperties.class);
		ENVVAR_NAMES.put(ConfigDataServiceProperties.DS_NAME, "DATASERVICE_NAME");
		ENVVAR_NAMES.put(ConfigDataServiceProperties.DS_HOSTNAME, "DATASERVICE_HOST");
		ENVVAR_NAMES.put(ConfigDataServiceProperties.DS_TCPPORT, "DATASERVICE_JAVA_PORT_TCP");
		ENVVAR_NAMES.put(ConfigDataServiceProperties.DBHANDLER_TYPE, "DATASERVICE_DBHANDLER_TYPE");
		ENVVAR_NAMES.put(ConfigDataServiceProperties.DB_PATH, "DATASERVICE_DB_PATH");
	}

	/**
	 * This method parses the configuration from the environment
	 * @return The configuration of a DataService
	 */
	public static CfgDataService parseConfiguration() {
		final String name = ProcessEnvironment.getInstance().get(CfgDataServiceEnvLoader.ENVVAR_NAMES.get(ConfigDataServiceProperties.DS_NAME));

		/* @pierlauro sept. 2018
			TODO Change this ugly thing: it should be completely removed any static method to parse the configuration
			and instead of environment variables it should be used a configuration file
		*/

		final DBHandlerType dbHandlerType = (DBHandlerType)Flags.DB_HANDLER_TYPE_FOR_DATASERVICES.getValue();

		final DBHandlerConf dbHandlerConf = parseConfiguration(dbHandlerType, name);

		String hostname = ProcessEnvironment.getInstance().get(CfgDataServiceEnvLoader.ENVVAR_NAMES.get(ConfigDataServiceProperties.DS_HOSTNAME));

		if (hostname == null || hostname.isEmpty()) {
			try {
				hostname = InetAddress.getLocalHost().getHostAddress();
				LOGGER.info("Resolved inet address : " + InetAddress.getLocalHost());
				LOGGER.info("Resolved hostname : " + hostname);
			} catch (final UnknownHostException e) {
				LOGGER.info("Unknown hostname, using 127.0.0.1");
				// Fallback: localhost
				hostname = "127.0.0.1";
			}
		}
		
		final int tcpPort = new Integer(ProcessEnvironment.getInstance().get(CfgDataServiceEnvLoader.ENVVAR_NAMES.get(
				ConfigDataServiceProperties.DS_TCPPORT)));

		return new CfgDataService(name, hostname, tcpPort, dbHandlerConf);
	}

	/**
	 * Parse the configuration regarding the DBHandler.
	 * 
	 * @param dbHandlerType The specific type that should be used as the DBHandler.
	 * @param name Extra parameters in the configuration for the DBHandler.
	 * @return A DBHandlerConf for the specific DBHandler, null if not required.
	 */
	private static DBHandlerConf parseConfiguration(final DBHandlerType dbHandlerType, final String name) {
		switch(dbHandlerType) {
		case SQLITE:
			return new SQLiteHandlerConfig(name);
		case NVRAM:
			try {
				final Class<?> nvramHandlerConfClass = Class.forName(NVRAM_HANDLER_CONF_CLASS_NAME);
				if (DBHandlerConf.class.isAssignableFrom(nvramHandlerConfClass)) {
					final Constructor<?> constructor = nvramHandlerConfClass.getConstructor(String.class);
					return (DBHandlerConf) constructor.newInstance(name);
				} else {
					throw new IllegalArgumentException("Class NVRAMHandlerConfClass is not a DBHandlerConf");
				}
			} catch (final ClassNotFoundException ex) {
				throw new IllegalArgumentException("Could not import class for NVRAMHandlerConf, dependencies error");
			} catch (final NoSuchMethodException ex) {
				throw new IllegalArgumentException("Could not call NVRAMHandlerConf constructor");
			} catch (final InstantiationException ex) {
				throw new IllegalArgumentException("Could not instantiate a NVRAMHandlerConf instance");
			} catch (final InvocationTargetException ex) {
				throw new IllegalArgumentException("Could not invocate the NVRAMHandlerConf constructor");
			} catch (final IllegalAccessException ex) {
				throw new IllegalArgumentException("Could not legally access and call the NVRAMHandlerConf constructor");
			}
		default:
			throw new DataClayRuntimeException(ERRORCODE.BAD_DEFINED_DBHANDLER_CONF, "Bad defined DbHandler configuration", false);
		}
	}

}
