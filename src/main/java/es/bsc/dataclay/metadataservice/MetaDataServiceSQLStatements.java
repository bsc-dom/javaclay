
package es.bsc.dataclay.metadataservice;

import java.util.ResourceBundle;

/**
 * This class represents SQL statements.
 */
public final class MetaDataServiceSQLStatements {

	/** Properties. */
	private static ResourceBundle props = null;

	/**
	 * Utility classes should have private constructor.
	 */
	private MetaDataServiceSQLStatements() {

	}

	/**
	 * Init properties of the properties file
	 */
	static {
		if (props == null) {
			props = ResourceBundle.getBundle("es.bsc.dataclay.properties.metadata_service_sql");
		}
	}

	/**
	 * This enumeration represent all possible SQL statements.
	 */
	public enum SqlStatements {

		/** Create metadata table. */
		CREATE_TABLE_METADATA,
		/** Create storage locations table. */
		CREATE_TABLE_STORAGE_LOCATIONS,
		/** Create execution environments table. */
		CREATE_TABLE_EXECUTION_ENVIRONMENTS,

		/** Drop metadata table. */
		DROP_TABLE_METADATA,
		/** Drop storage locations table. */
		DROP_TABLE_STORAGE_LOCATIONS,
		/** Drop execution environments table. */
		DROP_TABLE_EXECUTION_ENVIRONMENTS,

		/** Insert metadata. */
		INSERT_METADATA,
		/** Insert storage location. */
		INSERT_STORAGE_LOCATION,
		/** Insert execution environment. */
		INSERT_EXECUTION_ENVIRONMENT,

		/** Select metadata. */
		SELECT_METADATA,
		/** Get all object ids in system. */
		SELECT_ALL_OIDS,
		/** Select storage location. */
		SELECT_STORAGE_LOCATION,
		/** Select execution environment. */
		SELECT_EXECUTION_ENVIRONMENT,

		/** Exists metadata by id. */
		EXISTS_METADATA_BY_ID,
		/** Exists storage location by id. */
		EXISTS_STORAGE_LOCATION_BY_ID,
		/** Exists execution environment by id. */
		EXISTS_EXECUTION_ENVIRONMENT_BY_ID,
		/** Exists execution environment by host and port. */
		EXISTS_EXECUTION_ENVIRONMENT_BY_HOSTPORT,
		/** Exists storage location by host and port. */
		EXISTS_STORAGE_LOCATION_BY_HOSTPORT,

		/** Exists metadata by dataset id. */
		EXISTS_METADATA_BY_DATASETID,
		/** Delete metadata. */
		DELETE_METADATA,

		/** Delete storage location. */
		DELETE_STORAGE_LOCATION,
		/** Delete execution environment. */
		DELETE_EXECUTION_ENVIRONMENT,

		/** Update aliases. */
		UPDATE_ALIAS_METADATA,
		/** Update dataset id. */
		UPDATE_DATASETID_METADATA,
		/** Update read only metadata. */
		UPDATE_READONLY_METADATA,
		/** Update st. locs metadata. */
		UPDATE_LOCS_METADATA,
		/** Update for replica. */
		UPDATE_FOR_REPLICA_METADATA,

		/** Select all metadata from class. */
		SELECT_METADATA_FOR_CLASS,
		/** Select metadata by alias (and class) */
		SELECT_METADATA_BY_ALIAS,

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
		/** Table of objects federated with external dataClay instances. */
		CREATE_TABLE_FEDERATED_OBJECTS,
		/** Table of external objects federated from an external dataClay instance. */
		CREATE_TABLE_EXTERNAL_OBJECTS,

		/** Insert dataclay external info. */
		INSERT_DATACLAY_INFO,
		/** Select dataclay external info by id. */
		SELECT_DATACLAY_INFO_BY_ID,
		/** Select dataclay external info by name. */
		SELECT_DATACLAY_INFO_BY_NAME,
		/** Select dataclay id from host and port. */
		SELECT_DATACLAY_ID_FROM_HOST_PORT,
		/** Delete dataClay instance. */
		DELETE_DATACLAY,
		
		/** Select all dataclays. */
		SELECT_ALL_DATACLAYS,

		/** Insert a federated object with an external dataClay instance. */
		INSERT_FEDERATED_OBJECT,
		/** Delete a federated object with an external dataClay instance. */
		DELETE_FEDERATED_OBJECT,
		/** Check if an object is already federated with a dataClay instance. */
		EXISTS_FEDERATED_OBJECT_WITH_DC,
		/** Check if an object is federated. */
		EXISTS_FEDERATED_OBJECT,
		/** Select dataClay ids for which an object has been federated. */
		SELECT_TARGET_DATACLAYS_FEDERATED_OBJECT,

		/** Insert an object federated from an external dataClay instance. */
		INSERT_EXTERNAL_OBJECT,
		/** Delete an object federated from an external dataClay instance. */
		DELETE_EXTERNAL_OBJECT,
		/** Check if an object is federated from an external dataClay instance. */
		EXISTS_EXTERNAL_OBJECT,
		/** Select id of external dataClay federating an object with current dataClay instance. */
		SELECT_SOURCE_DATACLAY_EXTERNAL_OBJECT,
		
		/** Select all unregistered external objects. */
		SELECT_UNREGISTERED_EXTERNAL_OBJECTS,
		/** Update external object to be unregistered. */
		UPDATE_UNREGISTER_EXTERNAL_OBJECT,

		/** Select objects federated/belonging to external dataClay. */
		SELECT_OBJECTS_FEDERATED_WITH_DATACLAY,
		
		/** Select all execution environments in host name. */
		SELECT_ALL_EXECENV_BY_HOSTNAME_AND_LANG;

		/** SQL statement. */
		private String sqlStatement;

		/**
		 * Init properties of the properties file
		 */
		private void init() {
			final String sqlSt = props.getString(this.name());
			setSqlStatement(sqlSt);
		}

		/**
		 * Get the sqlStatement
		 * 
		 * @return the sqlStatement
		 */
		public String getSqlStatement() {
			if (sqlStatement == null) {
				init();
			}
			return sqlStatement;
		}

		/**
		 * Set the sqlStatement
		 * 
		 * @param newsqlStatement
		 *            the sqlStatement
		 */
		public void setSqlStatement(final String newsqlStatement) {
			sqlStatement = newsqlStatement;
		}
	}
}
