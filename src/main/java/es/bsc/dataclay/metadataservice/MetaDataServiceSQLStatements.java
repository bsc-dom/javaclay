
package es.bsc.dataclay.metadataservice;

import es.bsc.dataclay.logic.accountmgr.AccountMgrSQLStatements;
import es.bsc.dataclay.logic.sessionmgr.SessionManagerSQLStatements;

import java.util.ResourceBundle;

/**
 * This class represents SQL statements.
 */
public final class MetaDataServiceSQLStatements {



	/**
	 * Utility classes should have private constructor.
	 */
	private MetaDataServiceSQLStatements() {

	}

	/**
	 * This enumeration represent all possible SQL statements.
	 */
	public enum SqlStatements {

		/** Create metadata table. */
		CREATE_TABLE_METADATA,

		/** Drop metadata table. */
		DROP_TABLE_METADATA,

		/** Insert metadata. */
		INSERT_METADATA,

		/** Select metadata. */
		SELECT_METADATA,
		/** Get all object ids in system. */
		SELECT_ALL_OIDS,

		/** Exists metadata by id. */
		EXISTS_METADATA_BY_ID,

		/** Exists metadata by dataset id. */
		EXISTS_METADATA_BY_DATASETID,
		/** Delete metadata. */
		DELETE_METADATA,

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

		/** Table of objects federated with external dataClay instances. */
		CREATE_TABLE_FEDERATED_OBJECTS,
		/** Table of external objects federated from an external dataClay instance. */
		CREATE_TABLE_EXTERNAL_OBJECTS,

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
			ResourceBundle props = ResourceBundle.getBundle("es.bsc.dataclay.properties.metadata_service_sql");

			for (MetaDataServiceSQLStatements.SqlStatements statement : MetaDataServiceSQLStatements.SqlStatements.values()) {
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
		 * 
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
		 * 
		 * @param newsqlStatement
		 *            the sqlStatement
		 */
		public void setSqlStatement(final String newsqlStatement) {
			sqlStatement = newsqlStatement;
		}
	}
}
