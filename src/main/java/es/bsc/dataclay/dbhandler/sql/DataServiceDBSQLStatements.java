
package es.bsc.dataclay.dbhandler.sql;

import es.bsc.dataclay.logic.accountmgr.AccountMgrSQLStatements;
import es.bsc.dataclay.metadataservice.MetaDataServiceSQLStatements;

import java.util.ResourceBundle;

/**
 * This class represents SQL statements for Account Manager.
 */
public final class DataServiceDBSQLStatements {



	/**
	 * Utility classes should have private constructor.
	 */
	private DataServiceDBSQLStatements() {

	}

	/**
	 * This enumeration represent all possible SQL statements.
	 */
	public enum SqlStatements {

		/** Create DS table. */
		CREATE_TABLE_DATASERVICE,

		/** Drop DS table. */
		DROP_TABLE_DATASERVICE,

		/** Insert object sql query. */
		INSERT_OBJECT,

		/** Select object sql query. */
		SELECT_OBJECT,

		/** Exists object query. */
		EXISTS_OBJECT,

		/** Delete object. */
		DELETE_OBJECT,

		/** Vacuum database. */
		VACUUM,

		/** Count objects query. */
		COUNT_OBJECTS,

		/** Update object. */
		UPDATE_OBJECT;

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
			ResourceBundle props = ResourceBundle.getBundle("es.bsc.dataclay.properties.dataservice_db_sql");
			for (DataServiceDBSQLStatements.SqlStatements statement : DataServiceDBSQLStatements.SqlStatements.values()) {
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
