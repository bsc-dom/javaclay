
package es.bsc.dataclay.logic.datasetmgr;

import java.util.ResourceBundle;

/**
 * This class represents SQL statements for Data Contract Manager.
 */
public final class DataSetManagerSQLStatements {

	/** Properties. */
	private static ResourceBundle props = null;

	/**
	 * Utility classes should have private constructor.
	 */
	private DataSetManagerSQLStatements() {

	}

	/**
	 * Init properties of the properties file
	 */
	static {
		if (props == null) {
			props = ResourceBundle.getBundle("es.bsc.dataclay.properties.dataset_mgr_sql");
		}
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

		/**
		 * Init properties of the properties file
		 */
		private void init() {
			final String sqlSt = props.getString(this.name());
			setSqlStatement(sqlSt);
		}

		/**
		 * Get the sqlStatement
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
		 * @param newsqlStatement
		 *            the sqlStatement
		 */
		public void setSqlStatement(final String newsqlStatement) {
			sqlStatement = newsqlStatement;
		}

	}
}
