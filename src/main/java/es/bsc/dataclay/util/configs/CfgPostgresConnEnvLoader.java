
package es.bsc.dataclay.util.configs;

import java.util.EnumMap;
import java.util.Map;

import es.bsc.dataclay.dbhandler.sql.postgres.PostgresHandlerConf;
import es.bsc.dataclay.util.ProcessEnvironment;

/**
 * This class parses the information of the Postgres connection from
 * environment variables.
 */
public final class CfgPostgresConnEnvLoader {

	/**
	 * The configurable properties.
	 */
	public enum ConfigLogicProperties {
		/** Postgres hostname environment variable name. */
		PG_HOST,
		/** Postgres port environment variable name. */
		PG_PORT,
		/** Postgres user environment variable name. */
		PG_USER,
		/** Postgres password environment variable name. */
		PG_PASSWORD,
		/** Postgres table environment variable name. */
		PG_DBNAME
	}
	
	/** The names of the config tags. */
	public static final Map<ConfigLogicProperties, String> ENVVAR_NAMES = new EnumMap<ConfigLogicProperties, String>(ConfigLogicProperties.class);

	/**
	 * Association between keys and strings which the Environment Variables will be named.
	 */
	static {
		ENVVAR_NAMES.put(ConfigLogicProperties.PG_HOST, "POSTGRES_HOST");
		ENVVAR_NAMES.put(ConfigLogicProperties.PG_PORT, "POSTGRES_PORT");
		ENVVAR_NAMES.put(ConfigLogicProperties.PG_USER, "POSTGRES_USER");
		ENVVAR_NAMES.put(ConfigLogicProperties.PG_PASSWORD, "POSTGRES_PASSWORD");
		ENVVAR_NAMES.put(ConfigLogicProperties.PG_DBNAME, "POSTGRES_DBNAME");
	}


	/**
	 * Constructor
	 * @throws IllegalAccessException
	 *             Prevent instantiation
	 */
	private CfgPostgresConnEnvLoader() throws IllegalAccessException {
		throw new IllegalAccessException("This class cannot be instantiated");
	}

	/**
	 * Get postgres configuration.
	 * @return Postgres configuration.
	 */
	public static PostgresHandlerConf parsePostgresConn() {
		ENVVAR_NAMES.put(ConfigLogicProperties.PG_HOST, "POSTGRES_HOST");
		ENVVAR_NAMES.put(ConfigLogicProperties.PG_PORT, "POSTGRES_PORT");
		ENVVAR_NAMES.put(ConfigLogicProperties.PG_USER, "POSTGRES_USER");
		ENVVAR_NAMES.put(ConfigLogicProperties.PG_PASSWORD, "POSTGRES_PASSWORD");
		ENVVAR_NAMES.put(ConfigLogicProperties.PG_DBNAME, "POSTGRES_DBNAME");
		final int port = new Integer(ProcessEnvironment.getInstance().get(CfgPostgresConnEnvLoader.ENVVAR_NAMES.get(
				ConfigLogicProperties.PG_PORT)));
		final String dbname = ProcessEnvironment.getInstance().get(CfgPostgresConnEnvLoader.ENVVAR_NAMES.get(
				ConfigLogicProperties.PG_DBNAME));
		final String host = ProcessEnvironment.getInstance().get(CfgPostgresConnEnvLoader.ENVVAR_NAMES.get(
				ConfigLogicProperties.PG_HOST));
		final String user = ProcessEnvironment.getInstance().get(CfgPostgresConnEnvLoader.ENVVAR_NAMES.get(
				ConfigLogicProperties.PG_USER));
		final String password = ProcessEnvironment.getInstance().get(CfgPostgresConnEnvLoader.ENVVAR_NAMES.get(
				ConfigLogicProperties.PG_PASSWORD));
		return new PostgresHandlerConf(host, port, user, password, dbname); 
	}
}
