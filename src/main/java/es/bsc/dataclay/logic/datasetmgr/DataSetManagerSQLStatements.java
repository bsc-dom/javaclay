
package es.bsc.dataclay.logic.datasetmgr;

import es.bsc.dataclay.logic.accountmgr.AccountMgrSQLStatements;
import es.bsc.dataclay.logic.datacontractmgr.DataContractManagerSQLStatements;

import java.util.ResourceBundle;

/**
 * This class represents SQL statements for Data Contract Manager.
 */
public final class DataSetManagerSQLStatements {

	/**
	 * Utility classes should have private constructor.
	 */
	private DataSetManagerSQLStatements() {

	}

	/**
	 * This enumeration represent all possible SQL statements.
	 */
	public enum SqlStatements {

		/** Create. */
		CREATE_TABLE_DATASET,

		/** Drop. */
		DROP_TABLE_DATASET,

		/** Insert. */
		INSERT_DATASET,

		/** Select account. */
		SELECT_DATASET,

		/** Delete. */
		DELETE_DATASET,

		/** Select a dataset by name. */
		SELECT_DATASET_BY_NAME,
		
		/** Select public datasets. */
		SELECT_PUBLIC_DATASETS,

		/** Select account's datasets */
		SELECT_ACCOUNT_DATASETS;

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
			ResourceBundle props = ResourceBundle.getBundle("es.bsc.dataclay.properties.dataset_mgr_sql");

			for (DataSetManagerSQLStatements.SqlStatements statement : DataSetManagerSQLStatements.SqlStatements.values()) {
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
