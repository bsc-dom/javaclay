
package es.bsc.dataclay.logic.logicmetadata;

import es.bsc.dataclay.logic.accountmgr.AccountMgrSQLStatements;
import es.bsc.dataclay.logic.interfacemgr.InterfaceManagerSQLStatements;

import java.util.ResourceBundle;

/**
 * This class represents SQL statements.
 */
public final class LogicMetadataSQLStatements {


	/**
	 * Utility classes should have private constructor.
	 */
	private LogicMetadataSQLStatements() {

	}

	/**
	 * This enumeration represent all possible SQL statements.
	 */
	public enum SqlStatements {

		/** Create table. */
		CREATE_TABLE_LOGICMODULE,
		/** Create storage locations table. */
		CREATE_TABLE_STORAGE_LOCATIONS,
		/** Create execution environments table. */
		CREATE_TABLE_EXECUTION_ENVIRONMENTS,

		/** Drop table. */
		DROP_TABLE_LOGICMODULE,
		/** Drop storage locations table. */
		DROP_TABLE_STORAGE_LOCATIONS,
		/** Drop execution environments table. */
		DROP_TABLE_EXECUTION_ENVIRONMENTS,

		/** Insert. */
		INSERT_LOGICMODULE,
		/** Insert storage location. */
		INSERT_STORAGE_LOCATION,
		/** Insert execution environment. */
		INSERT_EXECUTION_ENVIRONMENT,

		/** Exists. */
		EXISTS_LOGICMODULE_BY_ID,
		/** Exists storage location by id. */
		EXISTS_STORAGE_LOCATION_BY_ID,
		/** Exists execution environment by id. */
		EXISTS_EXECUTION_ENVIRONMENT_BY_ID,
		/** Exists execution environment by host and port. */
		EXISTS_EXECUTION_ENVIRONMENT_BY_HOSTPORT,
		/** Exists storage location by host and port. */
		EXISTS_STORAGE_LOCATION_BY_HOSTPORT,


		/** Delete storage location. */
		DELETE_STORAGE_LOCATION,
		/** Delete execution environment. */
		DELETE_EXECUTION_ENVIRONMENT,


		/** Select all storage locations. */
		SELECT_ALL_LOCS,
		/** Select all execution environments. */
		SELECT_ALL_EXECENVS,
		/** Select all execution environments. */
		SELECT_ALL_EXECENVS_BY_LANG,

		/** Select storage location by name. */
		SELECT_STLOC_BY_NAME,
		/** Select exec environment by name and language. */
		SELECT_EXECENV_BY_NAME_LANG,
		/** Select exec environment by hostname and port. */
		SELECT_EXECENV_BY_HOSTNAME_AND_PORT,

		/** Update storage location. */
		UPDATE_STORAGE_LOCATION,
		/** Update execution environment. */
		UPDATE_EXECUTION_ENVIRONMENT,

		/** Table of external dataClay info. */
		CREATE_TABLE_DATACLAYS,


		/** Insert dataclay external info. */
		INSERT_DATACLAY_INFO,
		/** Select dataclay external info by id. */
		SELECT_DATACLAY_INFO_BY_ID,
		/** Select dataclay id from host and port. */
		SELECT_DATACLAY_ID_FROM_HOST_PORT,
		/** Delete dataClay instance. */
		DELETE_DATACLAY,

		/** Select all dataclays. */
		SELECT_ALL_DATACLAYS,

		/** Select. */
		SELECT_LOGICMODULE,
		/** Select storage location. */
		SELECT_STORAGE_LOCATION,
		/** Select execution environment. */
		SELECT_EXECUTION_ENVIRONMENT,

		/** Select all execution environments in host name. */
		SELECT_ALL_EXECENV_BY_HOSTNAME_AND_LANG,

		/** Vacuum db. */
		VACUUM;

		/** SQL statement. */
		private String sqlStatement;


		/** Indicates statements are loaded in memory. */
		private static boolean LOADED = false;
		/**
		 * Unload statements.
		 */
		public static void unloadStatements() {
			LOADED = false;
			for (AccountMgrSQLStatements.SqlStatements statement : AccountMgrSQLStatements.SqlStatements.values()) {
				statement.setSqlStatement(null);
			}
		}


		/**
		 * Init properties of the properties file
		 */
		public static void loadStatements() {
			ResourceBundle props = ResourceBundle.getBundle("es.bsc.dataclay.properties.logicmodule_metadata_sql");

			for (LogicMetadataSQLStatements.SqlStatements statement : LogicMetadataSQLStatements.SqlStatements.values()) {
				statement.init(props);
			}
			LOADED = true;
		}

		/**
		 * Init properties of the properties file
		 */
		private void init(ResourceBundle props) {
			final String sqlSt = props.getString(this.name());
			setSqlStatement(sqlSt);
		}

		/**
		 * Get the sqlStatement
		 * @return the sqlStatement
		 */
		public String getSqlStatement() {
			if (!LOADED) {
				loadStatements();
			}
			return sqlStatement;
		}

		/**
		 * Set the sqlStatement
		 * @param newsqlStatement
		 *            the sqlStatement
		 */
		public void setSqlStatement(final String newsqlStatement) {
			sqlStatement = newsqlStatement;
		}

	}
}
